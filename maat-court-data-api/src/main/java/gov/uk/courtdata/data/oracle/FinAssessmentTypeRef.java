package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class FinAssessmentTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.FIN_ASSESSMENT_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final FinAssessmentTypeRef _FinAssessmentTypeRefFactory = new FinAssessmentTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _FinAssessmentTypeRefFactory; }
  /* constructor */
  public FinAssessmentTypeRef()
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
    FinAssessmentTypeRef r = new FinAssessmentTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static FinAssessmentTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (FinAssessmentTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to FinAssessmentTypeRef: "+exn.toString()); }
  }

  public FinAssessmentType getValue() throws SQLException
  {
     return (FinAssessmentType) FinAssessmentType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(FinAssessmentType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
