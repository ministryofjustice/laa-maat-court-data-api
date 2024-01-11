package gov.uk.courtdata.reporder.testutils;

import gov.uk.courtdata.reporder.dto.AssessorDetails;
import gov.uk.courtdata.reporder.projection.RepOrderCreatorDetails;
import lombok.AllArgsConstructor;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestDataBuilder {

    public static AssessorDetails getAssessorDetails() {
        return AssessorDetails.builder()
                .name("Karen Greaves")
                .userName("grea-k")
                .build();
    }

    public static RepOrderCreatorDetails getRepOrderCreatorDetails() {
        return new RepOrderCreatorDetailsStub("Karen Greaves","grea-k");
    }

    @AllArgsConstructor
    private static final class RepOrderCreatorDetailsStub implements RepOrderCreatorDetails{

        private final String name;
        private final String userName;

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getUserName() {
            return userName;
        }
    }
}
