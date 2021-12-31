package gov.uk.courtdata.iojAppeal.impl;

import gov.uk.courtdata.entity.IOJAppealEntity;
import gov.uk.courtdata.repository.IOJAppealRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static gov.uk.courtdata.builder.TestModelDataBuilder.IOJ_APPEAL_ID;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class IOJAppealImplTest {

    @Spy
    @InjectMocks
    private IOJAppealImpl iojAppealImpl;

    @Spy
    private IOJAppealRepository iojAppealRepository;

    @Test
    public void whenFindIsInvoked_thenAssessmentIsRetrieved() {
        when(iojAppealRepository.getById(any())).thenReturn(IOJAppealEntity.builder().id(IOJ_APPEAL_ID).build());
        var iojAppeal = iojAppealImpl.find(IOJ_APPEAL_ID);
        assertEquals(iojAppeal.getId(), IOJ_APPEAL_ID);
    }
}