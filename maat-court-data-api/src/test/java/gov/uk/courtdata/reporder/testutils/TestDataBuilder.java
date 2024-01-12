package gov.uk.courtdata.reporder.testutils;

import gov.uk.courtdata.reporder.projection.IOJAssessorDetails;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestDataBuilder {

    public static final String REP_ORDER_CREATOR_NAME = "Karen Greaves";
    public static final String REP_ORDER_CREATOR_USER_NAME = "grea-k";

    public static IOJAssessorDetails getIOJAssessorDetails() {
        return new IOJAssessorDetails() {
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
