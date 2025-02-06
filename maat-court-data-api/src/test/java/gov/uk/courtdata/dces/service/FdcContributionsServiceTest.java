package gov.uk.courtdata.dces.service;

import gov.uk.courtdata.dces.mapper.ContributionFileMapper;
import gov.uk.courtdata.dces.request.CreateFdcContributionRequest;
import gov.uk.courtdata.dces.request.CreateFdcFileRequest;
import gov.uk.courtdata.dces.request.LogFdcProcessedRequest;
import gov.uk.courtdata.dces.request.UpdateFdcContributionRequest;
import gov.uk.courtdata.dces.response.FdcContributionEntry;
import gov.uk.courtdata.dces.response.FdcContributionsGlobalUpdateResponse;
import gov.uk.courtdata.dces.response.FdcContributionsResponse;
import gov.uk.courtdata.entity.ContributionFileErrorsEntity;
import gov.uk.courtdata.entity.ContributionFilesEntity;
import gov.uk.courtdata.entity.FdcContributionsEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.enums.FdcContributionsStatus;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.repository.ContributionFileErrorsRepository;
import gov.uk.courtdata.repository.ContributionFilesRepository;
import gov.uk.courtdata.repository.FdcContributionsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static gov.uk.courtdata.enums.FdcContributionsStatus.REQUESTED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FdcContributionsServiceTest {
    private List<FdcContributionsEntity> fdcContributionsEntityList;


    @InjectMocks
    private FdcContributionsService fdcContributionsService;

    @Mock
    private ContributionFileMapper contributionFileMapper;
    @Mock
    private DebtCollectionRepository debtCollectionRepository;
    @Mock
    private DebtCollectionService debtCollectionService;
    @Mock
    private FdcContributionsRepository fdcContributionsRepository;
    @Mock
    private ContributionFilesRepository contributionFilesRepository;
    @Mock
    private ContributionFileErrorsRepository contributionFileErrorsRepository;

    private LocalDate expectedDateCalculated;
    private LocalDate expectedSentenceDate;
    private final BigDecimal expectedAgfsCost = new BigDecimal("100.50");
    private final BigDecimal expectedLgfsCost = new BigDecimal("333.33");
    private final BigDecimal expectedFinalCost = new BigDecimal("1010.10");
    private final Integer expectedId = 111;
    private final Integer expectedMaatId = 999;
    private FdcContributionEntry expectedEntry;

    @Captor
    private ArgumentCaptor<FdcContributionsStatus> statusCaptor;
    @Captor
    private ArgumentCaptor<ContributionFileErrorsEntity> fileErrorCaptor;

    @BeforeEach
    void setUp() {
        expectedDateCalculated = LocalDate.now();
        expectedSentenceDate = LocalDate.now().minusDays(1);
        getFdcContributionsFile();
        populateExpectedEntry();
    }

    @Test
    void givenRequestedStatus_whenGetFdcContributions_thenReturnListOfContributions() {
        when(debtCollectionRepository.findFdcEntriesByStatus(statusCaptor.capture())).thenReturn(fdcContributionsEntityList);

        FdcContributionsResponse response = fdcContributionsService.getFdcContributions(REQUESTED);

        assertThat(response).isNotNull();
        final FdcContributionsStatus capturedStatus = statusCaptor.getValue();
        assertThat(capturedStatus).isEqualTo(REQUESTED);

        assertEqualsWithExpectedValues(response);
    }

    @Test
    void givenNonExistentIds_whenGetFdcContributions_thenReturnEmptyList() {
        when(debtCollectionRepository.findFdcEntriesByIdIn(any())).thenReturn(new ArrayList<>());
        FdcContributionsResponse response = fdcContributionsService.getFdcContributions(List.of(1));
        assertThat(response.getFdcContributions()).isEqualTo(new ArrayList<>());
    }

    @Test
    void givenValidIds_whenGetFdcContributions_thenReturnListOfContributions() {
        when(debtCollectionRepository.findFdcEntriesByIdIn(any())).thenReturn(fdcContributionsEntityList);
        FdcContributionsResponse response = fdcContributionsService.getFdcContributions(List.of(1));
        assertEqualsWithExpectedValues(response);
    }

    @Test
    void givenValidRequest_whenFdcContributionGlobalUpdate_thenReturnNumberOfUpdates() {
        int expected1 = 2;
        int expected2 = 5;

        when(debtCollectionRepository.setEligibleForFdcDelayedPickup("5")).thenReturn(expected1);
        when(debtCollectionRepository.setEligibleForFdcFastTracking("5")).thenReturn(expected2);
        when(fdcContributionsRepository.callGetFdcCalculationDelay()).thenReturn("5");
        FdcContributionsGlobalUpdateResponse response = fdcContributionsService.fdcContributionGlobalUpdate();
        assertThat(response).isNotNull();
        assertThat(response.getNumberOfUpdates()).isEqualTo(expected1+expected2);
    }

    @Test
    void givenValidRequest_whenCreateContributionFileAndUpdateFdcStatus_thenReturnUpdatedFdcStatus() {
        // setup
        CreateFdcFileRequest request = createFdcFileRequest();
        ContributionFilesEntity mappedEntity = createContributionsFileEntity();

        when(debtCollectionRepository.saveContributionFilesEntity(mappedEntity)).thenReturn(true);
        when(fdcContributionsRepository.findByIdIn(request.getFdcIds())).thenReturn(fdcContributionsEntityList);
        when(fdcContributionsRepository.saveAll(fdcContributionsEntityList)).thenReturn(fdcContributionsEntityList);

        when(contributionFileMapper.toContributionFileEntity(request)).thenReturn(mappedEntity);
        // run
        assertThat(fdcContributionsService.createContributionFileAndUpdateFdcStatus(request)).isNotNull();
        // test
        verify(fdcContributionsRepository).findByIdIn(any());
        verify(fdcContributionsRepository).saveAll(any());
        verify(fdcContributionsRepository).findByIdIn(any());
    }

    @Test
    void givenNullRequest_whenCreateContributionFileAndUpdateFdcStatus_thenThrowValidationException() {
        ValidationException e = assertThrows(ValidationException.class,() -> fdcContributionsService.createContributionFileAndUpdateFdcStatus(null));
        assertThat(e.getMessage()).isEqualTo("fdcRequest object is null");
    }

    @Test
    void givenNullFdcIds_whenCreateContributionFileAndUpdateFdcStatus_thenThrowValidationException() {
        CreateFdcFileRequest request = createFdcFileRequest();
        request.setFdcIds(null);
        ValidationException e = assertThrows(ValidationException.class,() -> fdcContributionsService.createContributionFileAndUpdateFdcStatus(request));
        assertThat(e.getMessage()).isEqualTo("FdcIds is empty/null.");
    }

    @Test
    void givenEmptyFdcIds_whenCreateContributionFileAndUpdateFdcStatus_thenThrowValidationException() {
        CreateFdcFileRequest request = createFdcFileRequest();
        request.setFdcIds(Set.of());
        ValidationException e = assertThrows(ValidationException.class,() -> fdcContributionsService.createContributionFileAndUpdateFdcStatus(request));
        assertThat(e.getMessage()).isEqualTo("FdcIds is empty/null.");
    }

    @Test
    void givenValidRequestAndNoErrorText_whenLogFdcProcessed_thenUpdateContributionFileReceivedCount() {
        int id = 123;
        int repId = 456;
        int fileId = 10000;

        FdcContributionsEntity concorEntity = createFdcContributionsEntity(id, repId, fileId);

        when(fdcContributionsRepository.findById(id)).thenReturn(Optional.of(concorEntity));
        when(debtCollectionService.updateContributionFileReceivedCount(fileId)).thenReturn(true);
        // do
        fdcContributionsService.logFdcProcessed(createLogFdcProcessedRequest(id, ""));
        // verify
        verify(fdcContributionsRepository).findById(id);
        verify(debtCollectionService).updateContributionFileReceivedCount(fileId);
        verify(contributionFileErrorsRepository, never()).save(any());
    }

    @Test
    void givenValidRequestAndErrorText_whenLogFdcProcessed_thenSaveErrorEntity() {
        int id = 123;
        int repId = 456;
        int fileId = 10000;
        String errorText = "Error Text";
        LocalDateTime currDate = LocalDateTime.now();

        FdcContributionsEntity fdcEntity = createFdcContributionsEntity(id, repId, fileId);

        when(fdcContributionsRepository.findById(id)).thenReturn(Optional.of(fdcEntity));

        doNothing().when(debtCollectionService).saveError(fileErrorCaptor.capture());
        // do
        fdcContributionsService.logFdcProcessed(createLogFdcProcessedRequest(id, errorText));
        // verify
        verify(fdcContributionsRepository).findById(id);
        verify(debtCollectionService, never()).updateContributionFileReceivedCount(fileId);
        ContributionFileErrorsEntity errorEntity = fileErrorCaptor.getValue();

        assertThat(errorEntity.getErrorText()).isEqualTo(errorText);
        assertThat(errorEntity.getFdcContributionId()).isEqualTo(id);
        assertThat(errorEntity.getContributionId()).isEqualTo(id);
        assertThat(errorEntity.getContributionFileId()).isEqualTo(fileId);
        assertThat(errorEntity.getConcorContributionId()).isNull();
        assertThat(errorEntity.getRepId()).isEqualTo(repId);
        assertThat(errorEntity.getDateCreated().getDayOfMonth()).isEqualTo(currDate.getDayOfMonth());
        assertThat(errorEntity.getDateCreated().getMonth()).isEqualTo(currDate.getMonth());
        assertThat(errorEntity.getDateCreated().getYear()).isEqualTo(currDate.getYear());
    }

    @Test
    void givenValidRequest_whenCreateFdcContribution_thenReturnCreatedContribution() {
        ArgumentCaptor<FdcContributionsEntity> captor = ArgumentCaptor.forClass(FdcContributionsEntity.class);

        CreateFdcContributionRequest request = CreateFdcContributionRequest.builder()
                .repId(11212)
                .lgfsComplete("N")
                .agfsComplete("Y")
                .status(REQUESTED)
                .build();

        FdcContributionsEntity entity = FdcContributionsEntity.builder()
                .id(1)
                .repOrderEntity(RepOrderEntity.builder().id(1).build())
                .lgfsComplete(request.getLgfsComplete())
                .agfsComplete(request.getAgfsComplete())
                .status(request.getStatus())
                .build();

        when(fdcContributionsRepository.save(any(FdcContributionsEntity.class))).thenReturn(entity);

        FdcContributionsEntity result = fdcContributionsService.createFdcContribution(request);

        verify(fdcContributionsRepository).save(captor.capture());
        FdcContributionsEntity capturedEntity = captor.getValue();

        assertThat(result).isNotNull();
        assertThat(capturedEntity.getRepOrderEntity().getId()).isEqualTo(request.getRepId());
        assertThat(capturedEntity.getLgfsComplete()).isEqualTo(request.getLgfsComplete());
        assertThat(capturedEntity.getAgfsComplete()).isEqualTo(request.getAgfsComplete());
        assertThat(capturedEntity.getStatus()).isEqualTo(request.getStatus());
    }

    @Test
    void givenDatabaseError_whenCreateFdcContribution_thenThrowRuntimeException() {
        CreateFdcContributionRequest request = CreateFdcContributionRequest.builder().build();

        when(fdcContributionsRepository.save(any(FdcContributionsEntity.class))).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> fdcContributionsService.createFdcContribution(request));

        assertThat(exception.getMessage()).isEqualTo("Database error");
    }

    @Test
    void givenValidRequest_whenUpdateFdcContribution_thenReturnUpdatedCount() {
        UpdateFdcContributionRequest request = getUpdateFdcContributionRequest();

        when(fdcContributionsRepository.updateStatus(anyInt(), anyString(), any(), any())).thenReturn(1);

        Integer result = fdcContributionsService.updateFdcContribution(request);
        assertThat(result).isEqualTo(1);
    }

    @Test
    void givenNonExistentIds_whenUpdateFdcContribution_thenReturnZeroUpdatedCount() {
        UpdateFdcContributionRequest request = getUpdateFdcContributionRequest();

        when(fdcContributionsRepository.updateStatus(anyInt(), anyString(), any(), any())).thenReturn(0);

        Integer result = fdcContributionsService.updateFdcContribution(request);
        assertThat(result).isEqualTo(0);
    }

    @Test
    void givenDatabaseError_whenUpdateFdcContribution_thenThrowRuntimeException() {
        UpdateFdcContributionRequest request = getUpdateFdcContributionRequest();

        when(fdcContributionsRepository.updateStatus(anyInt(), anyString(), any(), any())).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class,
                () -> fdcContributionsService.updateFdcContribution(request));

        assertThat(exception.getMessage()).isEqualTo("Database error");
    }

    private UpdateFdcContributionRequest getUpdateFdcContributionRequest() {
        return UpdateFdcContributionRequest.builder()
                .repId(1)
                .newStatus(FdcContributionsStatus.SENT)
                .previousStatus(REQUESTED)
                .build();
    }


    @Test
    void givenNonExistentFdcId_whenLogFdcProcessed_thenThrowRequestedObjectNotFoundException() {
        int id = 123;
        String errorText = "Error Text";

        when(fdcContributionsRepository.findById(id)).thenReturn(Optional.empty());
        // do
        assertThrows(RequestedObjectNotFoundException.class, () ->
                fdcContributionsService.logFdcProcessed(createLogFdcProcessedRequest(id, errorText)));
        // verify
        verify(fdcContributionsRepository).findById(id);
        verify(contributionFilesRepository, never()).findById(any());
        verify(contributionFilesRepository, never()).save(any());
        verify(contributionFileErrorsRepository, never()).save(any());
    }

    @Test
    void givenNonExistentFileId_whenLogFdcProcessed_thenThrowNoSuchElementException() {
        int id = 123;
        int repId = 456;
        int fileId = 10000;

        FdcContributionsEntity fdcEntity = createFdcContributionsEntity(id, repId, fileId);
        when(fdcContributionsRepository.findById(id)).thenReturn(Optional.of(fdcEntity));
        // do
        assertThrows(NoSuchElementException.class, () ->
                fdcContributionsService.logFdcProcessed(createLogFdcProcessedRequest(id, "")));
        // verify
        verify(fdcContributionsRepository).findById(id);
        verify(debtCollectionService).updateContributionFileReceivedCount(any());
        // verify no error is saved, as file is not found.
        verify(debtCollectionService, never()).saveError(any());
    }

    private LogFdcProcessedRequest createLogFdcProcessedRequest(int id, String errorText) {
        return LogFdcProcessedRequest.builder()
                .fdcId(id)
                .errorText(errorText)
                .build();
    }

    private static FdcContributionsEntity createFdcContributionsEntity(int id, int repId, int fileId) {
        RepOrderEntity repOrderEntity = RepOrderEntity.builder()
                .id(repId)
                .build();

        return FdcContributionsEntity.builder()
                .id(id)
                .repOrderEntity(repOrderEntity)
                .contFileId(fileId)
                .build();
    }

    private CreateFdcFileRequest createFdcFileRequest() {
        return CreateFdcFileRequest.builder()
                .xmlFileName("filename.xml")
                .xmlContent("<xml></xml>")
                .ackXmlContent("<ackXml></ackXml>")
                .fdcIds(Set.of(1))
                .build();
    }

    private ContributionFilesEntity createContributionsFileEntity() {
        LocalDate date = LocalDate.now();
        return ContributionFilesEntity.builder()
                .fileId(1)
                .fileName("filename.xml")
                .recordsSent(1)
                .recordsReceived(1)
                .dateCreated(date)
                .userCreated("DCES")
                .dateSent(date)
                .userModified("DCES")
                .xmlContent("<xml>test</xml>")
                .dateModified(date)
                .dateModified(date)
                .ackXmlContent("<xml>ackXML<xml>")
                .build();
    }

    private void getFdcContributionsFile() {
        fdcContributionsEntityList = new ArrayList<>();
        RepOrderEntity repOrderEntity = new RepOrderEntity();
        repOrderEntity.setId(expectedMaatId);
        repOrderEntity.setSentenceOrderDate(expectedSentenceDate);
        FdcContributionsEntity fdcFile = new FdcContributionsEntity();
        fdcFile.setStatus(REQUESTED);
        fdcFile.setId(expectedId);
        fdcFile.setAgfsCost(expectedAgfsCost);
        fdcFile.setLgfsCost(expectedLgfsCost);
        fdcFile.setFinalCost(expectedFinalCost);
        fdcFile.setDateCalculated(expectedDateCalculated);
        fdcFile.setRepOrderEntity(repOrderEntity);
        fdcContributionsEntityList.add(fdcFile);
    }

    private void populateExpectedEntry() {
        expectedEntry = FdcContributionEntry.builder()
            .id(expectedId)
            .maatId(expectedMaatId)
            .finalCost(expectedFinalCost)
            .agfsCost(expectedAgfsCost)
            .lgfsCost(expectedLgfsCost)
            .dateCalculated(expectedDateCalculated)
            .sentenceOrderDate(expectedSentenceDate)
            .build();
    }


    private void assertEqualsWithExpectedValues(FdcContributionsResponse response) {
        List<FdcContributionEntry> fdcContributionEntries = response.getFdcContributions();
        assertThat(fdcContributionEntries.isEmpty()).isFalse();
        FdcContributionEntry responseValue = fdcContributionEntries.get(0);
        FdcContributionsEntity expectedValue = fdcContributionsEntityList.get(0);
        assertThat(responseValue.getId()).isEqualTo(expectedValue.getId());
        assertThat(responseValue.getFinalCost()).isEqualTo(expectedValue.getFinalCost());
        assertThat(responseValue.getAgfsCost()).isEqualTo(expectedValue.getAgfsCost());
        assertThat(responseValue.getLgfsCost()).isEqualTo(expectedValue.getLgfsCost());
        assertThat(responseValue.getDateCalculated()).isEqualTo(expectedValue.getDateCalculated());
        assertThat(responseValue.getSentenceOrderDate()).isEqualTo(expectedValue.getRepOrderEntity().getSentenceOrderDate());
    }

    @Test
    void testGetFdcContribution() {
        when(fdcContributionsRepository.findById(expectedId)).thenReturn(Optional.of(fdcContributionsEntityList.get(0)));

        FdcContributionEntry result = fdcContributionsService.getFdcContribution(expectedId);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expectedEntry);
    }

    @Test
    void testGetFdcContributionWhenIdDoesNotExist() {
        Integer fdcContributionId = 1;

        when(fdcContributionsRepository.findById(fdcContributionId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RequestedObjectNotFoundException.class,
                () -> fdcContributionsService.getFdcContribution(fdcContributionId));

        assertThat(exception.getMessage()).isEqualTo("fdc_contribution could not be found by id");
    }
}
