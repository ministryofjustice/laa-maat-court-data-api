package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class FullAssessmentTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.FULL_ASSESSMENTTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final FullAssessmentTypeRef _FullAssessmentTypeRefFactory = new FullAssessmentTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _FullAssessmentTypeRefFactory; }
  /* constructor */
  public FullAssessmentTypeRef()
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
    FullAssessmentTypeRef r = new FullAssessmentTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static FullAssessmentTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (FullAssessmentTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to FullAssessmentTypeRef: "+exn.toString()); }
  }

  public FullAssessmentType getValue() throws SQLException
  {
     return (FullAssessmentType) FullAssessmentType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(FullAssessmentType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
