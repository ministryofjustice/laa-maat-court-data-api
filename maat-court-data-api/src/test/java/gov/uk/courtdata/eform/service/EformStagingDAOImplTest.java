package gov.uk.courtdata.eform.service;

import gov.uk.courtdata.eform.dto.EformStagingDTO;
import gov.uk.courtdata.eform.mapper.EformStagingDTOMapper;
import gov.uk.courtdata.entity.EformsStagingEntity;

import gov.uk.courtdata.eform.repository.EformStagingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EformStagingDAOImplTest {

    @InjectMocks
    private EformStagingDAOImpl eformStagingDAOImpl;


    @Mock
    private EformStagingDTOMapper eformStagingDTOMapper;

    @Spy
    private EformStagingRepository eformStagingRepositorySpy;
    @Captor
    private ArgumentCaptor<EformsStagingEntity> eformsStagingEntityArgumentCaptor;

    // TODO Complete these tests

    @Test
    public void givenEformsDetail_whenServiceIncolved_thenSaveEformsInfoToDatabase() {

        when(eformStagingDTOMapper.toEformsStagingEntity(getEformsStagingDTO())).thenReturn(getEformsStagingEntity());

        eformStagingDAOImpl.create(getEformsStagingDTO());

        verify(eformStagingRepositorySpy).save(eformsStagingEntityArgumentCaptor.capture());
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


    private EformStagingDTO getEformsStagingDTO() {

        return EformStagingDTO
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