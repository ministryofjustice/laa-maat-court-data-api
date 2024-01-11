package gov.uk.courtdata.reporder.testutils;

import gov.uk.courtdata.reporder.dto.AssessorDetails;
import gov.uk.courtdata.reporder.projection.RepOrderCreatorDetails;
import lombok.AllArgsConstructor;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestDataBuilder {

    public static final String REP_ORDER_CREATOR_NAME = "Karen Greaves";
    public static final String REP_ORDER_CREATOR_USER_NAME = "grea-k";

    public static AssessorDetails getAssessorDetails() {
        return AssessorDetails.builder()
                .name(REP_ORDER_CREATOR_NAME)
                .userName(REP_ORDER_CREATOR_USER_NAME)
                .build();
    }

    public static RepOrderCreatorDetails getRepOrderCreatorDetails() {
        return new RepOrderCreatorDetails() {
            @Override
            public String getName() {
                return REP_ORDER_CREATOR_NAME;
            }

            @Override
            public String getUserName() {
                return REP_ORDER_CREATOR_USER_NAME;
            }
        };
    }
}
