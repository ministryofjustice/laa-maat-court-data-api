package gov.uk.courtdata.prosecutionconcluded.helper;


import gov.uk.courtdata.entity.CrownCourtCode;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.repository.CrownCourtCodeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Optional;
import static java.lang.String.format;

@Slf4j
@Component
@RequiredArgsConstructor
public class CrownCourtCodeHelper {

    private final CrownCourtCodeRepository crownCourtCodeRepository;

    public String get(String ouCode) {
        log.info("Getting Crown Court Code");
        Optional<CrownCourtCode> optCrownCourtCode = crownCourtCodeRepository.findByOuCode(ouCode);
        CrownCourtCode crownCourtCode = optCrownCourtCode.orElseThrow(()
                -> new MAATCourtDataException(format("Crown Court Code Look Up Failed for %s", ouCode)));
        return crownCourtCode.getCode();
    }
}