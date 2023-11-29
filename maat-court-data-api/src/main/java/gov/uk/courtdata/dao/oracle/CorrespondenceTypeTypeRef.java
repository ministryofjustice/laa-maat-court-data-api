package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class CorrespondenceTypeTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.CORRESPONDENCE_TYPE_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final CorrespondenceTypeTypeRef _CorrespondenceTypeTypeRefFactory = new CorrespondenceTypeTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _CorrespondenceTypeTypeRefFactory; }
  /* constructor */
  public CorrespondenceTypeTypeRef()
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
    CorrespondenceTypeTypeRef r = new CorrespondenceTypeTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static CorrespondenceTypeTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (CorrespondenceTypeTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to CorrespondenceTypeTypeRef: "+exn.toString()); }
  }

  public CorrespondenceTypeType getValue() throws SQLException
  {
     return (CorrespondenceTypeType) CorrespondenceTypeType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(CorrespondenceTypeType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
