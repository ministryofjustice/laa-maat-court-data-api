package gov.uk.courtdata.link.processor;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.CaseEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.CaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static gov.uk.courtdata.constants.CourtDataConstants.NO;
import static gov.uk.courtdata.constants.CourtDataConstants.YES;


@Component
@RequiredArgsConstructor
public class CaseInfoProcessor implements Process {

    private final CaseRepository caseRepository;

    @Override
    public void process(CourtDataDTO courtDataDTO) {

        CaseDetails caseDetails = courtDataDTO.getCaseDetails();
        final CaseEntity caseEntity = CaseEntity.builder()
                .txId(courtDataDTO.getTxId())
                .caseId(courtDataDTO.getCaseId())
                .asn(caseDetails.getAsn())
                .cjsAreaCode(caseDetails.getCjsAreaCode())
                .inactive(caseDetails.isActive() ? NO : YES)
                .libraCreationDate(LocalDate.parse(caseDetails.getCaseCreationDate()))
                .docLanguage(caseDetails.getDocLanguage())
                .proceedingId(courtDataDTO.getProceedingId())
                .build();

        caseRepository.save(caseEntity);
    }
}
