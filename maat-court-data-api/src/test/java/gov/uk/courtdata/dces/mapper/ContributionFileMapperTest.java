package gov.uk.courtdata.dces.mapper;

import gov.uk.courtdata.dces.request.CreateContributionFileRequest;
import gov.uk.courtdata.dces.request.CreateFdcFileRequest;
import gov.uk.courtdata.dces.request.CreateFileRequest;
import gov.uk.courtdata.entity.ContributionFilesEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ContributionFileMapperTest {

    private static final ContributionFileMapper mapper = new ContributionFileMapperImpl();

    @Test
    void fdcMappingTest(){
        CreateFdcFileRequest request = createFdcRequest();
        ContributionFilesEntity mapped = mapper.toContributionFileEntity(request);
        assertTrue(validateMappedObject(mapped, request),"Mapping the fdc request was incorrect.");
    }

    @Test
    void contributionMappingTest(){
        CreateContributionFileRequest request = createContributionRequest();
        ContributionFilesEntity mapped = mapper.toContributionFileEntity(request);
        assertTrue(validateMappedObject(mapped, request),"Mapping the fdc request was incorrect.");

    }

    private boolean validateMappedObject(ContributionFilesEntity mapped, CreateFileRequest request){
        assertEquals(request.getXmlContent(), mapped.getXmlContent());
        assertEquals(request.getAckXmlContent(), mapped.getAckXmlContent());
        assertEquals(request.getRecordsSent(), mapped.getRecordsSent());
        assertEquals(request.getXmlFileName(), mapped.getFileName());
        // validate non-map-related fields are unset. No stray mappings.
        assertTrue(assertNonMappedAreNull(mapped.getId()));
        assertTrue(assertNonMappedAreNull(mapped.getId()));
        assertTrue(assertNonMappedAreNull(mapped.getDateReceived()));
        assertTrue(assertNonMappedAreNull(mapped.getDateCreated()));
        assertTrue(assertNonMappedAreNull(mapped.getDateModified()));
        assertTrue(assertNonMappedAreNull(mapped.getUserModified()));
        assertTrue(assertNonMappedAreNull(mapped.getDateSent()));
        assertTrue(assertNonMappedAreNull(mapped.getDateReceived()));
        // should have a default of DCES for the created.
        assertEquals("DCES", mapped.getUserCreated());
        return true;
    }

    private boolean assertNonMappedAreNull(Object o){
        return Objects.isNull(o);
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
