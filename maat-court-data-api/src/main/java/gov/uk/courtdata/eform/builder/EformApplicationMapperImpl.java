package gov.uk.courtdata.eform.builder;

import gov.uk.courtdata.eform.dto.EformStagingDTO;
import gov.uk.courtdata.eform.model.EformApplication;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
@Slf4j
public class EformApplicationMapperImpl implements EformApplicationMapper {

    private static final String APPLICATION_TYPE = "CRM14";

    @Override
    public EformStagingDTO map(EformApplication eformApplication) {
        return EformStagingDTO
                .builder()
                .usn(eformApplication.getReference())
                .type(APPLICATION_TYPE)
                .build();
    }


    private LocalDateTime toLocalDateTime(LocalDate date) {
        return date == null ? null:date.atStartOfDay();
    }
}
