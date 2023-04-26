package gov.uk.courtdata.eform.service;

import gov.uk.courtdata.eform.dto.EformStagingDTO;
import gov.uk.courtdata.eform.mapper.EformStagingDTOMapper;
import gov.uk.courtdata.eform.repository.EformStagingRepository;
import gov.uk.courtdata.eform.repository.entity.EformsStagingEntity;
import gov.uk.courtdata.eform.validator.UsnValidator;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@ExtendWith(MockitoExtension.class)
@WebMvcTest
class EformStagingDAOImplTest {

    @Mock
    private EformStagingRepository eformStagingRepository;

    @Mock
    private EformStagingDTOMapper eformStagingDTOMapper;

    private EformStagingDAOImpl eformStagingDAOImpl;

    @BeforeEach
    void setUp() {
        eformStagingDAOImpl = new EformStagingDAOImpl(eformStagingRepository,
                eformStagingDTOMapper);
    }

    @Test
    public void givenUSN_whenServiceIncolved_thenSaveToDatabase() {

    }

    @Test
    public void givenUSN_whenServiceIncolved_thenUpdateTheDatabase() {

    }

    @Test
    public void givenUSN_whenServiceIncolved_thenPullFromTheDatabase() {

    }

    @Test
    public void givenUSN_whenServiceIncolved_thenDeletefromDatabase() {

    }


    private EformStagingDTO getEformsStagingDTO() {

        return EformStagingDTO
                .builder()
                .usn(1233)
                .build();
    }

    private EformsStagingEntity getEformsStagingEntity(){

        return EformsStagingEntity
                .builder()
                .usn(1233)
                .build();
    }
}