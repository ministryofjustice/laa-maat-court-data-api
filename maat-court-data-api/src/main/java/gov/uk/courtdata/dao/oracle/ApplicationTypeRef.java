package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class ApplicationTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.APPLICATION_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final ApplicationTypeRef _ApplicationTypeRefFactory = new ApplicationTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _ApplicationTypeRefFactory; }
  /* constructor */
  public ApplicationTypeRef()
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
    ApplicationTypeRef r = new ApplicationTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static ApplicationTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (ApplicationTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to ApplicationTypeRef: "+exn.toString()); }
  }

  public ApplicationType getValue() throws SQLException
  {
     return (ApplicationType) ApplicationType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(ApplicationType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
