package gov.uk.courtdata.dces.service;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.dces.mapper.ContributionFileMapper;
import gov.uk.courtdata.dces.mapper.ContributionFileMapperImpl;
import gov.uk.courtdata.model.id.ContributionFileErrorsId;
import gov.uk.courtdata.repository.ContributionFileErrorsRepository;
import gov.uk.courtdata.repository.ContributionFilesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.CollectionAssert.assertThatCollection;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContributionFileServiceTest {
    @InjectMocks
    private ContributionFileService contributionFileService;

    @Mock
    private ContributionFilesRepository contributionFileRepository;

    @Mock
    private ContributionFileErrorsRepository contributionFileErrorRepository;

    @Spy // injected into contributionFileService
    private ContributionFileMapper mapper = new ContributionFileMapperImpl();

    @Test
    void givenCorrectArguments_whenGetContributionFileIsInvoked_thenReturnIsFound() {
        final int fileId = 99;
        final var entity = TestEntityDataBuilder.getPopulatedContributionFilesEntity(fileId);
        when(contributionFileRepository.findById(fileId)).thenReturn(Optional.of(entity));

        final var optional = contributionFileService.getContributionFile(fileId);
        verify(contributionFileRepository).findById(fileId);
        assertThat(optional).isPresent();
        assertThat(optional.orElseThrow().getId()).isEqualTo(fileId);
    }

    @Test
    void givenIncorrectArguments_whenGetContributionFileIsInvoked_thenReturnIsEmpty() {
        final int incorrectFileId = 666;
        when(contributionFileRepository.findById(incorrectFileId)).thenReturn(Optional.empty());

        final var optional = contributionFileService.getContributionFile(incorrectFileId);
        verify(contributionFileRepository).findById(incorrectFileId);
        assertThat(optional).isEmpty();
    }

    @Test
    void givenCorrectArguments_whenGetAllContributionFileErrorIsInvoked_thenReturnIsFound() {
        final int fileId = 99;
        final int contributionId = 888;
        final var entity = TestEntityDataBuilder.getContributionFileErrorsEntity(fileId, contributionId);
        when(contributionFileErrorRepository.findByContributionFileId(fileId)).thenReturn(List.of(entity));

        final var list = contributionFileService.getAllContributionFileError(fileId);
        verify(contributionFileErrorRepository).findByContributionFileId(fileId);
        assertThatCollection(list).isNotEmpty();
        assertThat(list.get(0).getContributionFileId()).isEqualTo(fileId);
    }

    @Test
    void givenIncorrectArguments_whenGetAllContributionFileErrorIsInvoked_thenReturnIsEmpty() {
        final int incorrectFileId = 666;
        when(contributionFileErrorRepository.findByContributionFileId(incorrectFileId)).thenReturn(Collections.emptyList());

        final var list = contributionFileService.getAllContributionFileError(incorrectFileId);
        verify(contributionFileErrorRepository).findByContributionFileId(incorrectFileId);
        assertThatCollection(list).isEmpty();
    }

    @Test
    void givenCorrectArguments_whenGetContributionFileErrorIsInvoked_thenReturnIsFound() {
        final int fileId = 99;
        final int contributionId = 888;
        final var entity = TestEntityDataBuilder.getContributionFileErrorsEntity(fileId, contributionId);
        final var compositeId = new ContributionFileErrorsId(contributionId, fileId);
        when(contributionFileErrorRepository.findById(eq(compositeId))).thenReturn(Optional.of(entity));

        final var optional = contributionFileService.getContributionFileError(contributionId, fileId);
        verify(contributionFileErrorRepository).findById(eq(compositeId));
        assertThat(optional).isPresent();
        assertThat(optional.orElseThrow().getContributionFileId()).isEqualTo(fileId);
        assertThat(optional.orElseThrow().getContributionId()).isEqualTo(contributionId);
    }

    @Test
    void givenIncorrectArguments_whenGetContributionFileErrorIsInvoked_thenReturnIsEmpty() {
        final int incorrectFileId = 666;
        final int incorrectContribtutionId = 666;
        final var compositeId = new ContributionFileErrorsId(incorrectFileId, incorrectContribtutionId);
        when(contributionFileErrorRepository.findById(eq(compositeId))).thenReturn(Optional.empty());

        final var optional = contributionFileService.getContributionFileError(incorrectFileId, incorrectContribtutionId);
        verify(contributionFileErrorRepository).findById(eq(compositeId));
        assertThat(optional).isEmpty();
    }
}
