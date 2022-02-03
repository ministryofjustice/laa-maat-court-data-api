package gov.uk.courtdata.prosecutionconcluded.service;

import com.google.gson.Gson;
import gov.uk.courtdata.entity.WQHearingEntity;
import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.exception.MaatRecordLockedException;
import gov.uk.courtdata.prosecutionconcluded.dto.ConcludedDTO;
import gov.uk.courtdata.prosecutionconcluded.helper.CalculateOutcomeHelper;
import gov.uk.courtdata.prosecutionconcluded.helper.ReservationsRepositoryHelper;
import gov.uk.courtdata.prosecutionconcluded.impl.ProsecutionConcludedImpl;
import gov.uk.courtdata.prosecutionconcluded.listner.request.ProsecutionConcludedValidator;
import gov.uk.courtdata.prosecutionconcluded.listner.request.OffenceSummary;
import gov.uk.courtdata.prosecutionconcluded.listner.request.ProsecutionConcluded;
import gov.uk.courtdata.publisher.AwsStandardSqsPublisher;
import gov.uk.courtdata.repository.WQHearingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProsecutionConcludedService {

    private final CalculateOutcomeHelper calculateOutcomeHelper;

    private final WQHearingRepository wqHearingRepository;

    private final ProsecutionConcludedValidator prosecutionConcludedValidator;

    private final ProsecutionConcludedImpl prosecutionConcludedImpl;

    private final ReservationsRepositoryHelper reservationsRepositoryHelper;

    private final AwsStandardSqsPublisher awsStandardSqsPublisher;

    @Value("${cloud-platform.aws.sqs.queue.prosecutionConcluded}")
    private String sqsQueueName = "";

    private final Gson gson;


    public void execute(final ProsecutionConcluded prosecutionConcluded) {

        prosecutionConcludedValidator.validateRequestObject(prosecutionConcluded);

        WQHearingEntity wqHearingEntity = getWqHearingEntity(prosecutionConcluded);

        if (prosecutionConcluded.isConcluded()
                && wqHearingEntity != null
                && JurisdictionType.CROWN.name().equalsIgnoreCase(wqHearingEntity.getWqJurisdictionType())) {

            if (reservationsRepositoryHelper.isMaatRecordLocked(prosecutionConcluded.getMaatId())) {
                publishMessageToProsecutionSQS(prosecutionConcluded);
            } else {

                prosecutionConcludedValidator.validateOuCode(wqHearingEntity.getOuCourtLocation());
                String calculatedOutcome = calculateOutcomeHelper.calculate(prosecutionConcluded);
                log.info("calculated outcome is {} for this maat-id {}", calculatedOutcome, prosecutionConcluded.getMaatId());

                ConcludedDTO concludedDTO = ConcludedDTO.
                        builder()
                        .prosecutionConcluded(prosecutionConcluded)
                        .calculatedOutcome(calculatedOutcome)
                        .ouCourtLocation(wqHearingEntity.getOuCourtLocation())
                        .wqJurisdictionType(wqHearingEntity.getWqJurisdictionType())
                        .caseEndDate(getMostRecentCaseEndDate(prosecutionConcluded.getOffenceSummary()))
                        .caseUrn(wqHearingEntity.getCaseUrn())
                        .hearingResultCodeList(buildResultCodeList(wqHearingEntity))
                        .build();

                prosecutionConcludedImpl.execute(concludedDTO);
            }
        }
    }

    private WQHearingEntity getWqHearingEntity(ProsecutionConcluded prosecutionConcluded) {
        Optional<WQHearingEntity> wqHearingEntity = wqHearingRepository
                .findByMaatIdAndHearingUUID(prosecutionConcluded.getMaatId(), prosecutionConcluded.getHearingIdWhereChangeOccurred().toString());
        return wqHearingEntity.orElse(null);
    }

    private String getMostRecentCaseEndDate(List<OffenceSummary> offenceSummaryList) {

        if (offenceSummaryList == null || offenceSummaryList.isEmpty())
            return null;

        return offenceSummaryList.stream()
                .map(offenceSummary -> LocalDate.parse(offenceSummary.getProceedingsConcludedChangedDate()))
                .distinct()
                .collect(Collectors.toList())
                .stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList()).get(0).toString();
    }

    private List<String> buildResultCodeList(WQHearingEntity wqHearingEntity) {
        return Arrays.stream(wqHearingEntity.getResultCodes().split(","))
                .distinct()
                .collect(Collectors.toList());
    }

    private void publishMessageToProsecutionSQS(ProsecutionConcluded prosecutionConcluded) {

        log.info("Message retry attempt no. " + prosecutionConcluded.getMessageRetryCounter());

        if (prosecutionConcluded.getMessageRetryCounter() < 6) {
            log.info("Publishing a message to the SQS again, with retry number {}", prosecutionConcluded.getMessageRetryCounter());

            int counter = prosecutionConcluded.getMessageRetryCounter()+1;
            prosecutionConcluded.setMessageRetryCounter(counter);
            String toJson = gson.toJson(prosecutionConcluded);

            awsStandardSqsPublisher.publish(sqsQueueName,toJson);
        } else {
            throw new MaatRecordLockedException("Unable to process CP hearing notification because Maat Record is locked.");
        }
    }
}
