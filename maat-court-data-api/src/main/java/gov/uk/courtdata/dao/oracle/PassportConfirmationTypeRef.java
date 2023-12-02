package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class PassportConfirmationTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.PASSPORT_CONFIRMATION_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final PassportConfirmationTypeRef _PassportConfirmationTypeRefFactory = new PassportConfirmationTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _PassportConfirmationTypeRefFactory; }
  /* constructor */
  public PassportConfirmationTypeRef()
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
    PassportConfirmationTypeRef r = new PassportConfirmationTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static PassportConfirmationTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (PassportConfirmationTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to PassportConfirmationTypeRef: "+exn.toString()); }
  }

  public PassportConfirmationType getValue() throws SQLException
  {
     return (PassportConfirmationType) PassportConfirmationType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(PassportConfirmationType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
