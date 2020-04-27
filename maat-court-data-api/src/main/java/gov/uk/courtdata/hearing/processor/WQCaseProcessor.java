package gov.uk.courtdata.hearing.processor;

import gov.uk.courtdata.entity.WQCase;
import gov.uk.courtdata.hearing.magistrate.dto.MagistrateCourtDTO;
import gov.uk.courtdata.repository.WQCaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class WQCaseProcessor {

    private final WQCaseRepository wqCaseRepository;

    public void process(final MagistrateCourtDTO magsCourtDTO) {


        WQCase wqCase = WQCase.builder().caseId(magsCourtDTO.getCaseId())
                .txId(magsCourtDTO.getTxId())
                .asn(magsCourtDTO.getAsn())
                .docLanguage(magsCourtDTO.getDocLanguage())
                .inactive(magsCourtDTO.getIsActive())
                .libraCreationDate(LocalDate.now())
                .cjsAreaCode(magsCourtDTO.getCjsAreaCode())
                .proceedingId(magsCourtDTO.getProceedingId())
                .build();
        wqCaseRepository.save(wqCase);
    }


}
