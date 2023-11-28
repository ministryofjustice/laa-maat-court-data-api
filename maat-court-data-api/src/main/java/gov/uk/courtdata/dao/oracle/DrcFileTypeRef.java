package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class DrcFileTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.DRC_FILE_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final DrcFileTypeRef _DrcFileTypeRefFactory = new DrcFileTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _DrcFileTypeRefFactory; }
  /* constructor */
  public DrcFileTypeRef()
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
    DrcFileTypeRef r = new DrcFileTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static DrcFileTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (DrcFileTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to DrcFileTypeRef: "+exn.toString()); }
  }

  public DrcFileType getValue() throws SQLException
  {
     return (DrcFileType) DrcFileType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(DrcFileType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
