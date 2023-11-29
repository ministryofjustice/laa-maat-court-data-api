package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class OffenceTypeTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.OFFENCE_TYPE_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final OffenceTypeTypeRef _OffenceTypeTypeRefFactory = new OffenceTypeTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _OffenceTypeTypeRefFactory; }
  /* constructor */
  public OffenceTypeTypeRef()
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
    OffenceTypeTypeRef r = new OffenceTypeTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static OffenceTypeTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (OffenceTypeTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to OffenceTypeTypeRef: "+exn.toString()); }
  }

  public OffenceTypeType getValue() throws SQLException
  {
     return (OffenceTypeType) OffenceTypeType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(OffenceTypeType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
