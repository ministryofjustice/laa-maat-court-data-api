package gov.uk.courtdata.builder;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DataBuilderUtil {
    public static BigDecimal createScaledBigDecimal(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
    }
}
