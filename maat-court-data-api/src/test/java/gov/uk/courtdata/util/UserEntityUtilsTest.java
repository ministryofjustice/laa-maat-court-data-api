package gov.uk.courtdata.util;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.entity.UserEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityUtilsTest {

    @Test
    void shouldSuccessfullyExtractFullNameFromUserEntity() {
        UserEntity userEntity = TestEntityDataBuilder.getUserEntity();

        String actualFullName = UserEntityUtils.extractFullName(userEntity);

        assertEquals("Karen Greaves", actualFullName);
    }
}