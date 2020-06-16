package gov.uk.courtdata.hearing.impl;

import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HearingResultedImplTest {

    @InjectMocks
    private HearingResultedImpl hearingResultedImpl;

    @Spy
    private WqLinkRegisterRepository wqLinkRegisterRepository;

    @Spy
    private List<WqLinkRegisterEntity> linkRegisterEntities;

    @Spy
    private Iterator<WqLinkRegisterEntity> itr;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenACaseDetailForCrownCourt_whenHearingResultedImplIsInvoked_thenCrownCourtProcessingImplIsInvoked() {
        //given
        HearingResulted laaHearingDetails = HearingResulted.builder().jurisdictionType(JurisdictionType.CROWN).build();

        when(wqLinkRegisterRepository.findBymaatId(Mockito.anyInt())).thenReturn(getWqLinkRegisterEntityList());

        linkRegisterEntities = getWqLinkRegisterEntityList();

        Mockito.when(itr.next()).thenReturn(getWqLinkRegisterEntityList().get(0));
        when(linkRegisterEntities.iterator()).thenReturn(itr);
        //when
        hearingResultedImpl.execute(laaHearingDetails);
        //then
        verify(hearingResultedImpl, times(1)).execute(laaHearingDetails);
    }

    public List<WqLinkRegisterEntity> getWqLinkRegisterEntityList() {
        return Arrays.asList(WqLinkRegisterEntity.builder()
                .caseId(345)
                .createdTxId(123)
                .proceedingId(345)
                .cjsAreaCode("16")
                .maatCat(1)
                .mlrCat(1)
                .maatId(1234)
                .build());
    }
}
