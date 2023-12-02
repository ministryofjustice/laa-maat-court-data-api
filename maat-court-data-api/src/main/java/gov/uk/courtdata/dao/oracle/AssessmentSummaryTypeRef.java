package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class AssessmentSummaryTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.ASSESSMENT_SUMMARY_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final AssessmentSummaryTypeRef _AssessmentSummaryTypeRefFactory = new AssessmentSummaryTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _AssessmentSummaryTypeRefFactory; }
  /* constructor */
  public AssessmentSummaryTypeRef()
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
    AssessmentSummaryTypeRef r = new AssessmentSummaryTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static AssessmentSummaryTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (AssessmentSummaryTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to AssessmentSummaryTypeRef: "+exn.toString()); }
  }

  public AssessmentSummaryType getValue() throws SQLException
  {
     return (AssessmentSummaryType) AssessmentSummaryType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(AssessmentSummaryType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
