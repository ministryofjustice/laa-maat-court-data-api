package gov.uk.courtdata.dto.application;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;
import java.math.MathContext;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SysGenCurrency extends Currency implements Serializable {

    private static final long serialVersionUID = 4441570960123040475L;

    public SysGenCurrency() {
        super();

    }

    public SysGenCurrency(char[] chars, int i, int i1) {
        super(chars, i, i1);
    }

    public SysGenCurrency(char[] chars, int i, int i1, MathContext mathContext) {
        super(chars, i, i1, mathContext);
    }

    public SysGenCurrency(char[] chars) {
        super(chars);
    }

    public SysGenCurrency(char[] chars, MathContext mathContext) {
        super(chars, mathContext);
    }

    public SysGenCurrency(String s) {
        super(s);
    }

    public SysGenCurrency(String s, MathContext mathContext) {
        super(s, mathContext);
    }

    public SysGenCurrency(double v) {
        super(v);
    }

    public SysGenCurrency(double v, MathContext mathContext) {
        super(v, mathContext);
    }

    public SysGenCurrency(BigInteger bigInteger) {
        super(bigInteger);
    }

    public SysGenCurrency(BigInteger bigInteger, MathContext mathContext) {
        super(bigInteger, mathContext);
    }

    public SysGenCurrency(BigInteger bigInteger, int i) {
        super(bigInteger, i);
    }

    public SysGenCurrency(BigInteger bigInteger, int i, MathContext mathContext) {
        super(bigInteger, i, mathContext);
    }

    public SysGenCurrency(int i) {
        super(i);
    }

    public SysGenCurrency(int i, MathContext mathContext) {
        super(i, mathContext);
    }

    public SysGenCurrency(long l) {
        super(l);
    }

    public SysGenCurrency(long l, MathContext mathContext) {
        super(l, mathContext);
    }
}