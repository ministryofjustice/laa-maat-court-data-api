package gov.uk.courtdata.dto.application;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SysGenString implements Comparable<SysGenString>, Serializable {

    private static final long serialVersionUID = 9184684438673245028L;

    private String value;

    public SysGenString() {}

    public SysGenString(String value) {
        setValue(value);
    }

    public int compareTo(SysGenString o) {
        return value != null ? value.compareTo(o.getValue()) : 1;
    }

    @Override
    public boolean equals(Object o) {
        return value != null && value.equals(o);
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
