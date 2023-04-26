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
public class ContributionsServiceTest {

    @InjectMocks
    private ContributionsService contributionsService;

    @Mock
    private ContributionsImpl contributionsImpl;

    @Mock
    private ContributionsMapper contributionsMapper;

    @Test
    public void whenFindIsInvoked_thenContributionsEntryIsRetrieved() {
        Integer testRepId = 999;
        when(contributionsImpl.findLatest(any())).thenReturn(ContributionsEntity.builder().repId(testRepId).build());
        when(contributionsMapper.mapEntityToDTO(any())).thenReturn(ContributionsDTO.builder().repId(testRepId).build());

        ContributionsDTO contributionsDTO = contributionsService.find(testRepId);

        assertThat(contributionsDTO.getRepId()).isEqualTo(testRepId);
    }

    @Test
    public void givenContributionsEntryDoesntExist_whenFindIsInvoked_thenExceptionIsRaised() {
        Integer testRepId = 666;
        when(contributionsImpl.findLatest(any())).thenReturn(null);

        assertThatExceptionOfType(RequestedObjectNotFoundException.class)
                .isThrownBy(() -> contributionsService.find(testRepId))
                .withMessageContaining(String.format("Contributions entry not found for repId %d", testRepId));

    }

    @Test
    public void whenUpdateIsInvoked_thenContributionsEntryIsUpdated() {
        Integer testId = 999;
        ContributionsEntity contributionsEntity = ContributionsEntity.builder().build();
        when(contributionsImpl.find(any())).thenReturn(contributionsEntity);
        when(contributionsMapper.mapEntityToDTO(any())).thenReturn(ContributionsDTO.builder().id(testId).build());

        ContributionsDTO contributionsDTO = contributionsService.update(UpdateContributions.builder().id(testId).build());

        assertThat(contributionsDTO.getId()).isEqualTo(testId);
        verify(contributionsImpl).update(contributionsEntity);
    }

    @Test
    public void givenContributionsEntryDoesntExist_whenUpdateIsInvoked_thenExceptionIsRaised() {
        Integer testId = 666;
        when(contributionsImpl.find(any())).thenReturn(null);

        assertThatExceptionOfType(RequestedObjectNotFoundException.class)
                .isThrownBy(() -> contributionsService.update(UpdateContributions.builder().id(testId).build()))
                .withMessageContaining(String.format("Contributions entry not found for id %d", testId));
    }

    @Test
    public void whenCreateIsInvoked_thenContributionsEntryIsCreated() {
        Integer testRepId = 999;
        ContributionsEntity contributionsEntity = ContributionsEntity.builder().build();
        when(contributionsImpl.findLatest(any())).thenReturn(null);
        when(contributionsMapper.mapEntityToDTO(any())).thenReturn(ContributionsDTO.builder().repId(testRepId).build());
        when(contributionsMapper.createContributionsToContributionsEntity(any())).thenReturn(contributionsEntity);

        ContributionsDTO contributionsDTO = contributionsService.create(CreateContributions.builder().repId(testRepId).build());

        assertThat(contributionsDTO.getRepId()).isEqualTo(testRepId);
        verify(contributionsImpl).create(contributionsEntity);
    }

    @Test
    public void givenContributionsEntryAlreadyExists_whenCreateIsInvoked_thenExistingEntryIsUpdated_andNewContributionsEntryIsCreated() {
        Integer testRepId = 999;
        LocalDate testEffectiveDate = LocalDate.now();
        ContributionsEntity contributionsEntity = ContributionsEntity.builder().build();
        when(contributionsImpl.findLatest(any())).thenReturn(contributionsEntity);
        when(contributionsMapper.mapEntityToDTO(any())).thenReturn(ContributionsDTO.builder().repId(testRepId).build());
        when(contributionsMapper.createContributionsToContributionsEntity(any())).thenReturn(contributionsEntity);

        ContributionsDTO contributionsDTO = contributionsService.create(CreateContributions.builder().repId(testRepId).effectiveDate(testEffectiveDate).build());

        assertThat(contributionsDTO.getRepId()).isEqualTo(testRepId);
        verify(contributionsImpl).updateInactiveAndPrior(testRepId, testEffectiveDate);
        verify(contributionsImpl).create(contributionsEntity);
    }
}
