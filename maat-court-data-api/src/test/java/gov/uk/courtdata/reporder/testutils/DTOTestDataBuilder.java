package gov.uk.courtdata.reporder.testutils;

import gov.uk.courtdata.reporder.dto.AssessorDetails;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DTOTestDataBuilder {

    public static AssessorDetails getAssessorDetails() {
        return AssessorDetails.builder()
                .name("Karen Greaves")
                .userName("grea-k")
                .build();
    }
}
