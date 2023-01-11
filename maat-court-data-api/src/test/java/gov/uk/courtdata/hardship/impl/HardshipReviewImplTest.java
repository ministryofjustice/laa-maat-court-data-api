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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HardshipReviewImplTest {

    private static final int MOCK_REP_ID = 5678;
    private static final Integer MOCK_HARDSHIP_ID = 1000;
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
    void givenExistingHardshipId_whenFindIsInvoked_thenHardshipIsRetrieved() {
        HardshipReviewEntity mockHardship = HardshipReviewEntity.builder().id(MOCK_HARDSHIP_ID).build();
        when(hardshipReviewRepository.getReferenceById(any(Integer.class))).thenReturn(mockHardship);
        assertThat(hardshipReviewImpl.find(MOCK_HARDSHIP_ID)).isEqualTo(mockHardship);
    }

    @Test
    void givenExistingRepId_whenFindByRepIdIsInvoked_thenHardshipIsRetrieved() {
        when(hardshipReviewRepository.findByRepId(MOCK_REP_ID))
                .thenReturn(HardshipReviewEntity.builder().id(MOCK_HARDSHIP_ID).repId(MOCK_REP_ID).build());

        HardshipReviewEntity returnedEntity = hardshipReviewImpl.findByRepId(MOCK_REP_ID);

        assertThat(returnedEntity.getId()).isEqualTo(MOCK_HARDSHIP_ID);
        assertThat(returnedEntity.getRepId()).isEqualTo(MOCK_REP_ID);
    }

    @Test
    void givenHardshipDTO_whenCreateIsInvoked_thenHardshipIsPersisted() {
        HardshipReviewDTO hardshipReviewDTO = TestModelDataBuilder.getHardshipReviewDTOWithRelationships();
        when(hardshipReviewMapper.hardshipReviewDTOToHardshipReviewEntity(any(HardshipReviewDTO.class))).thenReturn(
                TestEntityDataBuilder.getHardshipReviewEntityWithRelationships());
        when(hardshipReviewDetailReasonRepository.getReferenceById(any(Integer.class))).thenReturn(
                HardshipReviewDetailReasonEntity.builder()
                        .id(MOCK_HARDSHIP_ID)
                        .accepted("Y")
                        .dateCreated(LocalDateTime.now())
                        .userCreated("test-s")
                        .detailType(HardshipReviewDetailType.INCOME)
                        .build());

        hardshipReviewImpl.create(hardshipReviewDTO);
        verify(hardshipReviewRepository).saveAndFlush(hardshipReviewEntityArgumentCaptor.capture());
        assertThat(hardshipReviewEntityArgumentCaptor.getValue().getId()).isEqualTo(hardshipReviewDTO.getId());
    }

    @Test
    void givenHardshipDTO_whenUpdateIsInvoked_thenHardshipIsUpdated() {
        HardshipReviewDTO hardshipReviewDTO = TestModelDataBuilder.getHardshipReviewDTOWithRelationships();
        HardshipReviewEntity mockHardshipEntity = TestEntityDataBuilder.getHardshipReviewEntityWithRelationships();
        when(hardshipReviewRepository.getReferenceById(any(Integer.class))).thenReturn(mockHardshipEntity);
        when(hardshipReviewMapper.hardshipReviewDetailToHardshipReviewDetailEntity(any(HardshipReviewDetail.class))).thenReturn(
                TestEntityDataBuilder.getHardshipReviewDetailsEntity());
        when(hardshipReviewMapper.hardshipReviewProgressToHardshipReviewProgressEntity(any(HardshipReviewProgress.class))).thenReturn(
                TestEntityDataBuilder.getHardshipReviewProgressEntity());
        when(hardshipReviewMapper.newWorkReasonToNewWorkReasonEntity(any(NewWorkReason.class))).thenReturn(
                TestEntityDataBuilder.getNewWorkReasonEntity());

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
