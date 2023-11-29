package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class ApplPaymentDetailsTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.APPL_PAYMENT_DETAILS_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final ApplPaymentDetailsTypeRef _ApplPaymentDetailsTypeRefFactory = new ApplPaymentDetailsTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _ApplPaymentDetailsTypeRefFactory; }
  /* constructor */
  public ApplPaymentDetailsTypeRef()
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
    ApplPaymentDetailsTypeRef r = new ApplPaymentDetailsTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static ApplPaymentDetailsTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (ApplPaymentDetailsTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to ApplPaymentDetailsTypeRef: "+exn.toString()); }
  }

  public ApplPaymentDetailsType getValue() throws SQLException
  {
     return (ApplPaymentDetailsType) ApplPaymentDetailsType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(ApplPaymentDetailsType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
