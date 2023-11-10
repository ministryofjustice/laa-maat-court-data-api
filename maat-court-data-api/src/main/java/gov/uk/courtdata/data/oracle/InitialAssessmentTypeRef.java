package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class InitialAssessmentTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.INITIAL_ASSESSMENTTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final InitialAssessmentTypeRef _InitialAssessmentTypeRefFactory = new InitialAssessmentTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _InitialAssessmentTypeRefFactory; }
  /* constructor */
  public InitialAssessmentTypeRef()
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
    InitialAssessmentTypeRef r = new InitialAssessmentTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static InitialAssessmentTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (InitialAssessmentTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to InitialAssessmentTypeRef: "+exn.toString()); }
  }

  public InitialAssessmentType getValue() throws SQLException
  {
     return (InitialAssessmentType) InitialAssessmentType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(InitialAssessmentType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
