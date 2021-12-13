package gov.uk.courtdata.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum Frequency {

    @SerializedName("WEEKLY")
    WEEKLY("WEEKLY"),

    @SerializedName("2WEEKLY")
    TWO_WEEKLY("2WEEKLY"),

    @SerializedName("4WEEKLY")
    FOUR_WEEKLY("4WEEKLY"),

    @SerializedName("MONTHLY")
    MONTHLY("MONTHLY"),

    @SerializedName("ANNUALLY")
    ANNUALLY("ANNUALLY");

    private String code;

    @Override
    public String toString() {
        return code;
    }

    @JsonCreator
    public static Frequency fromCode(String code) {
        if (code == null) return null;

        return Stream.of(Frequency.values())
                .filter(f -> f.code.equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
