package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class AssessmentDetailTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.ASSESSMENT_DETAILTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final AssessmentDetailTypeRef _AssessmentDetailTypeRefFactory = new AssessmentDetailTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _AssessmentDetailTypeRefFactory; }
  /* constructor */
  public AssessmentDetailTypeRef()
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
    AssessmentDetailTypeRef r = new AssessmentDetailTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static AssessmentDetailTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (AssessmentDetailTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to AssessmentDetailTypeRef: "+exn.toString()); }
  }

  public AssessmentDetailType getValue() throws SQLException
  {
     return (AssessmentDetailType) AssessmentDetailType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(AssessmentDetailType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
