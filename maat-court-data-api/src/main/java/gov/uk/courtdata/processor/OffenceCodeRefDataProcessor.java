package gov.uk.courtdata.processor;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.entity.XLATOffenceEntity;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.repository.XLATOffenceRepository;
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
public class OffenceCodeRefDataProcessor {

    private final XLATOffenceRepository xlatOffenceRepository;


    /**
     * if the Offence Code is not available on XXMLA_XLAT_OFFENCE then
     * Add the Offence code there.
     */
    public void processOffenceCode(final String offenceCode) {

        if (offenceCode != null) {
            Optional<XLATOffenceEntity> xlatOffence =
                    xlatOffenceRepository.findById(offenceCode);
            if (xlatOffence.isEmpty()) {
                createNewXLATOffence(offenceCode);
                log.info("A New Offence Code : " + offenceCode + " has been added to the Ref Data");
            }
        } else {
            throw new MAATCourtDataException("A Null Offence Code is passed in");
        }
    }


    private void createNewXLATOffence(final String offenceCode) {

        XLATOffenceEntity xlatOffenceEntity = XLATOffenceEntity.builder()
                .offenceCode(offenceCode)
                .parentCode(offenceCode.length() >= 4 ? offenceCode.substring(0, 4) : offenceCode)
                .codeMeaning(UNKNOWN_OFFENCE)
                .applicationFlag(G_NO)
                .codeStart(LocalDate.now())
                .createdUser(AUTO_USER)
                .createdDate(LocalDate.now())
                .build();


        xlatOffenceRepository.save(xlatOffenceEntity);


    }


}
