package gov.uk.courtdata.eformsApplication.builder;

import com.fasterxml.jackson.core.JsonProcessingException;
import gov.uk.courtdata.model.eformsApplication.EformsApplication;
import gov.uk.courtdata.model.eformsApplication.Offence;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EformsStagingBuilderTest {
    private static EformsStagingBuilder testBuilder;

    @BeforeAll
    public static void setUp() {
        testBuilder = new EformsStagingBuilder();
    }
    @Test
    void build() {
    }

    @Test
    void getOffenceType() {
        List<Offence> testOffences = List.of(
                Offence.builder().offenceClass("Class C").build(),
                Offence.builder().build(),
                Offence.builder().offenceClass("Class A").build()
                );

        String expectedResult = "Class A";

        assertEquals(expectedResult, testBuilder.getOffenceType(testOffences));
    }

    @Test
    void generateXmlString() throws JsonProcessingException {
        String output = testBuilder.generateXmlString(EformsApplication.builder().build());
        assertNotNull(output);
    }

    @Test
    void buildChargeList() {
    }
}