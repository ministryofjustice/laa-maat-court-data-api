package gov.uk.courtdata.eform.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import gov.uk.courtdata.eform.dto.EformStagingDTO;
import gov.uk.courtdata.eform.exception.UsnException;
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
import org.springframework.http.HttpStatus;

import java.util.Optional;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(EformStagingService.class)
class EformStagingServiceTest {

    private static final int USN = 7000001;
    private static final String TYPE = "CRM14";
    private static final String CA_USER = "causer";
    private static final int MAAT_ID = 3290392;
    private static final String USER_CREATED = "MLA";
    private static final EformStagingResponse EFORM_STAGING_RESPONSE = EformStagingResponse
            .builder()
            .usn(USN)
            .type(TYPE)
            .maatRef(MAAT_ID)
            .build();

    @MockitoBean
    private EformStagingRepository mockEformStagingRepository;

    @MockitoBean
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

        EformStagingDTO eformStagingDTO = eformStagingService.createOrRetrieve(USN, CA_USER);

        assertEquals(this.eformStagingDTO, eformStagingDTO);
    }

    @Test
    void givenUsnNotInEformStaging_whenCreateOrRetrieveServiceIsInvoked_then_insertUsnInEformStagingAndBuildEformStagingDtoWithUsnAndRetrun() {
        when(mockEformStagingRepository.existsById(any()))
                .thenReturn(false);
        EformStagingDTO expectedDto = EformStagingDTO.builder().usn(USN).userCreated(CA_USER).build();
        EformsStagingEntity entity = EformsStagingEntity.builder().usn(USN).build();
        when(mockEformStagingDTOMapper.toEformsStagingEntity(expectedDto))
                .thenReturn(entity);

        EformStagingDTO eformStagingDTO = eformStagingService.createOrRetrieve(USN, CA_USER);

        assertEquals(expectedDto, eformStagingDTO);
        Mockito.verify(mockEformStagingRepository, Mockito.times(1)).saveAndFlush(entity);
    }

    @Test
    void shouldUpdateEformsStagingRecordForGivenUSN() throws JsonProcessingException {
        EformsStagingEntity eformsStaging = EformsStagingEntity.builder().maatStatus("PURGE").build();
        when(mockEformStagingRepository.findById(USN)).thenReturn(Optional.ofNullable(eformsStagingEntity));
        eformStagingService.updateEformStagingFields(USN, eformsStaging);
        verify(mockEformStagingRepository, times(1)).findById(USN);
        verify(mockEformStagingRepository, times(1)).save(eformsStagingEntity);
        assertEquals(eformsStagingEntity.getMaatStatus(), "PURGE");
    }

    @Test
    void shouldReturnDataNotFoundIfUsnNotThereInEformsDecisionHistoryTable() throws JsonProcessingException {
        EformsStagingEntity eformsStaging = EformsStagingEntity.builder().maatStatus("PURGE").build();
        when(mockEformStagingRepository.findById(USN)).thenReturn(Optional.empty());
        UsnException exception = assertThrows(UsnException.class, () -> {
            eformStagingService.updateEformStagingFields(USN, eformsStaging);
        });
        verify(mockEformStagingRepository, times(1)).findById(USN);
        verify(mockEformStagingRepository, times(0)).save(eformsStagingEntity);
        assertEquals("The USN [7000001] does not exist in the data store.", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpResponseCode());
    }
}