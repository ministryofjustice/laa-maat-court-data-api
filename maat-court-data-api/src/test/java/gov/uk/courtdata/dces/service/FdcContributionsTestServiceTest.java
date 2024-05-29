package gov.uk.courtdata.dces.service;

import gov.uk.courtdata.dces.request.CreateFdcTestDataRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FdcContributionsTestServiceTest {

    @InjectMocks
    FdcContributionsTestService fdcContributionsTestService;


    @Test
    void testPositiveTestDataGeneration(){
//        fdcContributionsTestService.createFdcMergeTestData(createTestDataRequest());
        var i =0;
    }

    private CreateFdcTestDataRequest createTestDataRequest(){
        return CreateFdcTestDataRequest.builder()
                .numOfTestEntries(5)
                .negativeTest(false)
                .negativeTestType(null)
                .build();

    }

}