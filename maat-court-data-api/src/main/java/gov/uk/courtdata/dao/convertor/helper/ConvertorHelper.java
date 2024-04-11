package gov.uk.courtdata.dao.convertor.helper;

import gov.uk.courtdata.dto.application.*;
import uk.gov.justice.laa.crime.util.DateUtil;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;


/**
 * This class is the core of any type conversion for dao being moved between oracleTypes and
 * java DTO objects.
 * <br>
 * It provides static methods to provide any mappings required
 * <br>
 * Note that there are additional null-safe utilities in the package
 * ccmt-web/src/main/java/uk/gov/lsc/maat/web/util
 * e.g. NullSafeMathsUtils
 *
 * @author SWAN-D
 */
public class ConvertorHelper {


    /**
     * Converts a String from the oracleType to the java type.
     * Although trivial as they are both represents by java String,
     * having a standardised conversion method helps in refactoring the desing
     * later to provide automatic generation of the DTO classes
     *
     * @param str
     * @return
     */
    public String toString(String str) {
        return (str != null) ? str : "";
    }

    public SysGenString toSysGenString(String str) {
        return new SysGenString((str != null) ? str : "");
    }

    public String toSysGenString(SysGenString str) {
        return (str != null && str.getValue() != null) ? str.getValue() : "";
    }

    /**
     * converts a string value to boolean. In the database type boolean
     * values are stored as "y" or "n". This method will test for "y" and
     * return true in that case. Any other value is fales. The test is case
     * insensitive.
     * <br>
     * <b>Note</b> In the attempt to unify the java boolean with the y/n of the database
     * the case of the database being null was overlooked. Evaluation of the application
     * would indicate that it is reasonable to map a null db value to java boolean false
     * <br>
     * @param booleanString
     * @return public  boolean toBoolean( String booleanString )
    {
    return ( booleanString != null )?  booleanString.equalsIgnoreCase( "Y" ) : false;
    }
     */

    /**
     * converts a string value to boolean. In the database type boolean
     * values are stored as "y" or "n". This method will test for "y" and
     * return true in that case. Any other value is fales. The test is case
     * insensitive.
     * <br>
     * <b>Note</b> In the attempt to unify the java boolean with the y/n of the database
     * the case of the database being null was overlooked. Evaluation of the application
     * would indicate that it is reasonable to map a null db value to java boolean false
     * <br>
     *
     * @param booleanString
     * @return
     */
    public Boolean toBoolean(String booleanString) {
        return Boolean.valueOf((booleanString != null) ? booleanString.equalsIgnoreCase("Y") : false);
    }

    /**
     * converts a boolean value to a string representation. In the database type boolean
     * values are stored as "y" or "n". This method will test for true or false and
     * returns "Y" for true and "N" for false.
     * <br>
     *
     * @param booleanString
     * @return
     */
    public String toBoolean(boolean flag) {
        return (flag) ? "Y" : "N";
    }

    /**
     * converts a boolean value to a string representation. In the database type boolean
     * values are stored as "y" or "n". This method will test for true or false and
     * returns "Y" for true and "N" for false.
     * <br>
     *
     * @param booleanString
     * @return
     */
    public String toBoolean(Boolean flag) {
        return (flag != null) ? ((flag.booleanValue()) ? "Y" : "N") : "N";
    }

    /**
     * Method to convert a BigDecimal from the oracle type to
     * a native java int. Note there is no check for numeric
     * overflow. If the BigDecmial is numerically outside of the
     * range for int, spurious results may be returned. This can
     * be avoided by ensuring the numeric rang in the database.
     * <br>
     *
     * @param number
     * @return
     */
    public int toInt(BigDecimal number) {

        return (number != null) ? number.intValue() : 0;
    }

    /**
     * Method to convert an int from the dto to a BigDecimal to be
     * compliant with the OracleType object.
     * <br>
     *
     * @param number
     * @return
     */
    public BigDecimal toInteger(Integer number) {
        return (number != null) ? new BigDecimal(number) : null;
    }

    /**
     * Method to convert a BigDecimal from the oracle type to
     * a native java int. Note there is no check for numeric
     * overflow. If the BigDecmial is numerically outside of the
     * range for int, spurious results may be returned. This can
     * be avoided by ensuring the numeric rang in the database.
     * <br>
     *
     * @param number
     * @return
     */
    public Integer toInteger(BigDecimal number) {

        return (number != null) ?  number.intValue() : null;
    }

    /**
     * Method to convert an int from the dto to a BigDecimal to be
     * compliant with the OracleType object.
     * <br>
     *
     * @param number
     * @return
     */
    public BigDecimal toInt(int number) {
        return new BigDecimal(number);
    }

    public BigDecimal toLong(Long number) {
        return (number != null) ? new BigDecimal(number) : null;
    }

    public Long toLong(BigDecimal number) {

        return (number != null) ? number.longValue() : null;
    }

    public BigDecimal toSysGenLong(SysGenLong number) {
        return (number != null && number.getValue() != null) ? new BigDecimal(number.getValue()) : null;
    }

    public SysGenLong toSysGenLong(BigDecimal number) {

        return new SysGenLong(number != null ? number.longValue() : null);
    }

    public BigDecimal toDouble(Double number) {
        // BigDecimal constructor taking Doubles should be avoided as to introduces 'noise'
        // safer to use the BigDecimal(String) constructor.
        // i.e.
        // double with value of 0.1 when passed to BigDecimal constructor as a double results in a
        // BigDecimal value of  0.1000000000000000055511151231257827021181583404541015625
        // However when the String based constructor is used:
        // BigDecimal value of  0.1
        //return (number != null)?  new BigDecimal( number ) : null;
        // Gjl ... sir150954... investigation.

        return (number != null) ? new BigDecimal(number.toString()) : null;
    }

    public Double toDouble(BigDecimal number) {
        return (number != null) ? number.doubleValue() : 0.0d;
    }

    public BigDecimal toCurrency(Currency number) {
        return number;
    }

    public Currency toCurrency(BigDecimal number) {
        return number != null ? new Currency(number.toString()) : null;

    }

    public Timestamp toTimestamp(ZonedDateTime localDateTime) {
        return (localDateTime != null) ? Timestamp.valueOf(localDateTime.toLocalDateTime()) : null;
    }

    public ZonedDateTime toZonedDateTime(Timestamp timeStamp) {
        return (timeStamp != null) ? DateUtil.toZonedDateTime(timeStamp.toLocalDateTime()) : null;
    }

    public Date toDate(Timestamp timeStamp) {
        return (Date) timeStamp;
    }

    public Timestamp toDate(Date date) {
        return (date != null) ? new Timestamp(date.getTime()) : null;
    }

    public SysGenDate toSysGenDate(Timestamp timeStamp) {
        return new SysGenDate(timeStamp);
    }

    public Timestamp toSysGenDate(SysGenDate date) {
        return (date != null && date.getValue() != null) ? new Timestamp(date.getValue().getTime()) : null;
    }


    public java.sql.Date toSqlDate(Date date) {
        return (date != null) ? new java.sql.Date(date.getTime()) : null;
    }

    public String toString(BigDecimal number) {
        return (number != null) ? number.toString() : null;
    }


    public String clobToString(Clob clob) throws SQLException {
        final int length = (int) clob.length();
        final char[] buffer = new char[length];
        int charactersRead;

        try (Reader characterStream = clob.getCharacterStream()) {
            charactersRead = characterStream.read(buffer, 0, length);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read ");
        }
        return new String(buffer, 0, charactersRead);
    }
}
