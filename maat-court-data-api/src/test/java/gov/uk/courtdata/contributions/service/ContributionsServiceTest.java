package gov.uk.courtdata.contributions.service;

import gov.uk.courtdata.contributions.impl.ContributionsImpl;
import gov.uk.courtdata.contributions.mapper.ContributionsMapper;
import gov.uk.courtdata.dto.ContributionsDTO;
import gov.uk.courtdata.entity.ContributionsEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.model.contributions.CreateContributions;
import gov.uk.courtdata.model.contributions.UpdateContributions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class ContributionsServiceTest {

    private static final Integer TEST_REP_ID = 999;

    @InjectMocks
    private ContributionsService contributionsService;
    @Mock
    private ContributionsImpl contributionsImpl;
    @Mock
    private ContributionsMapper contributionsMapper;

    @Test
    void whenFindIsInvoked_thenContributionsEntryIsRetrieved() {
        when(contributionsImpl.findLatest(anyInt())).thenReturn(ContributionsEntity.builder().repId(TEST_REP_ID).build());
        when(contributionsMapper.mapEntityToDTO(any(ContributionsEntity.class))).thenReturn(ContributionsDTO.builder().repId(TEST_REP_ID).build());

        ContributionsDTO contributionsDTO = contributionsService.find(TEST_REP_ID);

        assertThat(contributionsDTO.getRepId()).isEqualTo(TEST_REP_ID);
    }

    @Test
    void givenContributionsEntryDoesntExist_whenFindIsInvoked_thenExceptionIsRaised() {
        Integer testRepId = 666;
        when(contributionsImpl.findLatest(anyInt())).thenReturn(null);

        assertThatExceptionOfType(RequestedObjectNotFoundException.class)
                .isThrownBy(() -> contributionsService.find(testRepId))
                .withMessageContaining(String.format("Contributions entry not found for repId %d", testRepId));

    }

    @Test
    void whenUpdateIsInvoked_thenContributionsEntryIsUpdated() {
        Integer testId = 999;
        ContributionsEntity contributionsEntity = ContributionsEntity.builder().build();
        when(contributionsImpl.find(anyInt())).thenReturn(contributionsEntity);
        when(contributionsImpl.update(any(ContributionsEntity.class))).thenReturn(contributionsEntity);
        when(contributionsMapper.mapEntityToDTO(any(ContributionsEntity.class))).thenReturn(ContributionsDTO.builder().id(testId).build());

        ContributionsDTO contributionsDTO = contributionsService.update(UpdateContributions.builder().id(testId).build());

        assertThat(contributionsDTO.getId()).isEqualTo(testId);
        verify(contributionsImpl).update(contributionsEntity);
    }

    @Test
    void givenContributionsEntryDoesntExist_whenUpdateIsInvoked_thenExceptionIsRaised() {
        Integer testId = 666;
        when(contributionsImpl.find(anyInt())).thenReturn(null);

        assertThatExceptionOfType(RequestedObjectNotFoundException.class)
                .isThrownBy(() -> contributionsService.update(UpdateContributions.builder().id(testId).build()))
                .withMessageContaining(String.format("Contributions entry not found for id %d", testId));
    }

    @Test
    void whenCreateIsInvoked_thenContributionsEntryIsCreated() {
        ContributionsEntity contributionsEntity = ContributionsEntity.builder().build();
        when(contributionsImpl.findLatest(anyInt())).thenReturn(null);
        when(contributionsMapper.createContributionsToContributionsEntity(any(CreateContributions.class))).thenReturn(contributionsEntity);
        when(contributionsImpl.create(any(ContributionsEntity.class))).thenReturn(contributionsEntity);
        when(contributionsMapper.mapEntityToDTO(any(ContributionsEntity.class))).thenReturn(ContributionsDTO.builder().repId(TEST_REP_ID).build());

        ContributionsDTO contributionsDTO = contributionsService.create(CreateContributions.builder().repId(TEST_REP_ID).build());

        assertThat(contributionsDTO.getRepId()).isEqualTo(TEST_REP_ID);
        verify(contributionsImpl).create(contributionsEntity);
    }

    @Test
    void givenContributionsEntryAlreadyExists_whenCreateIsInvoked_thenExistingEntryIsUpdated_andNewContributionsEntryIsCreated() {
        LocalDate testEffectiveDate = LocalDate.now();
        ContributionsEntity contributionsEntity = ContributionsEntity.builder().build();
        when(contributionsImpl.findLatest(anyInt())).thenReturn(contributionsEntity);
        when(contributionsMapper.createContributionsToContributionsEntity(any(CreateContributions.class))).thenReturn(contributionsEntity);
        when(contributionsImpl.create(any(ContributionsEntity.class))).thenReturn(contributionsEntity);
        when(contributionsMapper.mapEntityToDTO(any(ContributionsEntity.class))).thenReturn(ContributionsDTO.builder().repId(TEST_REP_ID).build());

        ContributionsDTO contributionsDTO = contributionsService.create(CreateContributions.builder().repId(TEST_REP_ID).effectiveDate(testEffectiveDate).build());

        assertThat(contributionsDTO.getRepId()).isEqualTo(TEST_REP_ID);
        verify(contributionsImpl).updateInactiveAndPrior(TEST_REP_ID, testEffectiveDate);
        verify(contributionsImpl).create(contributionsEntity);
    }
}
