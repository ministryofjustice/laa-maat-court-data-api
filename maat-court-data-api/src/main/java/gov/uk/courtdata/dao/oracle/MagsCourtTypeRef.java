package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class MagsCourtTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.MAGS_COURT_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final MagsCourtTypeRef _MagsCourtTypeRefFactory = new MagsCourtTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _MagsCourtTypeRefFactory; }
  /* constructor */
  public MagsCourtTypeRef()
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
    MagsCourtTypeRef r = new MagsCourtTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static MagsCourtTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (MagsCourtTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to MagsCourtTypeRef: "+exn.toString()); }
  }

  public MagsCourtType getValue() throws SQLException
  {
     return (MagsCourtType) MagsCourtType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(MagsCourtType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
