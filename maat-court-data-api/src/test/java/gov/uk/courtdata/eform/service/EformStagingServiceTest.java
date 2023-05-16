package gov.uk.courtdata.eform.service;

import gov.uk.courtdata.eform.dto.EformStagingDTO;
import gov.uk.courtdata.eform.mapper.EformStagingDTOMapper;
import gov.uk.courtdata.eform.model.EformStagingResponse;
import gov.uk.courtdata.eform.repository.EformStagingRepository;
import gov.uk.courtdata.eform.repository.entity.EformsStagingEntity;
import gov.uk.courtdata.testutils.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(EformStagingService.class)
class EformStagingServiceTest {

    private static final int USN = 7000001;
    private static final String TYPE = "CRM14";
    private static final int MAAT_ID = 3290392;
    private static final String USER_CREATED = "MLA";
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
    private EformsStagingEntity eformsStagingEntity;
    private EformStagingDTO eformStagingDTO;

    @BeforeEach
    void setUp() {
        String xmlDoc = FileUtils.readResourceToString("eform/request/xmlDoc_default.xml");

        eformsStagingEntity = EformsStagingEntity
                .builder()
                .usn(USN)
                .type(TYPE)
                .maatRef(MAAT_ID)
                .xmlDoc(xmlDoc)
                .userCreated(USER_CREATED)
                .build();

        eformStagingDTO = EformStagingDTO
                .builder()
                .usn(USN)
                .type(TYPE)
                .maatRef(MAAT_ID)
                .xmlDoc(xmlDoc)
                .userCreated(USER_CREATED)
                .build();

        eformStagingService = new EformStagingService(mockEformStagingRepository,
                mockEformStagingDTOMapper);

        when(mockEformStagingDTOMapper.toEformsStagingEntity(eformStagingDTO))
                .thenReturn(eformsStagingEntity);
        when(mockEformStagingDTOMapper.toEformStagingDTO(any(EformsStagingEntity.class)))
                .thenReturn(eformStagingDTO);
        when(mockEformStagingDTOMapper.toEformStagingResponse(any(EformStagingDTO.class)))
                .thenReturn(EFORM_STAGING_RESPONSE);
    }

    @Test
    void givenUSN_whenServiceInvoked_thenSaveToDatabase() {
        eformStagingService.create(eformStagingDTO);

        Mockito.verify(mockEformStagingRepository, Mockito.times(1)).saveAndFlush(eformsStagingEntity);
    }

    @Test
    void givenUSN_whenServiceInvoked_thenPullFromTheDatabase() {
        Mockito.when(mockEformStagingRepository.findById(USN))
                .thenReturn(Optional.of(eformsStagingEntity));

        EformStagingDTO eformStagingDTO = eformStagingService.retrieve(this.eformStagingDTO.getUsn());

        assertEquals(this.eformStagingDTO, eformStagingDTO);
    }

    @Test
    void givenUSN_whenServiceInvoked_thenDeleteFromDatabase() {
        Integer usn = eformStagingDTO.getUsn();

        eformStagingService.delete(usn);

        Mockito.verify(mockEformStagingRepository, Mockito.times(1)).deleteById(usn);
    }

    @Test
    void givenUsnExistInEformStaging_whenCreateOrRetrieveServiceIsInvoked_then_returnRetrievedDataFromEformStaging() {
        when(mockEformStagingRepository.existsById(any()))
                .thenReturn(true);
        when(mockEformStagingRepository.findById(any()))
                .thenReturn(Optional.of(eformsStagingEntity));

        EformStagingDTO eformStagingDTO = eformStagingService.createOrRetrieve(USN);

        assertEquals(this.eformStagingDTO, eformStagingDTO);
    }

    @Test
    void givenUsnNotInEformStaging_whenCreateOrRetrieveServiceIsInvoked_then_insertUsnInEformStagingAndBuildEformStagingDtoWithUsnAndRetrun() {
        when(mockEformStagingRepository.existsById(any()))
                .thenReturn(false);
        EformStagingDTO expectedDto = EformStagingDTO.builder().usn(USN).build();
        EformsStagingEntity entity = EformsStagingEntity.builder().usn(USN).build();
        when(mockEformStagingDTOMapper.toEformsStagingEntity(expectedDto))
                .thenReturn(entity);

        EformStagingDTO eformStagingDTO = eformStagingService.createOrRetrieve(USN);

        assertEquals(expectedDto, eformStagingDTO);
        Mockito.verify(mockEformStagingRepository, Mockito.times(1)).saveAndFlush(entity);
    }
}