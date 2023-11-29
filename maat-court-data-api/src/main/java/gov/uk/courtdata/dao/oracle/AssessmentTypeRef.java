package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class AssessmentTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.ASSESSMENTTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final AssessmentTypeRef _AssessmentTypeRefFactory = new AssessmentTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _AssessmentTypeRefFactory; }
  /* constructor */
  public AssessmentTypeRef()
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
    AssessmentTypeRef r = new AssessmentTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static AssessmentTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (AssessmentTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to AssessmentTypeRef: "+exn.toString()); }
  }

  public AssessmentType getValue() throws SQLException
  {
     return (AssessmentType) AssessmentType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(AssessmentType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
