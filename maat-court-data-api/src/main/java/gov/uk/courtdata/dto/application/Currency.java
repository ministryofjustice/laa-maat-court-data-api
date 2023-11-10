package gov.uk.courtdata.dto.application;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

public class Currency extends BigDecimal implements Serializable {

    private static final long serialVersionUID = -730899275611349938L;

    public Currency(char[] chars, int i, int i1) {
        super(chars, i, i1);
    }

    public Currency(char[] chars, int i, int i1, MathContext mathContext) {
        super(chars, i, i1, mathContext);
    }

    public Currency(char[] chars) {
        super(chars);
    }

    public Currency(char[] chars, MathContext mathContext) {
        super(chars, mathContext);
    }

    public Currency(String s) {
        super(s);
    }

    public Currency(String s, MathContext mathContext) {
        super(s, mathContext);
    }

    public Currency(double v) {
        super(v);
    }

    public Currency(double v, MathContext mathContext) {
        super(v, mathContext);
    }

    public Currency(BigInteger bigInteger) {
        super(bigInteger);
    }

    public Currency(BigInteger bigInteger, MathContext mathContext) {
        super(bigInteger, mathContext);
    }

    public Currency(BigInteger bigInteger, int i) {
        super(bigInteger, i);
    }

    public Currency(BigInteger bigInteger, int i, MathContext mathContext) {
        super(bigInteger, i, mathContext);
    }

    public Currency(int i) {
        super(i);
    }

    public Currency(int i, MathContext mathContext) {
        super(i, mathContext);
    }

    public Currency(long l) {
        super(l);
    }

    public Currency(long l, MathContext mathContext) {
        super(l, mathContext);
    }
}
