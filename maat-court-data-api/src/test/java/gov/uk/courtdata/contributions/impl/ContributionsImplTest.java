package gov.uk.courtdata.contributions.impl;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.ContributionsEntity;
import gov.uk.courtdata.repository.ContributionsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class ContributionsImplTest {

    @InjectMocks
    private ContributionsImpl contributionsImpl;
    @Mock
    private ContributionsRepository contributionsRepository;

    @Test
    void whenFindIsInvoked_thenContributionsEntriesAreRetrieved() {
        Integer testId = 999;
        when(contributionsRepository.findById(anyInt())).thenReturn(Optional.ofNullable(ContributionsEntity.builder().id(testId).build()));
        contributionsImpl.find(testId);
        verify(contributionsRepository).findById(anyInt());
    }

    @Test
    void whenFindLatestIsInvoked_thenLatestContributionsEntryIsRetrieved() {
        when(contributionsRepository.findByRepIdAndLatestIsTrue(anyInt())).thenReturn(ContributionsEntity.builder().id(TestModelDataBuilder.REP_ID).build());
        contributionsImpl.findLatest(TestModelDataBuilder.REP_ID);
        verify(contributionsRepository, times(1)).findByRepIdAndLatestIsTrue(anyInt());
    }

    @Test
    void whenUpdateIsInvoked_thenContributionsEntryIsUpdated() {
        ContributionsEntity contributionsEntity = ContributionsEntity.builder().build();
        when(contributionsRepository.saveAndFlush(any(ContributionsEntity.class))).thenReturn(contributionsEntity);
        contributionsImpl.update(contributionsEntity);
        verify(contributionsRepository).saveAndFlush(contributionsEntity);
    }

    @Test
    void whenUpdateExistingContributionToInactiveIsInvoked_thenContributionsEntryIsSetInactiveAndPrior() {
        LocalDate testEffectiveDate = LocalDate.now();
        contributionsImpl.updateExistingContributions(TestModelDataBuilder.REP_ID, testEffectiveDate);
        verify(contributionsRepository).updateExistingContributionToInactive(TestModelDataBuilder.REP_ID, testEffectiveDate);
        verify(contributionsRepository).updateExistingContributionToPrior(TestModelDataBuilder.REP_ID);
    }

    @Test
    void whenCreateIsInvoked_thenContributionsEntryIsInserted() {
        ContributionsEntity contributionsEntity = ContributionsEntity.builder().latest(true).build();
        when(contributionsRepository.saveAndFlush(any(ContributionsEntity.class))).thenReturn(contributionsEntity);
        contributionsImpl.update(contributionsEntity);
        verify(contributionsRepository).saveAndFlush(any());
    }
}
