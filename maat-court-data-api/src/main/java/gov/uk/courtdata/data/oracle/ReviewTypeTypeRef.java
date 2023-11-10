package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class ReviewTypeTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.REVIEW_TYPE_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final ReviewTypeTypeRef _ReviewTypeTypeRefFactory = new ReviewTypeTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _ReviewTypeTypeRefFactory; }
  /* constructor */
  public ReviewTypeTypeRef()
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
    ReviewTypeTypeRef r = new ReviewTypeTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static ReviewTypeTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (ReviewTypeTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to ReviewTypeTypeRef: "+exn.toString()); }
  }

  public ReviewTypeType getValue() throws SQLException
  {
     return (ReviewTypeType) ReviewTypeType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(ReviewTypeType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
