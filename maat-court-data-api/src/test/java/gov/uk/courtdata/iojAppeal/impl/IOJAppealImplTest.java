package gov.uk.courtdata.iojAppeal.impl;

import gov.uk.courtdata.assessment.mapper.FinancialAssessmentMapper;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.FinancialAssessmentDTO;
import gov.uk.courtdata.dto.IOJAppealDTO;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.entity.IOJAppealEntity;
import gov.uk.courtdata.iojAppeal.mapper.IOJAppealMapper;
import gov.uk.courtdata.repository.IOJAppealRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static gov.uk.courtdata.builder.TestModelDataBuilder.IOJ_APPEAL_ID;
import static gov.uk.courtdata.builder.TestModelDataBuilder.IOJ_REP_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class IOJAppealImplTest {

    @Spy
    @InjectMocks
    private IOJAppealImpl iojAppealImpl;

    @Spy
    private IOJAppealRepository iojAppealRepository;

    @Mock
    private IOJAppealMapper iojAppealMapper;

    @Captor
    private ArgumentCaptor<IOJAppealEntity> iojAppealEntityArgumentCaptor;

    @Test
    public void whenFindIsInvoked_thenAssessmentIsRetrieved() {
        when(iojAppealRepository.findById(any())).thenReturn(Optional.of(IOJAppealEntity.builder().id(IOJ_APPEAL_ID).build()));
        var iojAppeal = iojAppealImpl.find(IOJ_APPEAL_ID);
        assertEquals(iojAppeal.getId(), IOJ_APPEAL_ID);
    }

    @Test
    public void whenCreateIsInvoked_thenIOJAppealIsSaved() {

        var iojAppealDTO = TestModelDataBuilder.getIOJAppealDTO();
        when(iojAppealMapper.toIOJIojAppealEntity(any())).thenReturn(TestEntityDataBuilder.getIOJAppealEntity());

        iojAppealImpl.create(iojAppealDTO);

        verify(iojAppealRepository).save(iojAppealEntityArgumentCaptor.capture());

        assertThat(iojAppealEntityArgumentCaptor.getValue().getId()).isEqualTo(iojAppealDTO.getId());
    }

    @Test
    public void whenSetOldIOJAppealReplaced_thenAllIOJAppealRecordsWithGivenREP_IDAreSetToReplaced(){
        iojAppealImpl.setOldIOJAppealReplaced(IOJ_REP_ID, 124);
        verify(iojAppealRepository).setOldIOJAppealsReplaced(IOJ_REP_ID,124);
    }
}