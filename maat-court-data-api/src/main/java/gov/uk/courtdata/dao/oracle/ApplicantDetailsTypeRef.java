package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class ApplicantDetailsTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.APPLICANT_DETAILS_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final ApplicantDetailsTypeRef _ApplicantDetailsTypeRefFactory = new ApplicantDetailsTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _ApplicantDetailsTypeRefFactory; }
  /* constructor */
  public ApplicantDetailsTypeRef()
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
    ApplicantDetailsTypeRef r = new ApplicantDetailsTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static ApplicantDetailsTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (ApplicantDetailsTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to ApplicantDetailsTypeRef: "+exn.toString()); }
  }

  public ApplicantDetailsType getValue() throws SQLException
  {
     return (ApplicantDetailsType) ApplicantDetailsType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(ApplicantDetailsType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
