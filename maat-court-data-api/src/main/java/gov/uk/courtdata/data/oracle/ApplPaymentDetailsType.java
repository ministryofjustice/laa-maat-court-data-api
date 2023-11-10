package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class ApplPaymentDetailsType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.APPL_PAYMENT_DETAILS_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2002,2,12,12,12 };
  protected static ORADataFactory[] _factory = new ORADataFactory[5];
  static
  {
    _factory[0] = PaymentMethodType.getORADataFactory();
  }
  protected static final ApplPaymentDetailsType _ApplPaymentDetailsTypeFactory = new ApplPaymentDetailsType();

  public static ORADataFactory getORADataFactory()
  { return _ApplPaymentDetailsTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[5], _sqlType, _factory); }
  public ApplPaymentDetailsType()
  { _init_struct(true); }
  public ApplPaymentDetailsType(PaymentMethodType paymentMethodObject, java.math.BigDecimal paymentDay, String bankAccountNo, String bankAccountName, String sortCode) throws SQLException
  { _init_struct(true);
    setPaymentMethodObject(paymentMethodObject);
    setPaymentDay(paymentDay);
    setBankAccountNo(bankAccountNo);
    setBankAccountName(bankAccountName);
    setSortCode(sortCode);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(ApplPaymentDetailsType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new ApplPaymentDetailsType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public PaymentMethodType getPaymentMethodObject() throws SQLException
  { return (PaymentMethodType) _struct.getAttribute(0); }

  public void setPaymentMethodObject(PaymentMethodType paymentMethodObject) throws SQLException
  { _struct.setAttribute(0, paymentMethodObject); }


  public java.math.BigDecimal getPaymentDay() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(1); }

  public void setPaymentDay(java.math.BigDecimal paymentDay) throws SQLException
  { _struct.setAttribute(1, paymentDay); }


  public String getBankAccountNo() throws SQLException
  { return (String) _struct.getAttribute(2); }

  public void setBankAccountNo(String bankAccountNo) throws SQLException
  { _struct.setAttribute(2, bankAccountNo); }


  public String getBankAccountName() throws SQLException
  { return (String) _struct.getAttribute(3); }

  public void setBankAccountName(String bankAccountName) throws SQLException
  { _struct.setAttribute(3, bankAccountName); }


  public String getSortCode() throws SQLException
  { return (String) _struct.getAttribute(4); }

  public void setSortCode(String sortCode) throws SQLException
  { _struct.setAttribute(4, sortCode); }

}
