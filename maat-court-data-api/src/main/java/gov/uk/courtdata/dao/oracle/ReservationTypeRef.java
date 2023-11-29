package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class ReservationTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.RESERVATION_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final ReservationTypeRef _ReservationTypeRefFactory = new ReservationTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _ReservationTypeRefFactory; }
  /* constructor */
  public ReservationTypeRef()
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
    ReservationTypeRef r = new ReservationTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static ReservationTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (ReservationTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to ReservationTypeRef: "+exn.toString()); }
  }

  public ReservationType getValue() throws SQLException
  {
     return (ReservationType) ReservationType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(ReservationType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
