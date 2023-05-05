package gov.uk.courtdata.eform.service;

import gov.uk.courtdata.eform.dto.EformStagingDTO;
import gov.uk.courtdata.eform.mapper.EformStagingDTOMapper;
import gov.uk.courtdata.eform.model.EformStagingResponse;
import gov.uk.courtdata.eform.repository.EformStagingRepository;
import gov.uk.courtdata.eform.repository.entity.EformsStagingEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(EformStagingService.class)
class EformStagingServiceTest {

    private static final int USN = 1233;
    private static final int NEW_USN = 3321;
    private static final String TYPE = "CRM14";
    private static final int MAAT_ID = 3290392;

    private static final EformsStagingEntity EFORMS_STAGING_ENTITY = EformsStagingEntity
            .builder()
            .usn(USN)
            .type(TYPE)
            .maatRef(MAAT_ID)
            .build();

    private static final EformsStagingEntity NEW_EFORMS_STAGING_ENTITY = EformsStagingEntity
            .builder()
            .usn(NEW_USN)
            .type(TYPE)
            .maatRef(MAAT_ID)
            .build();

    private static final EformStagingDTO EFORM_STAGING_DTO = EformStagingDTO
            .builder()
            .usn(USN)
            .type(TYPE)
            .maatRef(MAAT_ID)
            .build();

    private static final EformStagingDTO NEW_EFORM_STAGING_DTO = EformStagingDTO
            .builder()
            .usn(NEW_USN)
            .type(TYPE)
            .build();

    private static final EformStagingResponse EFORM_STAGING_RESPONSE = EformStagingResponse
            .builder()
            .usn(USN)
            .type(TYPE)
            .maatRef(MAAT_ID)
            .build();

    @MockBean
    private EformStagingRepository mockEformStagingRepository;

    @MockBean
    private EformStagingDTOMapper mockEformStagingDTOMapper;

    private EformStagingService eformStagingService;

    @BeforeEach
    void setUp() {
        eformStagingService = new EformStagingService(mockEformStagingRepository,
                mockEformStagingDTOMapper);

        when(mockEformStagingDTOMapper.toEformsStagingEntity(EFORM_STAGING_DTO))
                .thenReturn(EFORMS_STAGING_ENTITY);
        when(mockEformStagingDTOMapper.toEformsStagingEntity(NEW_EFORM_STAGING_DTO))
                .thenReturn(NEW_EFORMS_STAGING_ENTITY);
        when(mockEformStagingDTOMapper.toEformStagingDTO(any(EformsStagingEntity.class)))
                .thenReturn(EFORM_STAGING_DTO);
        when(mockEformStagingDTOMapper.toEformStagingResponse(any(EformStagingDTO.class)))
                .thenReturn(EFORM_STAGING_RESPONSE);
    }

    @Test
    void givenUSN_whenServiceInvoked_thenSaveToDatabase() {

        eformStagingService.create(EFORM_STAGING_DTO);

        Mockito.verify(mockEformStagingRepository, Mockito.times(1)).saveAndFlush(EFORMS_STAGING_ENTITY);
    }

    @Test
    void givenUSN_whenServiceInvoked_thenPullFromTheDatabase() {
        Mockito.when(mockEformStagingRepository.findById(USN))
                .thenReturn(Optional.of(EFORMS_STAGING_ENTITY));

        EformStagingDTO retrieve = eformStagingService.retrieve(EFORM_STAGING_DTO.getUsn());

        Assertions.assertEquals(EFORM_STAGING_DTO, retrieve);
    }

    @Test
    void givenUSN_whenServiceInvoked_thenDeleteFromDatabase() {

        Integer usn = EFORM_STAGING_DTO.getUsn();
        eformStagingService.delete(usn);

        Mockito.verify(mockEformStagingRepository, Mockito.times(1)).deleteById(usn);
    }
}