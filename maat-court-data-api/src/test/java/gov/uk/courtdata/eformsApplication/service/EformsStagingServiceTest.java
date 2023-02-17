package gov.uk.courtdata.eformsApplication.service;

import gov.uk.courtdata.eformsApplication.dto.EformsStagingDTO;
import gov.uk.courtdata.eformsApplication.mapper.EformsStagingDTOMapper;
import gov.uk.courtdata.entity.EformsStagingEntity;

import gov.uk.courtdata.repository.EformsStagingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EformsStagingServiceTest {

    @InjectMocks
    private EformsStagingService eformsStagingService;


    @Mock
    private EformsStagingDTOMapper eformsStagingDTOMapper;

    @Spy
    private EformsStagingRepository eformsStagingRepositorySpy;
    @Captor
    private ArgumentCaptor<EformsStagingEntity> eformsStagingEntityArgumentCaptor;


    @Test
    public void givenEformsDetail_whenServiceIncolved_thenSaveEformsInfoToDatabase() {

        when(eformsStagingDTOMapper.toEformsStagingEntity(getEformsStagingDTO())).thenReturn(getEformsStagingEntity());

        eformsStagingService.execute(getEformsStagingDTO());

        verify(eformsStagingRepositorySpy).save(eformsStagingEntityArgumentCaptor.capture());
        assertThat(eformsStagingEntityArgumentCaptor.getValue().getUsn()).isEqualTo(1233);
        assertThat(eformsStagingEntityArgumentCaptor.getValue().getMaatStatus()).isEqualTo("mys status type");
        assertThat(eformsStagingEntityArgumentCaptor.getValue().getUserCreated()).isEqualTo("AM");
    }

//    @Test
//    public void givenEformsStaging_when() {
//
//        //given
//        when(eformsStagingDTOMapper.toEformsStagingEntity(getEformsStagingDTO())).thenReturn(getEformsStagingEntity());
//
//        //when
//        eformsStagingService.execute(getEformsStagingDTO());
//
//        //then
//        verify(eformsStagingRepository,atLeast(1)).save(any());
//    }


    private EformsStagingDTO getEformsStagingDTO() {

        return EformsStagingDTO
                .builder()
                .usn(1233)
                .maatStatus("mys status type")
                .userCreated("AM")
                .build();
    }

    private EformsStagingEntity getEformsStagingEntity(){

        return EformsStagingEntity
                .builder()
                .usn(1233)
                .maatStatus("mys status type")
                .userCreated("AM")
                .build();
    }
}