package gov.uk.courtdata.hearing.processor;

import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LinkRegisterProcessor {

    private final WqLinkRegisterRepository wqLinkRegisterRepository;

    public void process(HearingDTO hearingDTO) {

        WqLinkRegisterEntity wqLinkRegisterEntity = wqLinkRegisterRepository
                .findBymaatId(hearingDTO.getMaatId())
                .stream()
                .findFirst()
                .orElse(null);

        if (wqLinkRegisterEntity != null) {
            wqLinkRegisterEntity.setProsecutionConcluded(String.valueOf(hearingDTO.isProsecutionConcluded()));
            wqLinkRegisterRepository.save(wqLinkRegisterEntity);
            log.info("Saved plea successfully");
        } else {
            throw new MAATCourtDataException("MAAT ID not found.");
        }
    }
}