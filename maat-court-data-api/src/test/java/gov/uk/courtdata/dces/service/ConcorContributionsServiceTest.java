package gov.uk.courtdata.dces.service;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dces.mapper.ConcorContributionMapper;
import gov.uk.courtdata.dces.mapper.ContributionFileMapper;
import gov.uk.courtdata.dces.request.CreateContributionFileRequest;
import gov.uk.courtdata.dces.request.UpdateConcorContributionStatusRequest;
import gov.uk.courtdata.dces.response.ConcorContributionResponse;
import gov.uk.courtdata.dces.response.ConcorContributionResponseDTO;
import gov.uk.courtdata.entity.ConcorContributionsEntity;
import gov.uk.courtdata.entity.ContributionFileErrorsEntity;
import gov.uk.courtdata.entity.ContributionFilesEntity;
import gov.uk.courtdata.enums.ConcorContributionStatus;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.repository.ConcorContributionsRepository;
import gov.uk.courtdata.repository.ContributionFileErrorsRepository;
import gov.uk.courtdata.repository.ContributionFilesRepository;
import gov.uk.courtdata.testutils.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static gov.uk.courtdata.enums.ConcorContributionStatus.ACTIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConcorContributionsServiceTest {
    private List<ConcorContributionsEntity> concorContributionFiles;

    @InjectMocks
    private ConcorContributionsService concorService;

    @Mock
    private ConcorContributionsRepository concorRepository;

    @Mock
    private ContributionFilesRepository contributionFileRepository;

    @Mock
    private ContributionFileMapper contributionFileMapper;

    @Mock
    private DebtCollectionRepository debtCollectionRepository;

    @Mock
    private DebtCollectionService debtCollectionService;

    @Mock
    private ContributionFileErrorsRepository contributionFileErrorsRepository;

    @Mock
    private ConcorContributionMapper concorContributionMapper;

    @Captor
    private ArgumentCaptor<ContributionFilesEntity> contributionFilesEntityArgumentCaptor;
    @Captor
    private ArgumentCaptor<List<ConcorContributionsEntity>> concorContributionEntityListArgumentCaptor;
    @Captor
    private ArgumentCaptor<ContributionFileErrorsEntity> fileErrorCaptor;

    private final String testXml = FileUtils.readResourceToString("eform/request/xmlDoc_default.xml");

    @BeforeEach
    void setUp() {
        concorContributionFiles = new ArrayList<>();
        concorContributionFiles.add(TestEntityDataBuilder.getPopulatedConcorContributionsEntity(-200, testXml));
    }

    @Test
    void givenSomeActiveContributions_whenGetConcorContributionFilesIsCalledWithStatusAndStartId_thenTheyAreReturned() {

        List<ConcorContributionsEntity> entities = List.of(
                TestEntityDataBuilder.getPopulatedConcorContributionsEntity(344, testXml),
                TestEntityDataBuilder.getPopulatedConcorContributionsEntity(345, testXml)
        );
        Pageable pageable = PageRequest.of(0, 3, Sort.by("id"));
        when(concorRepository.findByStatusAndIdGreaterThan(ACTIVE, 343, pageable)).thenReturn(entities);

        List<ConcorContributionResponse> responseList = concorService.getConcorContributionFiles(ACTIVE, 3, 343);

        verify(concorRepository).findByStatusAndIdGreaterThan(any(), any(), any());
        assertThat(responseList)
                .isNotNull()
                .isNotEmpty();
        assertThat(responseList.get(0).getConcorContributionId()).isEqualTo(344);

    }

    @Test
    void givenSomeActiveContributions_whenGetConcorContributionFilesIsCalledWithNullStartId_thenTheyAreReturned() {

        List<ConcorContributionsEntity> entities = List.of(
                populateConcorContributionsEntity(343),
                populateConcorContributionsEntity(344),
                populateConcorContributionsEntity(345),
                populateConcorContributionsEntity(346)
        );
        when(concorRepository.findByStatusAndIdGreaterThan(any(),any(), any())).thenReturn(entities);

        List<ConcorContributionResponse> responseList = concorService.getConcorContributionFiles(ACTIVE, null, null);

        verify(concorRepository).findByStatusAndIdGreaterThan(any(), any(), any());
        assertThat(responseList)
                .isNotNull()
                .isNotEmpty();
        assertThat(responseList.get(0).getConcorContributionId()).isEqualTo(343);
    }

    @Test
    void givenSomeActiveContributions_whenGetConcorContributionFilesIsCalledWithInvalidStartId_thenEmptyListIsReturned() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by("id"));
        when(concorRepository.findByStatusAndIdGreaterThan(ACTIVE, 999, pageable)).thenReturn(List.of());

        List<ConcorContributionResponse> responseList = concorService.getConcorContributionFiles(ACTIVE, 2, 999);

        assertThat(responseList)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void givenSomeActiveContributions_whenGetConcorContributionFileIsCalledWithInvalidId_thenNotFoundExceptionIsThrown() {
        when(concorRepository.findById(0)).thenReturn(Optional.empty());
        Assertions.assertThrows(RequestedObjectNotFoundException.class,
            () -> concorService.getConcorContributionFile(0));
    }

    @Test
    void givenSomeContributionFiles_whenGetConcorContributionFileIsCalledWithValidId_thenItIsReturned() {
        when(concorRepository.findById(110)).thenReturn(Optional.of(ConcorContributionsEntity.builder().id(110).currentXml("XmlContent").build()));
        ConcorContributionResponse response = concorService.getConcorContributionFile(110);
        assertThat(response).isNotNull();
        assertThat(response.getConcorContributionId()).isEqualTo(110);
        assertThat(response.getXmlContent()).isEqualTo("XmlContent");
    }

    @Test
    void givenNullContributionRequest_whencreateContributionAndUpdateConcorStatusIsCalled_thenValidationExceptionIsThrown() {
        Assertions.assertThrows(ValidationException.class,
                () -> concorService.createContributionAndUpdateConcorStatus(null));
        verify(contributionFileRepository, never()).save(any());
        verify(concorRepository, never()).findByStatus(any(ConcorContributionStatus.class));
        verify(concorRepository, never()).saveAll(anyList());
    }

    @Test
    void givenValidRequest_whenCreateContributionAndUpdateConcorStatusIsCalled_thenContributionFileIsCreatedAndStatusUpdatedToSent() {
        final CreateContributionFileRequest createContributionFileRequest
                = CreateContributionFileRequest.builder()
                .concorContributionIds(Set.of(1))
                .xmlContent(testXml)
                .build();
        final ContributionFilesEntity dummyEntity = TestEntityDataBuilder.getPopulatedContributionFilesEntity(1);

        when(concorRepository.findByIdIn(any())).thenReturn(concorContributionFiles);
        when(contributionFileMapper.toContributionFileEntity(createContributionFileRequest)).thenReturn(dummyEntity);

        final var actualResponse = concorService.createContributionAndUpdateConcorStatus(createContributionFileRequest);

        verify(debtCollectionRepository).saveContributionFilesEntity(contributionFilesEntityArgumentCaptor.capture());
        verify(concorRepository).saveAll(concorContributionEntityListArgumentCaptor.capture());

        final ContributionFilesEntity actualContributionFileEntity = contributionFilesEntityArgumentCaptor.getValue();
        final List<ConcorContributionsEntity> contributionEntityList =  concorContributionEntityListArgumentCaptor.getValue();

        assertThat(actualResponse).isNotNull();
        assertThat(actualContributionFileEntity).isNotNull();
        assertThat(actualContributionFileEntity.getAckXmlContent()).isEqualTo("<ackXml>content</ackXml>");
        assertThat(actualContributionFileEntity.getRecordsSent()).isEqualTo(53);
        assertThat(actualContributionFileEntity.getFileId()).isEqualTo(1);
        assertThat(contributionEntityList)
                .isNotNull()
                .hasSize(1);
        assertThat(contributionEntityList.get(0).getStatus().name()).isEqualTo("SENT");
    }

    @Test
    void givenNonExistentIds_whenCreateContributionAndUpdateConcorStatusIsCalled_thenNoSuchElementExceptionIsThrown() {
        final CreateContributionFileRequest createContributionFileRequest = CreateContributionFileRequest.builder()
                .concorContributionIds(Set.of(1))
                .xmlContent(testXml).build();
        ContributionFilesEntity dummyEntity = TestEntityDataBuilder.getPopulatedContributionFilesEntity(1);

        when(concorRepository.findByIdIn(any())).thenReturn(new ArrayList<>());
        when(contributionFileMapper.toContributionFileEntity(createContributionFileRequest)).thenReturn(dummyEntity);

        assertThrows(NoSuchElementException.class, () ->
                concorService.createContributionAndUpdateConcorStatus(createContributionFileRequest));

        verify(debtCollectionRepository).saveContributionFilesEntity(contributionFilesEntityArgumentCaptor.capture());
        assertThat(contributionFilesEntityArgumentCaptor.getValue().getFileId()).isEqualTo(1);
        verify(concorRepository, never()).saveAll(anyList());
    }

    @Test
    void givenEmptyRequest_whenCreateContributionAndUpdateConcorStatusIsCalled_thenExceptionIsThrownAndNoDatabaseCallsAreMade() {
        final CreateContributionFileRequest mockDto = mock(CreateContributionFileRequest.class);

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> concorService.createContributionAndUpdateConcorStatus(mockDto));

        assertThat(exception.getMessage()).isEqualTo("ContributionIds are empty/null.");
        verify(contributionFileRepository, never()).save(any());
        verify(concorRepository, never()).findByStatus(any(ConcorContributionStatus.class));
        verify(concorRepository, never()).saveAll(anyList());
    }

    @Test
    void givenValidRequest_whenLogContributionProcessedIsCalled_thenReceivedCountIsUpdated() {
        int id = 123;
        int repId = 456;
        int fileId = 10000;

        ConcorContributionsEntity concorEntity = TestEntityDataBuilder.getConcorContributionsEntity(repId,ACTIVE,fileId,"");

        when(concorRepository.findById(id)).thenReturn(Optional.of(concorEntity));
        when(debtCollectionService.updateContributionFileReceivedCount(fileId)).thenReturn(true);
        // do
        concorService.logContributionProcessed(TestModelDataBuilder.getLogContributionProcessedRequest(id, ""));
        // verify
        verify(concorRepository).findById(id);
        verify(debtCollectionService).updateContributionFileReceivedCount(fileId);
        verify(contributionFileErrorsRepository, never()).save(any());
    }

    @Test
    void givenRequestWithError_whenLogContributionProcessedIsCalled_thenReceivedCountIsNotUpdatedAndErrorIsSaved() {
        int id = 123;
        int repId = 456;
        int fileId = 10000;
        String errorText = "Error Text";
        LocalDateTime currDate = LocalDateTime.now();

        ConcorContributionsEntity concorEntity = TestEntityDataBuilder.getConcorContributionsEntity(repId,ACTIVE,fileId,"");

        when(concorRepository.findById(id)).thenReturn(Optional.of(concorEntity));

        doNothing().when(debtCollectionService).saveError(fileErrorCaptor.capture());
        // do
        concorService.logContributionProcessed(TestModelDataBuilder.getLogContributionProcessedRequest(id, errorText));
        // verify
        verify(concorRepository).findById(id);
        verify(debtCollectionService, never()).updateContributionFileReceivedCount(fileId);
        ContributionFileErrorsEntity errorEntity = fileErrorCaptor.getValue();

        assertThat(errorEntity.getErrorText()).isEqualTo(errorText);
        assertThat(errorEntity.getConcorContributionId()).isEqualTo(id);
        assertThat(errorEntity.getContributionId()).isEqualTo(id);
        assertThat(errorEntity.getContributionFileId()).isEqualTo(fileId);
        assertThat(errorEntity.getFdcContributionId()).isNull();
        assertThat(errorEntity.getRepId()).isEqualTo(repId);
        assertThat(errorEntity.getDateCreated().getDayOfMonth()).isEqualTo(currDate.getDayOfMonth());
        assertThat(errorEntity.getDateCreated().getMonth()).isEqualTo(currDate.getMonth());
        assertThat(errorEntity.getDateCreated().getYear()).isEqualTo(currDate.getYear());
    }

    @Test
    void givenRequestWithInvalidID_whenLogContributionProcessIsCalled_thenRequestedObjectNotFoundExceptionIsThrownAndNoDBchangesAreMade() {
        int id = 123;
        String errorText = "Error Text";

        when(concorRepository.findById(id)).thenReturn(Optional.empty());
        // do
        assertThrows(RequestedObjectNotFoundException.class, () ->
            concorService.logContributionProcessed(TestModelDataBuilder.getLogContributionProcessedRequest(id, errorText)));
        // verify
        verify(concorRepository).findById(id);
        verify(contributionFileRepository, never()).findById(any());
        verify(contributionFileRepository, never()).save(any());
        verify(contributionFileErrorsRepository, never()).save(any());
    }

    @Test
    void givenInvalidFileId_whenLogContributionProcessedIsCalled_thenNoSuchElementExceptionIsThrownAndNoErrorIsSaved() {
        int id = 123;
        int repId = 456;
        int fileId = 10000;

        ConcorContributionsEntity concorEntity = TestEntityDataBuilder.getConcorContributionsEntity(repId,ACTIVE,fileId,"");
        when(concorRepository.findById(id)).thenReturn(Optional.of(concorEntity));
        when(debtCollectionService.updateContributionFileReceivedCount(fileId)).thenReturn(false);
        // do
        assertThrows(NoSuchElementException.class, () ->
                concorService.logContributionProcessed(TestModelDataBuilder.getLogContributionProcessedRequest(id, "")));
        // verify
        verify(concorRepository).findById(id);
        verify(debtCollectionService).updateContributionFileReceivedCount(any());
        // verify no error is saved, as file is not found.
        verify(debtCollectionService, never()).saveError(any());
    }

    @Test
    void givenValidIDs_whenUpdateConcorContributionStatusAndResetContribFileIsCalled_thenStatusIsUpdatedAndContribFileIsReset() {
        when(concorRepository.findIdsForUpdate(any())).thenReturn(List.of(1111, 2222));
        when(concorRepository.updateStatusAndResetContribFileForIds(any(), anyString(), any())).thenReturn(2);

        List<Integer> response = concorService.updateConcorContributionStatusAndResetContribFile(UpdateConcorContributionStatusRequest.builder().recordCount(2)
                .status(ConcorContributionStatus.SENT).build());

        verify(concorRepository).findIdsForUpdate(any());
        verify(concorRepository).updateStatusAndResetContribFileForIds(any(), anyString(), any());
        assertThat(response).hasSize(2);
    }

    @Test
    void givenInvalidIDs_whenUpdateConcorContributionStatusAndResetContribFileIsCalled_thenStatusIsNotUpdatedAndContribFileIsNotReset() {
        when(concorRepository.findIdsForUpdate(any())).thenReturn(List.of());

        List<Integer> response = concorService.updateConcorContributionStatusAndResetContribFile(UpdateConcorContributionStatusRequest.builder().recordCount(1)
                .status(ConcorContributionStatus.SENT).build());

        verify(concorRepository).findIdsForUpdate(any());
        verify(concorRepository,never()).updateStatusAndResetContribFileForIds(any(), anyString(), any());
        assertThat(response).isEmpty();
    }

    @Test
    void givenValidConcorContributionId_whenGetConcorContributionIsCalled_thenExpectedResponseIsReturned() {
        Integer concorContributionId = 1;
        ConcorContributionsEntity concorContributionsEntity = new ConcorContributionsEntity();
        ConcorContributionResponseDTO expectedResponse = ConcorContributionResponseDTO
                .builder()
                .id(concorContributionId)
                .status(ACTIVE)
                .build();

        when(concorRepository.findById(concorContributionId)).thenReturn(Optional.of(concorContributionsEntity));
        when(concorContributionMapper.toConcorContributionResponseDTO(concorContributionsEntity)).thenReturn(expectedResponse);

        ConcorContributionResponseDTO actualResponse = concorService.getConcorContribution(concorContributionId);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void givenInvalidConcorContributionId_whenGetConcorContributionIsCalled_thenNullResponseIsReturned() {
        Integer concorContributionId = 1;

        when(concorRepository.findById(concorContributionId)).thenReturn(Optional.empty());
        ConcorContributionResponseDTO actualResponse = concorService.getConcorContribution(concorContributionId);

        assertThat(actualResponse).isNull();
    }

    @Test
    void givenInvalidConcorContributionId_whenGetConcorContributionXmlIsCalled_thenEmptyResponseIsReturned() {
        when(concorRepository.findByIdIn(any())).thenReturn(new ArrayList<>());
        List<ConcorContributionResponse> actualResponse = concorService.getConcorContributionXml(List.of(1));
        assertThat(actualResponse).containsExactlyElementsOf(new ArrayList<>());
    }

    @Test
    void givenValidConcorContributionId_whenGetConcorContributionXmlIsCalled_thenExpectedResponseIsReturned() {
        List<ConcorContributionResponse> expectedResponse = List.of(ConcorContributionResponse.builder()
                .concorContributionId(1)
                .xmlContent(testXml)
                .build());

        when(concorRepository.findByIdIn(any())).thenReturn(List.of(TestEntityDataBuilder.getPopulatedConcorContributionsEntity(1, testXml)));
        List<ConcorContributionResponse> actualResponse = concorService.getConcorContributionXml(List.of(1));
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

}
