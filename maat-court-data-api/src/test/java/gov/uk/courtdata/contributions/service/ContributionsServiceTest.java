package gov.uk.courtdata.contributions.service;

import gov.uk.courtdata.builder.TestModelDataBuilder;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContributionsServiceTest {

    @InjectMocks
    private ContributionsService contributionsService;
    @Mock
    private ContributionsImpl contributionsImpl;
    @Mock
    private ContributionsMapper contributionsMapper;

    @Test
    void whenFindIsInvoked_thenContributionsEntryIsRetrieved() {
        when(contributionsImpl.findLatest(anyInt())).thenReturn(ContributionsEntity.builder().repId(TestModelDataBuilder.REP_ID).build());
        contributionsService.find(TestModelDataBuilder.REP_ID);
        verify(contributionsImpl).findLatest(anyInt());
        verify(contributionsMapper).mapEntityToDTO(any());
    }

    @Test
    void givenContributionsEntryDoesntExist_whenFindIsInvoked_thenExceptionIsRaised() {
        Integer testRepId = 666;
        when(contributionsImpl.findLatest(anyInt())).thenReturn(null);

        assertThatThrownBy(() -> {
            contributionsService.find(testRepId);
        }).isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining("Contributions entry not found for repId");
    }

    @Test
    void whenUpdateIsInvoked_thenContributionsEntryIsUpdated() {
        Integer testId = 666;
        ContributionsEntity contributionsEntity = ContributionsEntity.builder().build();
        when(contributionsImpl.find(anyInt())).thenReturn(contributionsEntity);
        when(contributionsImpl.update(any(ContributionsEntity.class))).thenReturn(contributionsEntity);
        contributionsService.update(UpdateContributions.builder().id(testId).build());
        verify(contributionsImpl).update(contributionsEntity);
        verify(contributionsMapper).mapEntityToDTO(any());
    }

    @Test
    void givenContributionsEntryDoesntExist_whenUpdateIsInvoked_thenExceptionIsRaised() {
        Integer testId = 666;
        when(contributionsImpl.find(anyInt())).thenReturn(null);
        assertThatThrownBy(() -> {
            contributionsService.update(UpdateContributions.builder().id(testId).build());
        }).isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining("Contributions entry not found for id 666");
    }

    @Test
    void whenCreateIsInvoked_thenContributionsEntryIsCreated() {
        contributionsService.create(CreateContributions.builder().repId(TestModelDataBuilder.REP_ID).build());
        verify(contributionsImpl).create(any());
        verify(contributionsMapper).mapEntityToDTO(any());
        verify(contributionsMapper).createContributionsToContributionsEntity(any());
    }

    @Test
    void givenContributionsEntryAlreadyExists_whenCreateIsInvoked_thenExistingEntryIsUpdated_andNewContributionsEntryIsCreated() {
        LocalDate testEffectiveDate = LocalDate.now();
        ContributionsEntity contributionsEntity = ContributionsEntity.builder().build();
        when(contributionsImpl.findLatest(anyInt())).thenReturn(contributionsEntity);
        when(contributionsMapper.createContributionsToContributionsEntity(any(CreateContributions.class))).thenReturn(contributionsEntity);
        when(contributionsImpl.create(any(ContributionsEntity.class))).thenReturn(contributionsEntity);
        when(contributionsMapper.mapEntityToDTO(any(ContributionsEntity.class))).thenReturn(ContributionsDTO.builder().repId(TestModelDataBuilder.REP_ID).build());

        contributionsService.create(CreateContributions.builder().repId(TestModelDataBuilder.REP_ID).effectiveDate(testEffectiveDate).build());
        verify(contributionsImpl).updateExistingContributions(TestModelDataBuilder.REP_ID, testEffectiveDate);
        verify(contributionsImpl).create(any());
    }
}
