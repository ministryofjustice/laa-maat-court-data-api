package gov.uk.courtdata.contributions.impl;

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


@ExtendWith(MockitoExtension.class)
class ContributionsImplTest {

    private static final Integer TEST_REP_ID = 999;

    @InjectMocks
    private ContributionsImpl contributionsImpl;
    @Mock
    private ContributionsRepository contributionsRepository;

    @Test
    void whenFindIsInvoked_thenContributionsEntriesAreRetrieved() {
        Integer testId = 999;
        when(contributionsRepository.findById(anyInt())).thenReturn(Optional.ofNullable(ContributionsEntity.builder().id(testId).build()));

        ContributionsEntity contributionsEntity = contributionsImpl.find(testId);

        assertThat(contributionsEntity.getId()).isEqualTo(testId);
    }

    @Test
    void whenFindLatestIsInvoked_thenLatestContributionsEntryIsRetrieved() {
        when(contributionsRepository.findByRepIdAndLatestIsTrue(anyInt())).thenReturn(ContributionsEntity.builder().id(TEST_REP_ID).build());

        ContributionsEntity contributionsEntity = contributionsImpl.findLatest(TEST_REP_ID);

        assertThat(contributionsEntity.getId()).isEqualTo(TEST_REP_ID);
    }

    @Test
    void whenUpdateIsInvoked_thenContributionsEntryIsUpdated() {
        ContributionsEntity contributionsEntity = ContributionsEntity.builder().build();
        when(contributionsRepository.saveAndFlush(any(ContributionsEntity.class))).thenReturn(contributionsEntity);

        ContributionsEntity updatedContributionsEntity = contributionsImpl.update(contributionsEntity);

        assertThat(updatedContributionsEntity).isEqualTo(contributionsEntity);
        verify(contributionsRepository).saveAndFlush(contributionsEntity);
    }

    @Test
    void whenUpdateInactivePriorIsInvoked_thenContributionsEntryIsSetInactiveAndPrior() {
        LocalDate testEffectiveDate = LocalDate.now();

        Optional<Void> response = contributionsImpl.updateInactiveAndPrior(TEST_REP_ID, testEffectiveDate);

        assertThat(response).isNotPresent();
        verify(contributionsRepository).setEntryAsInactive(TEST_REP_ID, testEffectiveDate);
        verify(contributionsRepository).setEntryAsPrior(TEST_REP_ID);
    }

    @Test
    void whenCreateIsInvoked_thenContributionsEntryIsInserted() {
        ContributionsEntity contributionsEntity = ContributionsEntity.builder().latest(true).build();
        when(contributionsRepository.saveAndFlush(any(ContributionsEntity.class))).thenReturn(contributionsEntity);

        ContributionsEntity newContributionsEntity = contributionsImpl.update(contributionsEntity);

        assertThat(newContributionsEntity.getLatest()).isTrue();
        verify(contributionsRepository).saveAndFlush(contributionsEntity);
    }
}
