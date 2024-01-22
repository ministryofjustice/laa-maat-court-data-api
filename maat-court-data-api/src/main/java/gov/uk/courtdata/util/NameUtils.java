package gov.uk.courtdata.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

@UtilityClass
public class NameUtils {

    /**
     * Given a number of names e.g. first, middle and last
     * Construct a concatenated full name with correct capitalisation
     * and leading and trailing spaces removed.
     * <p>
     * e.g. "linda ", " mcCormack" -> "Linda McCormack"
     *
     * @param names one or more of first, middle and last names
     * @return Concatenated and first letter capitalised full name.
     */
    public String toCapitalisedFullName(String... names) {

        return Arrays.stream(names)
                .map(StringUtils::trim)
                .filter(StringUtils::isNotBlank)
                .map(WordUtils::capitalize)
                .collect(Collectors.joining(StringUtils.SPACE));
    }
}
