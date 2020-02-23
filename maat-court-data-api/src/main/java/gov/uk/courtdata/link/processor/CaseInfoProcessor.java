package gov.uk.courtdata.link.processor;

import gov.uk.courtdata.entity.CaseEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.SaveAndLinkModel;
import gov.uk.courtdata.repository.CaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static gov.uk.courtdata.constants.CourtDataConstants.*;


@Component
@RequiredArgsConstructor
public class CaseInfoProcessor implements Process {

    private final CaseRepository caseRepository;

    @Override
    public void process(SaveAndLinkModel saveAndLinkModel) {

        CaseDetails caseDetails = saveAndLinkModel.getCaseDetails();
        final CaseEntity caseEntity = CaseEntity.builder()
                .txId(saveAndLinkModel.getTxId())
                .caseId(saveAndLinkModel.getCaseId())
                .asn(caseDetails.getAsn())
                .cjsAreaCode(caseDetails.getCjsAreaCode())
                .inactive(caseDetails.isActive() ? NO : YES)
                .libraCreationDate(LocalDate.parse(caseDetails.getCaseCreationDate()))
                .docLanguage(caseDetails.getDocLanguage())
                .proceedingId(saveAndLinkModel.getProceedingId())
                .build();

        caseRepository.save(caseEntity);
    }
}
