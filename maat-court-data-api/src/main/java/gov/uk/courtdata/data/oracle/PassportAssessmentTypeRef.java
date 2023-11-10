package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class PassportAssessmentTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.PASSPORT_ASSESSMENTTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final PassportAssessmentTypeRef _PassportAssessmentTypeRefFactory = new PassportAssessmentTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _PassportAssessmentTypeRefFactory; }
  /* constructor */
  public PassportAssessmentTypeRef()
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
    PassportAssessmentTypeRef r = new PassportAssessmentTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static PassportAssessmentTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (PassportAssessmentTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to PassportAssessmentTypeRef: "+exn.toString()); }
  }

  public PassportAssessmentType getValue() throws SQLException
  {
     return (PassportAssessmentType) PassportAssessmentType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(PassportAssessmentType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
