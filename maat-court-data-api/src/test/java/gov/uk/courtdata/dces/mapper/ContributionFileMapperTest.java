package gov.uk.courtdata.dces.mapper;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.dces.request.CreateContributionFileRequest;
import gov.uk.courtdata.dces.request.CreateFdcFileRequest;
import gov.uk.courtdata.dces.request.CreateFileRequest;
import gov.uk.courtdata.dces.response.ContributionFileErrorResponse;
import gov.uk.courtdata.dces.response.ContributionFileResponse;
import gov.uk.courtdata.entity.ContributionFileErrorsEntity;
import gov.uk.courtdata.entity.ContributionFilesEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
class ContributionFileMapperTest {
    private final ContributionFileMapper mapper = new ContributionFileMapperImpl();

    @Test
    void givenCreateFdcFileRequest_whenToContributionFileEntity_thenReturnMappedEntity() {
        CreateFdcFileRequest request = createFdcRequest();
        ContributionFilesEntity mapped = mapper.toContributionFileEntity(request);
        assertValidMappedObject(mapped, request);
    }

    @Test
    void givenCreateContributionFileRequest_whenToContributionFileEntity_thenReturnMappedEntity() {
        CreateContributionFileRequest request = createContributionRequest();
        ContributionFilesEntity mapped = mapper.toContributionFileEntity(request);
        assertValidMappedObject(mapped, request);

    }

    @Test
    void givenContributionFilesEntity_whenToContributionFileResponse_thenReturnMappedResponse() {
        var entity = TestEntityDataBuilder.getPopulatedContributionFilesEntity(99);
        var mapped = mapper.toContributionFileResponse(entity);
        assertValidMappedObject(mapped, entity);
    }

    @Test
    void givenContributionFileErrorsEntity_whenToContributionFileErrorResponse_thenReturnMappedErrorResponse() {
        var entity = TestEntityDataBuilder.getContributionFileErrorsEntity(99, 888);
        var mapped = mapper.toContributionFileErrorResponse(entity);
        assertValidMappedObject(mapped, entity);
    }

    private static void assertValidMappedObject(ContributionFilesEntity mapped, CreateFileRequest request) {
        assertAll(() -> assertThat(mapped.getXmlContent()).isEqualTo(request.getXmlContent()),
                () -> assertThat(mapped.getAckXmlContent()).isEqualTo(request.getAckXmlContent()),
                () -> assertThat(mapped.getRecordsSent()).isEqualTo(request.getRecordsSent()),
                () -> assertThat(mapped.getFileName()).isEqualTo(request.getXmlFileName()),
                // validate non-map-related fields are unset. No stray mappings
                () -> assertNonMappedAreNull(mapped.getFileId()),
                () -> assertNonMappedAreNull(mapped.getRecordsReceived()),
                () -> assertNonMappedAreNull(mapped.getDateCreated()),
                () -> assertNonMappedAreNull(mapped.getDateModified()),
                () -> assertNonMappedAreNull(mapped.getUserModified()),
                () -> assertNonMappedAreNull(mapped.getDateSent()),
                () -> assertNonMappedAreNull(mapped.getDateReceived()),
                // should have a default of DCES for the created
                () -> assertThat(mapped.getUserCreated()).isEqualTo("DCES")
        );
    }

    private static void assertNonMappedAreNull(Object fieldInMappedObject) {
        assertThat(fieldInMappedObject).isNull();
    }

    private static void assertValidMappedObject(ContributionFileResponse mapped, ContributionFilesEntity entity) {
        assertAll(() -> assertThat(mapped.getId()).isEqualTo(entity.getFileId()),
                () -> assertThat(mapped.getXmlFileName()).isEqualTo(entity.getFileName()),
                () -> assertThat(mapped.getRecordsSent()).isEqualTo(entity.getRecordsSent()),
                () -> assertThat(mapped.getRecordsReceived()).isEqualTo(entity.getRecordsReceived()),
                () -> assertThat(mapped.getDateCreated()).isEqualTo(entity.getDateCreated()),
                () -> assertThat(mapped.getUserCreated()).isEqualTo(entity.getUserCreated()),
                () -> assertThat(mapped.getDateModified()).isEqualTo(entity.getDateModified()),
                () -> assertThat(mapped.getUserModified()).isEqualTo(entity.getUserModified()),
                () -> assertThat(mapped.getXmlContent()).isEqualTo(entity.getXmlContent()),
                () -> assertThat(mapped.getDateSent()).isEqualTo(entity.getDateSent()),
                () -> assertThat(mapped.getDateReceived()).isEqualTo(entity.getDateReceived()),
                () -> assertThat(mapped.getAckXmlContent()).isEqualTo(entity.getAckXmlContent())
        );
    }

    private static void assertValidMappedObject(ContributionFileErrorResponse mapped, ContributionFileErrorsEntity entity) {
        assertAll(() -> assertThat(mapped.getContributionFileId()).isEqualTo(entity.getContributionFileId()),
                () -> assertThat(mapped.getContributionId()).isEqualTo(entity.getContributionId()),
                () -> assertThat(mapped.getRepId()).isEqualTo(entity.getRepId()),
                () -> assertThat(mapped.getErrorText()).isEqualTo(entity.getErrorText()),
                () -> assertThat(mapped.getFixAction()).isEqualTo(entity.getFixAction()),
                () -> assertThat(mapped.getFdcContributionId()).isEqualTo(entity.getFdcContributionId()),
                () -> assertThat(mapped.getConcorContributionId()).isEqualTo(entity.getConcorContributionId()),
                () -> assertThat(mapped.getDateCreated()).isEqualTo(entity.getDateCreated())
        );
    }

    private CreateContributionFileRequest createContributionRequest() {
        return CreateContributionFileRequest.builder()
                .concorContributionIds(Set.of(1))
                .xmlContent("<xml>content</xml>")
                .ackXmlContent("<ackXml>content</ackXml>")
                .recordsSent(1)
                .xmlFileName("testFilename.xml")
                .build();
    }

    private CreateFdcFileRequest createFdcRequest() {
        return CreateFdcFileRequest.builder()
                .fdcIds(Set.of(1))
                .xmlContent("<xml>content</xml>")
                .ackXmlContent("<ackXml>content</ackXml>")
                .recordsSent(1)
                .xmlFileName("testFilename.xml")
                .build();
    }
}
