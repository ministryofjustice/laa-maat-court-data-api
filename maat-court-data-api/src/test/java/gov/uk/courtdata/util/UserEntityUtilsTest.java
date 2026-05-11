package gov.uk.courtdata.util;

import static org.assertj.core.api.Assertions.assertThat;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.entity.UserEntity;

import org.junit.jupiter.api.Test;

class UserEntityUtilsTest {

    @Test
    void shouldSuccessfullyExtractFullNameFromUserEntity() {
        UserEntity userEntity = TestEntityDataBuilder.getUserEntity();

        String actualFullName = UserEntityUtils.extractFullName(userEntity);

        assertThat(actualFullName).isEqualTo("Karen Greaves");
    }
}
