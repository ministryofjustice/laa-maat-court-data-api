package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class PaymentMethodTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.PAYMENT_METHOD_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final PaymentMethodTypeRef _PaymentMethodTypeRefFactory = new PaymentMethodTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _PaymentMethodTypeRefFactory; }
  /* constructor */
  public PaymentMethodTypeRef()
  {
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _ref;
  }

  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    PaymentMethodTypeRef r = new PaymentMethodTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static PaymentMethodTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (PaymentMethodTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to PaymentMethodTypeRef: "+exn.toString()); }
  }

  public PaymentMethodType getValue() throws SQLException
  {
     return (PaymentMethodType) PaymentMethodType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(PaymentMethodType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
