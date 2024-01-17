package gov.uk.courtdata.util;

import gov.uk.courtdata.entity.UserEntity;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@UtilityClass
public class UserEntityUtils {

    /**
     * Determine the users full name and format it conventionally.
     * e.g. "Carol Britton"
     * @param userEntity containing the users names.
     * @return The user's full name, or an empty String.
     */
    public String extractFullName(UserEntity userEntity) {
        if (Objects.isNull(userEntity)) {
            return StringUtils.EMPTY;
        }

        String firstName = userEntity.getFirstName();
        String surname = userEntity.getSurname();
        return NameUtils.toCapitalisedFullName(firstName, surname);

    }
}
