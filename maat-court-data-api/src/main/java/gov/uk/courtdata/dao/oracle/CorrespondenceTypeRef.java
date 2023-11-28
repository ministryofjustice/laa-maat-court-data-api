package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class CorrespondenceTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.CORRESPONDENCE_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final CorrespondenceTypeRef _CorrespondenceTypeRefFactory = new CorrespondenceTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _CorrespondenceTypeRefFactory; }
  /* constructor */
  public CorrespondenceTypeRef()
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
    CorrespondenceTypeRef r = new CorrespondenceTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static CorrespondenceTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (CorrespondenceTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to CorrespondenceTypeRef: "+exn.toString()); }
  }

  public CorrespondenceType getValue() throws SQLException
  {
     return (CorrespondenceType) CorrespondenceType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(CorrespondenceType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
