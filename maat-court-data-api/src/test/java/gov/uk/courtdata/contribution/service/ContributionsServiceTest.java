package gov.uk.courtdata.contribution.service;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.contribution.mapper.ContributionsMapper;
import gov.uk.courtdata.contribution.model.CreateContributions;
import gov.uk.courtdata.contribution.model.UpdateContributions;
import gov.uk.courtdata.contribution.projection.ContributionsSummaryView;
import gov.uk.courtdata.dto.ContributionsDTO;
import gov.uk.courtdata.entity.ContributionsEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.contribution.repository.ContributionsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static gov.uk.courtdata.builder.TestEntityDataBuilder.REP_ID;
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
        when(repository.findByRepOrder_IdAndLatestIsTrue(anyInt()))
                .thenReturn(ContributionsEntity
                        .builder()
                        .repOrder(TestEntityDataBuilder
                                .getPopulatedRepOrder(REP_ID))
                        .build());
        contributionsService.find(TestModelDataBuilder.REP_ID, true);
        verify(repository).findByRepOrder_IdAndLatestIsTrue(TestModelDataBuilder.REP_ID);
        verify(contributionsMapper).mapEntityToDTO(any(List.class));
    }

    @Test
    void givenAValidRepIdAndFindAllRepId_whenFindIsInvoked_thenContributionsEntryIsRetrieved() {
        when(repository.findAllByRepOrder_Id(anyInt())).thenReturn(List.of(ContributionsEntity
                .builder()
                .repOrder(TestEntityDataBuilder
                        .getPopulatedRepOrder(REP_ID))
                .build()));
        contributionsService.find(TestModelDataBuilder.REP_ID, false);
        verify(repository).findAllByRepOrder_Id(TestModelDataBuilder.REP_ID);
        verify(contributionsMapper).mapEntityToDTO(any(List.class));
    }

    @Test
    void givenContributionsEntryDoesntExist_whenFindIsInvoked_thenExceptionIsRaised() {
        Integer testRepId = 666;
        when(repository.findByRepOrder_IdAndLatestIsTrue(anyInt())).thenReturn(null);
        assertThatThrownBy(() -> {
            contributionsService.find(testRepId, true);
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
        verify(repository).findByRepOrder_IdAndLatestIsTrue(TestModelDataBuilder.REP_ID);
        verify(repository).saveAndFlush(any(ContributionsEntity.class));
        verify(contributionsMapper).createContributionsToContributionsEntity(any());
    }

    @Test
    void givenContributionsEntryAlreadyExists_whenCreateIsInvoked_thenExistingEntryIsUpdated_andNewContributionsEntryIsCreated() {
        LocalDate testEffectiveDate = LocalDate.now();
        ContributionsEntity contributionsEntity = ContributionsEntity.builder().build();
        when(repository.findByRepOrder_IdAndLatestIsTrue(TestModelDataBuilder.REP_ID)).thenReturn(contributionsEntity);
        when(contributionsMapper.createContributionsToContributionsEntity(any(CreateContributions.class))).thenReturn(contributionsEntity);
        when(repository.saveAndFlush(any(ContributionsEntity.class))).thenReturn(contributionsEntity);
        when(contributionsMapper.mapEntityToDTO(any(ContributionsEntity.class))).thenReturn(ContributionsDTO.builder().repId(TestModelDataBuilder.REP_ID).build());

        contributionsService.create(CreateContributions.builder().repId(TestModelDataBuilder.REP_ID).effectiveDate(testEffectiveDate).build());
        verify(repository).updateExistingContributionToInactive(TestModelDataBuilder.REP_ID, testEffectiveDate);
        verify(repository).updateExistingContributionToPrior(TestModelDataBuilder.REP_ID);
        verify(repository).saveAndFlush(any(ContributionsEntity.class));
    }

    @Test
    void givenAValidRepId_whenGetContributionCountIsInvoked_thenReturnContributionCount() {
        when(repository.getContributionCount(TestModelDataBuilder.REP_ID)).thenReturn(1);
        contributionsService.getContributionCount(TestModelDataBuilder.REP_ID);
        verify(repository).getContributionCount(TestModelDataBuilder.REP_ID);
    }

    @Test
    void givenAValidRepId_whenGetContributionsSummaryIsInvoked_thenContributionsSummaryIsReturned() {
        List<ContributionsSummaryView> contributionsSummaryViewEntities = List.of(TestModelDataBuilder.getContributionsSummaryView());
        when(repository.getContributionsSummary(TestModelDataBuilder.REP_ID)).thenReturn(contributionsSummaryViewEntities);

        contributionsService.getContributionsSummary(TestModelDataBuilder.REP_ID);

        verify(repository).getContributionsSummary(TestModelDataBuilder.REP_ID);
        verify(contributionsMapper).contributionsSummaryToContributionsSummaryDTO(contributionsSummaryViewEntities);
    }

    @Test
    void givenAnInvalidRepId_whenGetContributionsSummaryIsInvoked_thenNotFoundExceptionIsRaised() {
        Integer repId = 666;
        List<ContributionsSummaryView> contributionsSummaryViewEntities = List.of();
        when(repository.getContributionsSummary(repId)).thenReturn(contributionsSummaryViewEntities);

        assertThatThrownBy(() -> {
            contributionsService.getContributionsSummary(repId);
        }).isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining(String.format("No contribution entries found for repId: %d", repId));
    }

    @Test
    void givenAValidRepId_whenFindByRepIdAndLatestSentContributionIsInvoked_thenContributionsEntryIsRetrieved() {
        when(repository.findByRepIdAndLatestSentContribution(anyInt())).thenReturn(ContributionsEntity
                .builder()
                .repOrder(TestEntityDataBuilder
                        .getPopulatedRepOrder(REP_ID))
                .build());
        contributionsService.findByRepIdAndLatestSentContribution(TestModelDataBuilder.REP_ID);
        verify(repository).findByRepIdAndLatestSentContribution(TestModelDataBuilder.REP_ID);
        verify(contributionsMapper).mapEntityToDTO(any(ContributionsEntity.class));
    }

    @Test
    void givenAValidRepIdAndFindByRepIdAndLatestSentContributionAllRepId_whenFindIsInvoked_thenContributionsEntryIsRetrieved() {
        when(repository.findByRepIdAndLatestSentContribution(anyInt())).thenReturn(ContributionsEntity
                .builder()
                .repOrder(TestEntityDataBuilder
                        .getPopulatedRepOrder(REP_ID))
                .build());
        contributionsService.findByRepIdAndLatestSentContribution(TestModelDataBuilder.REP_ID);
        verify(repository).findByRepIdAndLatestSentContribution(TestModelDataBuilder.REP_ID);
        verify(contributionsMapper).mapEntityToDTO(any(ContributionsEntity.class));
    }

    @Test
    void givenContributionsEntryDoesntExist_whenFindByRepIdAndLatestSentContributionIsInvoked_thenExceptionIsRaised() {
        Integer testRepId = 666;
        when(repository.findByRepOrder_IdAndLatestIsTrue(anyInt())).thenReturn(null);
        assertThatThrownBy(() -> {
            contributionsService.find(testRepId, true);
        }).isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining("Contributions entry not found for repId");
    }
}
