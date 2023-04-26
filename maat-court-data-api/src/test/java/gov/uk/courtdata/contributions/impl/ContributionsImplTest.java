package gov.uk.courtdata.contributions.impl;

import gov.uk.courtdata.entity.ContributionsEntity;
import gov.uk.courtdata.repository.ContributionsRepository;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ContributionsImplTest {

    @InjectMocks
    private ContributionsImpl contributionsImpl;

    @Mock
    private ContributionsRepository contributionsRepository;

    @Test
    public void whenFindIsInvoked_thenContributionsEntriesAreRetrieved() {
        Integer testId = 999;
        when(contributionsRepository.findById(any())).thenReturn(Optional.ofNullable(ContributionsEntity.builder().id(testId).build()));

        ContributionsEntity contributionsEntity = contributionsImpl.find(testId);

        assertThat(contributionsEntity.getId()).isEqualTo(testId);
    }

    @Test
    public void whenFindLatestIsInvoked_thenLatestContributionsEntryIsRetrieved() {
        Integer testRepId = 999;
        when(contributionsRepository.findByRepIdAndLatestIsTrue(any())).thenReturn(ContributionsEntity.builder().id(testRepId).build());

        ContributionsEntity contributionsEntity = contributionsImpl.findLatest(testRepId);

        assertThat(contributionsEntity.getId()).isEqualTo(testRepId);
    }

    @Test
    public void whenUpdateIsInvoked_thenContributionsEntryIsUpdated() {
        ContributionsEntity contributionsEntity = ContributionsEntity.builder().build();
        when(contributionsRepository.saveAndFlush(any())).thenReturn(contributionsEntity);

        ContributionsEntity updatedContributionsEntity = contributionsImpl.update(contributionsEntity);

        assertThat(updatedContributionsEntity).isEqualTo(contributionsEntity);
        verify(contributionsRepository).saveAndFlush(contributionsEntity);
    }

    @Test
    public void whenUpdateInactivePriorIsInvoked_thenContributionsEntryIsSetInactiveAndPrior() {
        Integer testRepId = 999;
        LocalDate testEffectiveDate = LocalDate.now();

        Optional<Void> response = contributionsImpl.updateInactiveAndPrior(testRepId, testEffectiveDate);

        assertThat(response).isEqualTo(Optional.empty());
        verify(contributionsRepository).setEntryAsInactive(testRepId, testEffectiveDate);
        verify(contributionsRepository).setEntryAsPrior(testRepId);
    }

    @Test
    public void whenCreateIsInvoked_thenContributionsEntryIsInserted() {
        ContributionsEntity contributionsEntity = ContributionsEntity.builder().latest(true).build();
        when(contributionsRepository.saveAndFlush(any())).thenReturn(contributionsEntity);

        ContributionsEntity newContributionsEntity = contributionsImpl.update(contributionsEntity);

        assertThat(newContributionsEntity.getLatest()).isEqualTo(true);
        verify(contributionsRepository).saveAndFlush(contributionsEntity);
    }
}
