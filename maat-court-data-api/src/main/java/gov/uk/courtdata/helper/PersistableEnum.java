package gov.uk.courtdata.helper;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface PersistableEnum<T> {
    @JsonIgnore
    T getValue();
}
