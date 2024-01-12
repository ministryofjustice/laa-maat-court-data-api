package gov.uk.courtdata.reporder.testutils;

import gov.uk.courtdata.reporder.dto.IOJAssessorDetails;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestDataBuilder {

    public static final String REP_ORDER_CREATOR_NAME = "Karen Greaves";
    public static final String REP_ORDER_CREATOR_USER_NAME = "grea-k";

    public static IOJAssessorDetails getIOJAssessorDetails() {
        return IOJAssessorDetails.builder()
                .name(REP_ORDER_CREATOR_NAME)
                .userName(REP_ORDER_CREATOR_USER_NAME)
                .build();
    }
}
