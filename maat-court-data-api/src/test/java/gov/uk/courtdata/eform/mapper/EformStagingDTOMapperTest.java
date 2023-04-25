package gov.uk.courtdata.eform.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@WebMvcTest
class EformStagingDTOMapperTest {

    @Autowired
    private EformStagingDTOMapper mapper;

    @Test
    void toEformsStagingEntity() {
    }

    @Test
    void toEformsStagingDTO() {
    }

    @Test
    void toEformStagingResponse() {
    }
}