package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class ApplicantLinkTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.APPLICANT_LINK_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final ApplicantLinkTypeRef _ApplicantLinkTypeRefFactory = new ApplicantLinkTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _ApplicantLinkTypeRefFactory; }
  /* constructor */
  public ApplicantLinkTypeRef()
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
    ApplicantLinkTypeRef r = new ApplicantLinkTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static ApplicantLinkTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (ApplicantLinkTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to ApplicantLinkTypeRef: "+exn.toString()); }
  }

  public ApplicantLinkType getValue() throws SQLException
  {
     return (ApplicantLinkType) ApplicantLinkType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(ApplicantLinkType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
