package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class AssessmentChildWeightingTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.ASSESSMENT_CHILD_WEIGHTINGTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final AssessmentChildWeightingTypeRef _AssessmentChildWeightingTypeRefFactory = new AssessmentChildWeightingTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _AssessmentChildWeightingTypeRefFactory; }
  /* constructor */
  public AssessmentChildWeightingTypeRef()
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
    AssessmentChildWeightingTypeRef r = new AssessmentChildWeightingTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static AssessmentChildWeightingTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (AssessmentChildWeightingTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to AssessmentChildWeightingTypeRef: "+exn.toString()); }
  }

  public AssessmentChildWeightingType getValue() throws SQLException
  {
     return (AssessmentChildWeightingType) AssessmentChildWeightingType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(AssessmentChildWeightingType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
