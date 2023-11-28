package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class ResidentialStatusTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.RESIDENTIAL_STATUS_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final ResidentialStatusTypeRef _ResidentialStatusTypeRefFactory = new ResidentialStatusTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _ResidentialStatusTypeRefFactory; }
  /* constructor */
  public ResidentialStatusTypeRef()
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
    ResidentialStatusTypeRef r = new ResidentialStatusTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static ResidentialStatusTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (ResidentialStatusTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to ResidentialStatusTypeRef: "+exn.toString()); }
  }

  public ResidentialStatusType getValue() throws SQLException
  {
     return (ResidentialStatusType) ResidentialStatusType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(ResidentialStatusType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
