package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class HardshipReviewTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.HARDSHIP_REVIEWTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final HardshipReviewTypeRef _HardshipReviewTypeRefFactory = new HardshipReviewTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _HardshipReviewTypeRefFactory; }
  /* constructor */
  public HardshipReviewTypeRef()
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
    HardshipReviewTypeRef r = new HardshipReviewTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static HardshipReviewTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (HardshipReviewTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to HardshipReviewTypeRef: "+exn.toString()); }
  }

  public HardshipReviewType getValue() throws SQLException
  {
     return (HardshipReviewType) HardshipReviewType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(HardshipReviewType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
