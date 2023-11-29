package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class DigiMeansAssessTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.DIGI_MEANS_ASSESS_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final DigiMeansAssessTypeRef _DigiMeansAssessTypeRefFactory = new DigiMeansAssessTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _DigiMeansAssessTypeRefFactory; }
  /* constructor */
  public DigiMeansAssessTypeRef()
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
    DigiMeansAssessTypeRef r = new DigiMeansAssessTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static DigiMeansAssessTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (DigiMeansAssessTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to DigiMeansAssessTypeRef: "+exn.toString()); }
  }

  public DigiMeansAssessType getValue() throws SQLException
  {
     return (DigiMeansAssessType) DigiMeansAssessType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(DigiMeansAssessType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
