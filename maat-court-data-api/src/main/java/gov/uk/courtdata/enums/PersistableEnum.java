package gov.uk.courtdata.enums;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface PersistableEnum<T> {
    @JsonIgnore
    T getValue();
}
