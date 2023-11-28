package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class ApplicantLinkType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.APPLICANT_LINK_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,91,91,2002 };
  protected static ORADataFactory[] _factory = new ORADataFactory[4];
  static
  {
    _factory[3] = ApplicantDetailsType.getORADataFactory();
  }
  protected static final ApplicantLinkType _ApplicantLinkTypeFactory = new ApplicantLinkType();

  public static ORADataFactory getORADataFactory()
  { return _ApplicantLinkTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[4], _sqlType, _factory); }
  public ApplicantLinkType()
  { _init_struct(true); }
  public ApplicantLinkType(java.math.BigDecimal id, java.sql.Timestamp linkDate, java.sql.Timestamp unlinkDate, ApplicantDetailsType applicantDetailsObject) throws SQLException
  { _init_struct(true);
    setId(id);
    setLinkDate(linkDate);
    setUnlinkDate(unlinkDate);
    setApplicantDetailsObject(applicantDetailsObject);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(ApplicantLinkType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new ApplicantLinkType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setId(java.math.BigDecimal id) throws SQLException
  { _struct.setAttribute(0, id); }


  public java.sql.Timestamp getLinkDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(1); }

  public void setLinkDate(java.sql.Timestamp linkDate) throws SQLException
  { _struct.setAttribute(1, linkDate); }


  public java.sql.Timestamp getUnlinkDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(2); }

  public void setUnlinkDate(java.sql.Timestamp unlinkDate) throws SQLException
  { _struct.setAttribute(2, unlinkDate); }


  public ApplicantDetailsType getApplicantDetailsObject() throws SQLException
  { return (ApplicantDetailsType) _struct.getAttribute(3); }

  public void setApplicantDetailsObject(ApplicantDetailsType applicantDetailsObject) throws SQLException
  { _struct.setAttribute(3, applicantDetailsObject); }

}
