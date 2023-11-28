package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class AssessmentDetailType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.ASSESSMENT_DETAILTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,2,2,2002,2,2002,12,12,93 };
  protected static ORADataFactory[] _factory = new ORADataFactory[9];
  static
  {
    _factory[3] = FrequencyType.getORADataFactory();
    _factory[5] = FrequencyType.getORADataFactory();
  }
  protected static final AssessmentDetailType _AssessmentDetailTypeFactory = new AssessmentDetailType();

  public static ORADataFactory getORADataFactory()
  { return _AssessmentDetailTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[9], _sqlType, _factory); }
  public AssessmentDetailType()
  { _init_struct(true); }
  public AssessmentDetailType(java.math.BigDecimal id, java.math.BigDecimal criteriaDetailId, java.math.BigDecimal applicantAmount, FrequencyType applicantFreqObject, java.math.BigDecimal partnerAmount, FrequencyType partnerFreqObject, String description, String detailCode, java.sql.Timestamp timeStamp) throws SQLException
  { _init_struct(true);
    setId(id);
    setCriteriaDetailId(criteriaDetailId);
    setApplicantAmount(applicantAmount);
    setApplicantFreqObject(applicantFreqObject);
    setPartnerAmount(partnerAmount);
    setPartnerFreqObject(partnerFreqObject);
    setDescription(description);
    setDetailCode(detailCode);
    setTimeStamp(timeStamp);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(AssessmentDetailType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new AssessmentDetailType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setId(java.math.BigDecimal id) throws SQLException
  { _struct.setAttribute(0, id); }


  public java.math.BigDecimal getCriteriaDetailId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(1); }

  public void setCriteriaDetailId(java.math.BigDecimal criteriaDetailId) throws SQLException
  { _struct.setAttribute(1, criteriaDetailId); }


  public java.math.BigDecimal getApplicantAmount() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(2); }

  public void setApplicantAmount(java.math.BigDecimal applicantAmount) throws SQLException
  { _struct.setAttribute(2, applicantAmount); }


  public FrequencyType getApplicantFreqObject() throws SQLException
  { return (FrequencyType) _struct.getAttribute(3); }

  public void setApplicantFreqObject(FrequencyType applicantFreqObject) throws SQLException
  { _struct.setAttribute(3, applicantFreqObject); }


  public java.math.BigDecimal getPartnerAmount() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(4); }

  public void setPartnerAmount(java.math.BigDecimal partnerAmount) throws SQLException
  { _struct.setAttribute(4, partnerAmount); }


  public FrequencyType getPartnerFreqObject() throws SQLException
  { return (FrequencyType) _struct.getAttribute(5); }

  public void setPartnerFreqObject(FrequencyType partnerFreqObject) throws SQLException
  { _struct.setAttribute(5, partnerFreqObject); }


  public String getDescription() throws SQLException
  { return (String) _struct.getAttribute(6); }

  public void setDescription(String description) throws SQLException
  { _struct.setAttribute(6, description); }


  public String getDetailCode() throws SQLException
  { return (String) _struct.getAttribute(7); }

  public void setDetailCode(String detailCode) throws SQLException
  { _struct.setAttribute(7, detailCode); }


  public java.sql.Timestamp getTimeStamp() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(8); }

  public void setTimeStamp(java.sql.Timestamp timeStamp) throws SQLException
  { _struct.setAttribute(8, timeStamp); }

}
