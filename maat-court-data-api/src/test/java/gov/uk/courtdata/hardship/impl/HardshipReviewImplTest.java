package gov.uk.courtdata.hardship.impl;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.HardshipReviewDTO;
import gov.uk.courtdata.entity.HardshipReviewDetailReasonEntity;
import gov.uk.courtdata.entity.HardshipReviewEntity;
import gov.uk.courtdata.enums.HardshipReviewDetailType;
import gov.uk.courtdata.hardship.mapper.HardshipReviewMapper;
import gov.uk.courtdata.model.NewWorkReason;
import gov.uk.courtdata.model.hardship.HardshipReviewDetail;
import gov.uk.courtdata.model.hardship.HardshipReviewProgress;
import gov.uk.courtdata.repository.HardshipReviewDetailReasonRepository;
import gov.uk.courtdata.repository.HardshipReviewRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HardshipReviewImplTest {

    @InjectMocks
    private HardshipReviewImpl hardshipReviewImpl;

    @Mock
    private HardshipReviewMapper hardshipReviewMapper;

    @Mock
    private HardshipReviewRepository hardshipReviewRepository;

    @Mock
    private HardshipReviewDetailReasonRepository hardshipReviewDetailReasonRepository;

    @Captor
    private ArgumentCaptor<HardshipReviewEntity> hardshipReviewEntityArgumentCaptor;

    @Test
    public void givenExistingHardshipId_whenFindIsInvoked_thenHardshipIsRetrieved() {
        HardshipReviewEntity mockHardship = HardshipReviewEntity.builder().id(1000).build();
        when(hardshipReviewRepository.getById(any(Integer.class))).thenReturn(
                mockHardship
        );
        assertThat(hardshipReviewImpl.find(1000)).isEqualTo(mockHardship);
    }

    @Test
    public void givenHardshipDTO_whenCreateIsInvoked_thenHardshipIsPersisted() {
        HardshipReviewDTO hardshipReviewDTO = TestModelDataBuilder.getHardshipReviewDTOWithRelationships();
        when(hardshipReviewMapper.HardshipReviewDTOToHardshipReviewEntity(any(HardshipReviewDTO.class))).thenReturn(
                TestEntityDataBuilder.getHardshipReviewEntityWithRelationships()
        );
        when(hardshipReviewDetailReasonRepository.getById(any(Integer.class))).thenReturn(
                HardshipReviewDetailReasonEntity.builder()
                        .id(1000)
                        .accepted("Y")
                        .dateCreated(LocalDateTime.now())
                        .userCreated("test-s")
                        .detailType(HardshipReviewDetailType.INCOME)
                        .build()
        );
        hardshipReviewImpl.create(hardshipReviewDTO);
        verify(hardshipReviewRepository).saveAndFlush(hardshipReviewEntityArgumentCaptor.capture());
        assertThat(hardshipReviewEntityArgumentCaptor.getValue().getId()).isEqualTo(hardshipReviewDTO.getId());
    }

    @Test
    public void givenHardshipDTO_whenUpdateIsInvoked_thenHardshipIsUpdated() {
        HardshipReviewDTO hardshipReviewDTO = TestModelDataBuilder.getHardshipReviewDTOWithRelationships();
        HardshipReviewEntity mockHardshipEntity = TestEntityDataBuilder.getHardshipReviewEntityWithRelationships();
        when(hardshipReviewRepository.getById(any(Integer.class))).thenReturn(
                mockHardshipEntity
        );
        when(hardshipReviewMapper.HardshipReviewDetailToHardshipReviewDetailEntity(any(HardshipReviewDetail.class))).thenReturn(
                TestEntityDataBuilder.getHardshipReviewDetailsEntity()
        );
        when(hardshipReviewMapper.HardshipReviewProgressToHardshipReviewProgressEntity(any(HardshipReviewProgress.class))).thenReturn(
                TestEntityDataBuilder.getHardshipReviewProgressEntity()
        );
        when(hardshipReviewMapper.NewWorkReasonToNewWorkReasonEntity(any(NewWorkReason.class))).thenReturn(
                TestEntityDataBuilder.getNewWorkReasonEntity()
        );

        hardshipReviewImpl.update(hardshipReviewDTO);

        verify(hardshipReviewRepository).saveAndFlush(hardshipReviewEntityArgumentCaptor.capture());
        assertThat(hardshipReviewEntityArgumentCaptor.getValue().getId()).isEqualTo(hardshipReviewDTO.getId());
        assertThat(hardshipReviewEntityArgumentCaptor.getValue().getCmuId()).isEqualTo(hardshipReviewDTO.getCmuId());
        assertThat(hardshipReviewEntityArgumentCaptor.getValue().getReviewResult()).isEqualTo(hardshipReviewDTO.getReviewResult());
        assertThat(hardshipReviewEntityArgumentCaptor.getValue().getResultDate()).isEqualTo(hardshipReviewDTO.getResultDate());
        assertThat(hardshipReviewEntityArgumentCaptor.getValue().getNotes()).isEqualTo(hardshipReviewDTO.getNotes());
        assertThat(hardshipReviewEntityArgumentCaptor.getValue().getDecisionNotes()).isEqualTo(hardshipReviewDTO.getDecisionNotes());
        assertThat(hardshipReviewEntityArgumentCaptor.getValue().getSolicitorRate()).isEqualTo(hardshipReviewDTO.getSolicitorCosts().getSolicitorRate());
        assertThat(hardshipReviewEntityArgumentCaptor.getValue().getSolicitorHours()).isEqualTo(hardshipReviewDTO.getSolicitorCosts().getSolicitorHours());
        assertThat(hardshipReviewEntityArgumentCaptor.getValue().getSolicitorVat()).isEqualTo(hardshipReviewDTO.getSolicitorCosts().getSolicitorVat());
        assertThat(hardshipReviewEntityArgumentCaptor.getValue().getSolicitorDisb()).isEqualTo(hardshipReviewDTO.getSolicitorCosts().getSolicitorDisb());
        assertThat(hardshipReviewEntityArgumentCaptor.getValue().getSolicitorEstTotalCost()).isEqualTo(hardshipReviewDTO.getSolicitorCosts().getSolicitorEstTotalCost());
        assertThat(hardshipReviewEntityArgumentCaptor.getValue().getDisposableIncome()).isEqualTo(hardshipReviewDTO.getDisposableIncome());
        assertThat(hardshipReviewEntityArgumentCaptor.getValue().getDisposableIncomeAfterHardship()).isEqualTo(hardshipReviewDTO.getDisposableIncomeAfterHardship());
        assertThat(hardshipReviewEntityArgumentCaptor.getValue().getStatus()).isEqualTo(hardshipReviewDTO.getStatus());
        assertThat(hardshipReviewEntityArgumentCaptor.getValue().getUserModified()).isEqualTo(hardshipReviewDTO.getUserModified());
        assertThat(hardshipReviewEntityArgumentCaptor.getValue().getNewWorkReason().getCode()).isEqualTo(hardshipReviewDTO.getNewWorkReason().getCode());
    }
}
