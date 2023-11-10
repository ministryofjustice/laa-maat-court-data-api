package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class Di_eform_typeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.DI_EFORM_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final Di_eform_typeRef _Di_eform_typeRefFactory = new Di_eform_typeRef();

  public static ORADataFactory getORADataFactory()
  { return _Di_eform_typeRefFactory; }
  /* constructor */
  public Di_eform_typeRef()
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
    Di_eform_typeRef r = new Di_eform_typeRef();
    r._ref = (REF) d;
    return r;
  }

  public static Di_eform_typeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (Di_eform_typeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to Di_eform_typeRef: "+exn.toString()); }
  }

  public Di_eform_type getValue() throws SQLException
  {
     return (Di_eform_type) Di_eform_type.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(Di_eform_type c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
