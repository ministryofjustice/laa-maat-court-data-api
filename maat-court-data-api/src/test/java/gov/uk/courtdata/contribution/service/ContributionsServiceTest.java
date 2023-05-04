package gov.uk.courtdata.contribution.service;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.contribution.mapper.ContributionsMapper;
import gov.uk.courtdata.dto.ContributionsDTO;
import gov.uk.courtdata.entity.ContributionsEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.model.contributions.CreateContributions;
import gov.uk.courtdata.model.contributions.UpdateContributions;
import gov.uk.courtdata.repository.ContributionsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContributionsServiceTest {

    @InjectMocks
    private ContributionsService contributionsService;
    @Mock
    private ContributionsRepository repository;
    @Mock
    private ContributionsMapper contributionsMapper;

    @Test
    void givenAValidRepId_whenFindIsInvoked_thenContributionsEntryIsRetrieved() {
        when(repository.findByRepIdAndLatestIsTrue(anyInt())).thenReturn(ContributionsEntity.builder().repId(TestModelDataBuilder.REP_ID).build());
        contributionsService.find(TestModelDataBuilder.REP_ID);
        verify(repository).findByRepIdAndLatestIsTrue(TestModelDataBuilder.REP_ID);
        verify(contributionsMapper).mapEntityToDTO(any(ContributionsEntity.class));
    }

    @Test
    void givenContributionsEntryDoesntExist_whenFindIsInvoked_thenExceptionIsRaised() {
        Integer testRepId = 666;
        when(repository.findByRepIdAndLatestIsTrue(anyInt())).thenReturn(null);
        assertThatThrownBy(() -> {
            contributionsService.find(testRepId);
        }).isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining("Contributions entry not found for repId");
    }

    @Test
    void givenAValidInput_whenUpdateIsInvoked_thenContributionsEntryIsUpdated() {
        Integer testId = 666;
        ContributionsEntity contributionsEntity = ContributionsEntity.builder().build();
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(contributionsEntity));
        when(repository.saveAndFlush(any(ContributionsEntity.class))).thenReturn(contributionsEntity);
        contributionsService.update(UpdateContributions.builder().id(testId).build());
        verify(repository).saveAndFlush(contributionsEntity);
        verify(contributionsMapper).mapEntityToDTO(any(ContributionsEntity.class));
    }

    @Test
    void givenContributionsEntryDoesntExist_whenUpdateIsInvoked_thenExceptionIsRaised() {
        Integer testId = 666;
        when(repository.findById(anyInt())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> {
            contributionsService.update(UpdateContributions.builder().id(testId).build());
        }).isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining("Contributions entry not found for id 666");
    }

    @Test
    void givenAValidContribution_whenCreateIsInvoked_thenContributionsEntryIsCreated() {
        ContributionsEntity contributionsEntity = ContributionsEntity.builder().build();
        when(contributionsMapper.createContributionsToContributionsEntity(any(CreateContributions.class))).thenReturn(contributionsEntity);
        contributionsService.create(CreateContributions.builder().repId(TestModelDataBuilder.REP_ID).build());
        verify(repository).findByRepIdAndLatestIsTrue(TestModelDataBuilder.REP_ID);
        verify(repository).saveAndFlush(any(ContributionsEntity.class));
        verify(contributionsMapper).createContributionsToContributionsEntity(any());
    }

    @Test
    void givenContributionsEntryAlreadyExists_whenCreateIsInvoked_thenExistingEntryIsUpdated_andNewContributionsEntryIsCreated() {
        LocalDate testEffectiveDate = LocalDate.now();
        ContributionsEntity contributionsEntity = ContributionsEntity.builder().build();
        when(repository.findByRepIdAndLatestIsTrue(TestModelDataBuilder.REP_ID)).thenReturn(contributionsEntity);
        when(contributionsMapper.createContributionsToContributionsEntity(any(CreateContributions.class))).thenReturn(contributionsEntity);
        when(repository.saveAndFlush(any(ContributionsEntity.class))).thenReturn(contributionsEntity);
        when(contributionsMapper.mapEntityToDTO(any(ContributionsEntity.class))).thenReturn(ContributionsDTO.builder().repId(TestModelDataBuilder.REP_ID).build());

        contributionsService.create(CreateContributions.builder().repId(TestModelDataBuilder.REP_ID).effectiveDate(testEffectiveDate).build());
        verify(repository).updateExistingContributionToInactive(TestModelDataBuilder.REP_ID, testEffectiveDate);
        verify(repository).updateExistingContributionToPrior(TestModelDataBuilder.REP_ID);
        verify(repository).saveAndFlush(any(ContributionsEntity.class));
    }
}
