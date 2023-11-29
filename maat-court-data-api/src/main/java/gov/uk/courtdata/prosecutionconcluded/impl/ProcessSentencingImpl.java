package gov.uk.courtdata.prosecutionconcluded.impl;

import gov.uk.courtdata.repository.CrownCourtProcessingRepository;
import gov.uk.courtdata.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static gov.uk.courtdata.enums.CrownCourtCaseType.APPEAL_CC;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProcessSentencingImpl {

    private final CrownCourtProcessingRepository crownCourtProcessingRepository;

    @Value("${spring.datasource.username}")
    private String dbUser;

    public void processSentencingDate(String ccCaseEndDate, Integer maatId, String catyType) {

        log.info("Processing sentencing date");
        LocalDate caseEndDate = DateUtil.parse(ccCaseEndDate);
        if (caseEndDate != null) {
            String user = dbUser != null ? dbUser.toUpperCase() : null;
            if (APPEAL_CC.getValue().equalsIgnoreCase(catyType)) {
                crownCourtProcessingRepository
                        .invokeUpdateAppealSentenceOrderDate(maatId,
                                user,
                                caseEndDate,
                                LocalDate.now()
                        );
            } else {
                crownCourtProcessingRepository
                        .invokeUpdateSentenceOrderDate(maatId,
                                user,
                                caseEndDate
                        );
            }
        }
    }
}