package gov.uk.courtdata.contribution.service;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.contribution.mapper.ContributionsMapper;
import gov.uk.courtdata.contribution.projection.ContributionsSummaryView;
import gov.uk.courtdata.contribution.repository.ContributionsRepository;
import gov.uk.courtdata.dto.ContributionsDTO;
import gov.uk.courtdata.entity.ContributionsEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.crime.common.model.contribution.maat_api.CreateContributionRequest;

import java.time.LocalDate;
import java.util.List;

import static gov.uk.courtdata.builder.TestEntityDataBuilder.REP_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        verify(contributionsMapper).mapEntityToDTO(anyList());
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
        verify(contributionsMapper).mapEntityToDTO(anyList());
    }

    @Test
    void givenContributionsEntryDoesntExist_whenFindIsInvoked_thenExceptionIsRaised() {
        int testRepId = 666;
        when(repository.findByRepOrder_IdAndLatestIsTrue(anyInt())).thenReturn(null);
        assertThatThrownBy(() -> contributionsService.find(testRepId, true))
                .isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining("Contributions entry not found for repId");
    }

    @Test
    void givenAValidContribution_whenCreateIsInvoked_thenContributionsEntryIsCreated() {
        ContributionsEntity contributionsEntity = ContributionsEntity.builder().build();
        when(contributionsMapper.createContributionsToContributionsEntity(any(CreateContributionRequest.class))).thenReturn(contributionsEntity);
        contributionsService.create(new CreateContributionRequest().withRepId(TestModelDataBuilder.REP_ID));
        verify(repository).findByRepOrder_IdAndLatestIsTrue(TestModelDataBuilder.REP_ID);
        verify(repository).saveAndFlush(any(ContributionsEntity.class));
        verify(contributionsMapper).createContributionsToContributionsEntity(any());
    }

    @Test
    void givenContributionsEntryAlreadyExists_whenCreateIsInvoked_thenExistingEntryIsUpdated_andNewContributionsEntryIsCreated() {
        LocalDate testEffectiveDate = LocalDate.now();
        ContributionsEntity contributionsEntity = ContributionsEntity.builder().build();
        when(repository.findByRepOrder_IdAndLatestIsTrue(TestModelDataBuilder.REP_ID)).thenReturn(contributionsEntity);
        when(contributionsMapper.createContributionsToContributionsEntity(any(CreateContributionRequest.class))).thenReturn(contributionsEntity);
        when(repository.saveAndFlush(any(ContributionsEntity.class))).thenReturn(contributionsEntity);
        when(contributionsMapper.mapEntityToDTO(any(ContributionsEntity.class))).thenReturn(ContributionsDTO.builder().repId(TestModelDataBuilder.REP_ID).build());

        contributionsService.create(new CreateContributionRequest().withRepId(TestModelDataBuilder.REP_ID).withEffectiveDate(testEffectiveDate));
        verify(repository).updateExistingContributionToInactive(TestModelDataBuilder.REP_ID, testEffectiveDate);
        verify(repository).updateExistingContributionToPrior(TestModelDataBuilder.REP_ID);
        verify(repository).saveAndFlush(any(ContributionsEntity.class));
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

        assertThatThrownBy(() -> contributionsService.getContributionsSummary(repId))
                .isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining(String.format("No contribution entries found for repId: %d", repId));
    }

}
