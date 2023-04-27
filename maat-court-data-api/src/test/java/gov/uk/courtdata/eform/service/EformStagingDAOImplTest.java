package gov.uk.courtdata.eform.service;

import gov.uk.courtdata.eform.dto.EformStagingDTO;
import gov.uk.courtdata.eform.mapper.EformStagingDTOMapper;
import gov.uk.courtdata.eform.repository.EformStagingRepository;
import gov.uk.courtdata.eform.repository.entity.EformsStagingEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@WebMvcTest
class EformStagingDAOImplTest {

    private static final int USN = 1233;
    private static final EformsStagingEntity EFORMS_STAGING_ENTITY = EformsStagingEntity
            .builder()
            .usn(USN)
            .build();
    private static final EformStagingDTO EFORM_STAGING_DTO = EformStagingDTO
            .builder()
            .usn(USN)
            .build();

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
    void givenUSN_whenServiceIncolved_thenSaveToDatabase() {

        eformStagingDAOImpl.create(EFORM_STAGING_DTO);

        Mockito.verify(eformStagingRepository, Mockito.times(1)).saveAndFlush(EFORMS_STAGING_ENTITY);
    }

    @Test
    void givenUSN_whenServiceIncolved_thenUpdateTheDatabase() {

        eformStagingDAOImpl.update(EFORM_STAGING_DTO);

        Mockito.verify(eformStagingRepository, Mockito.times(1)).saveAndFlush(EFORMS_STAGING_ENTITY);
    }

    @Test
    void givenUSN_whenServiceInvocated_thenPullFromTheDatabase() {
        Mockito.when(eformStagingRepository.findById(USN))
                .thenReturn(Optional.of(EFORMS_STAGING_ENTITY));

        EformStagingDTO retrieve = eformStagingDAOImpl.retrieve(EFORM_STAGING_DTO.getUsn());

        Assertions.assertEquals(EFORM_STAGING_DTO, retrieve);
    }

    @Test
    void givenUSN_whenServiceIncolved_thenDeletefromDatabase() {

        Integer usn = EFORM_STAGING_DTO.getUsn();
        eformStagingDAOImpl.delete(usn);

        Mockito.verify(eformStagingRepository, Mockito.times(1)).deleteById(usn);
    }
}