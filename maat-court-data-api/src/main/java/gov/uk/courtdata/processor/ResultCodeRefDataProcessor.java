package gov.uk.courtdata.processor;


import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.entity.XLATResultEntity;
import gov.uk.courtdata.enums.WQType;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.repository.XLATResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

import static gov.uk.courtdata.constants.CourtDataConstants.*;

@Component
@Slf4j
@XRayEnabled
@RequiredArgsConstructor
public class ResultCodeRefDataProcessor {

    private final XLATResultRepository xlatResultRepository;

    /**
     * if the Result Code is not available on XXMLA_XLAT_RESULT then
     * Add the result code there.
     *
     * @param resultCode
     * @return
     */
    public void processResultCode(final Integer resultCode) {

        if (resultCode != null) {
            Optional<XLATResultEntity> xlatResult =
                    xlatResultRepository.findById(resultCode);
            if (xlatResult.isEmpty()) {
                createNewXLATResult(resultCode);
                log.info("A New CJS Result Code : " + resultCode + " has been added to the Ref Data");
            }
        } else {
            throw new MAATCourtDataException("A Null Result Code is passed in");
        }
    }


    private void createNewXLATResult(final Integer resultCode) {

        XLATResultEntity xlatResultEntity = XLATResultEntity.builder()
                .cjsResultCode(resultCode)
                .resultDescription(RESULT_CODE_DESCRIPTION)
                .englandAndWales(YES)
                .notes(buildNotesContent(resultCode))
                .wqType(WQType.USER_INTERVENTIONS_QUEUE.value())
                .createdUser(AUTO_USER)
                .createdDate(LocalDate.now())
                .build();

        xlatResultRepository.save(xlatResultEntity);


    }

    /**
     * Build notes description for the result.
     *
     * @param resultCode
     * @return
     */
    private String buildNotesContent(Integer resultCode) {
        return String.format("New Result code %s  has been received " +
                "and automatically added to the Intervention queue. Please contact support.'", resultCode);
    }

}
