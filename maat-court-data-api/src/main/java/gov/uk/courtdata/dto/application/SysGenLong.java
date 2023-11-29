package gov.uk.courtdata.dto.application;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: MASK-P
 * Date: 31-Mar-2010
 * Time: 14:18:06
 */
public class SysGenLong implements Comparable<SysGenLong>, Serializable {

    private static final long serialVersionUID = 9197249140954462724L;
    private Long value;

    public SysGenLong() {
    }

    public SysGenLong(Long value) {
        setValue(value);
    }

    public boolean isEmpty() {
        return (value != null && value > 0) ? false : true;
    }
    
    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public int compareTo(SysGenLong o) {
        return value != null ? value.compareTo(o.getValue()) : 1;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        SysGenLong that = (SysGenLong) o;

        if(value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
