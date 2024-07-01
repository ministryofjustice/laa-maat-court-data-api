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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class ContributionFileMapperTest {

    private static final ContributionFileMapper mapper = new ContributionFileMapperImpl();

    @Test
    void fdcMappingTest(){
        CreateFdcFileRequest request = createFdcRequest();
        ContributionFilesEntity mapped = mapper.toContributionFileEntity(request);
        assertValidMappedObject(mapped, request);
    }

    @Test
    void contributionMappingTest(){
        CreateContributionFileRequest request = createContributionRequest();
        ContributionFilesEntity mapped = mapper.toContributionFileEntity(request);
        assertValidMappedObject(mapped, request);

    }

    @Test
    void testEntityToContributionFileResponse() {
        var entity = TestEntityDataBuilder.getPopulatedContributionFilesEntity(99);
        var mapped = mapper.toContributionFileResponse(entity);
        assertValidMappedObject(mapped, entity);
    }

    @Test
    void testEntityToContributionFileErrorResponse() {
        var entity = TestEntityDataBuilder.getContributionFileErrorsEntity(99, 888);
        var mapped = mapper.toContributionFileErrorResponse(entity);
        assertValidMappedObject(mapped, entity);
    }

    private static void assertValidMappedObject(ContributionFilesEntity mapped, CreateFileRequest request){
        assertEquals(request.getXmlContent(), mapped.getXmlContent());
        assertEquals(request.getAckXmlContent(), mapped.getAckXmlContent());
        assertEquals(request.getRecordsSent(), mapped.getRecordsSent());
        assertEquals(request.getXmlFileName(), mapped.getFileName());
        // validate non-map-related fields are unset. No stray mappings.
        assertNonMappedAreNull(mapped.getFileId());
        assertNonMappedAreNull(mapped.getRecordsReceived());
        assertNonMappedAreNull(mapped.getDateCreated());
        assertNonMappedAreNull(mapped.getDateModified());
        assertNonMappedAreNull(mapped.getUserModified());
        assertNonMappedAreNull(mapped.getDateSent());
        assertNonMappedAreNull(mapped.getDateReceived());
        // should have a default of DCES for the created.
        assertEquals("DCES", mapped.getUserCreated());
    }

    private static void assertNonMappedAreNull(Object fieldInMappedObject){
        assertNull(fieldInMappedObject);
    }

    private static void assertValidMappedObject(ContributionFileResponse mapped, ContributionFilesEntity entity) {
        assertEquals(entity.getFileId(), mapped.getId());
        assertEquals(entity.getFileName(), mapped.getXmlFileName());
        assertEquals(entity.getRecordsSent(), mapped.getRecordsSent());
        assertEquals(entity.getRecordsReceived(), mapped.getRecordsReceived());
        assertEquals(entity.getDateCreated(), mapped.getDateCreated());
        assertEquals(entity.getUserCreated(), mapped.getUserCreated());
        assertEquals(entity.getDateModified(), mapped.getDateModified());
        assertEquals(entity.getUserModified(), mapped.getUserModified());
        assertEquals(entity.getXmlContent(), mapped.getXmlContent());
        assertEquals(entity.getDateSent(), mapped.getDateSent());
        assertEquals(entity.getDateReceived(), mapped.getDateReceived());
        assertEquals(entity.getAckXmlContent(), mapped.getAckXmlContent());
    }

    private static void assertValidMappedObject(ContributionFileErrorResponse mapped, ContributionFileErrorsEntity entity) {
        assertEquals(entity.getContributionFileId(), mapped.getContributionFileId());
        assertEquals(entity.getContributionId(), mapped.getContributionId());
        assertEquals(entity.getRepId(), mapped.getRepId());
        assertEquals(entity.getErrorText(), mapped.getErrorText());
        assertEquals(entity.getFixAction(), mapped.getFixAction());
        assertEquals(entity.getFdcContributionId(), mapped.getFdcContributionId());
        assertEquals(entity.getConcorContributionId(), mapped.getConcorContributionId());
        assertEquals(entity.getDateCreated(), mapped.getDateCreated());
    }

    private CreateContributionFileRequest createContributionRequest(){
        return CreateContributionFileRequest.builder()
                .concorContributionIds(Set.of(1))
                .xmlContent("<xml>content</xml>")
                .ackXmlContent("<ackXml>content</ackXml>")
                .recordsSent(1)
                .xmlFileName("testFilename.xml")
                .build();
    }

    private CreateFdcFileRequest createFdcRequest(){
        return CreateFdcFileRequest.builder()
                .fdcIds(Set.of(1))
                .xmlContent("<xml>content</xml>")
                .ackXmlContent("<ackXml>content</ackXml>")
                .recordsSent(1)
                .xmlFileName("testFilename.xml")
                .build();
    }

}
