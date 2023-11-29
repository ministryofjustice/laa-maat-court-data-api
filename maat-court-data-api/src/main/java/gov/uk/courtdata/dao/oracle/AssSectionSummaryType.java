package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class AssSectionSummaryType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.ASS_SECTION_SUMMARY_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 12,2,2,2,2003 };
  protected static ORADataFactory[] _factory = new ORADataFactory[5];
  static
  {
    _factory[4] = AssessmentDetailTabType.getORADataFactory();
  }
  protected static final AssSectionSummaryType _AssSectionSummaryTypeFactory = new AssSectionSummaryType();

  public static ORADataFactory getORADataFactory()
  { return _AssSectionSummaryTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[5], _sqlType, _factory); }
  public AssSectionSummaryType()
  { _init_struct(true); }
  public AssSectionSummaryType(String section, java.math.BigDecimal appAnnualTotal, java.math.BigDecimal partnerAnnualTotal, java.math.BigDecimal annualTotal, AssessmentDetailTabType assessmentDetailTab) throws SQLException
  { _init_struct(true);
    setSection(section);
    setAppAnnualTotal(appAnnualTotal);
    setPartnerAnnualTotal(partnerAnnualTotal);
    setAnnualTotal(annualTotal);
    setAssessmentDetailTab(assessmentDetailTab);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(AssSectionSummaryType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new AssSectionSummaryType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public String getSection() throws SQLException
  { return (String) _struct.getAttribute(0); }

  public void setSection(String section) throws SQLException
  { _struct.setAttribute(0, section); }


  public java.math.BigDecimal getAppAnnualTotal() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(1); }

  public void setAppAnnualTotal(java.math.BigDecimal appAnnualTotal) throws SQLException
  { _struct.setAttribute(1, appAnnualTotal); }


  public java.math.BigDecimal getPartnerAnnualTotal() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(2); }

  public void setPartnerAnnualTotal(java.math.BigDecimal partnerAnnualTotal) throws SQLException
  { _struct.setAttribute(2, partnerAnnualTotal); }


  public java.math.BigDecimal getAnnualTotal() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(3); }

  public void setAnnualTotal(java.math.BigDecimal annualTotal) throws SQLException
  { _struct.setAttribute(3, annualTotal); }


  public AssessmentDetailTabType getAssessmentDetailTab() throws SQLException
  { return (AssessmentDetailTabType) _struct.getAttribute(4); }

  public void setAssessmentDetailTab(AssessmentDetailTabType assessmentDetailTab) throws SQLException
  { _struct.setAttribute(4, assessmentDetailTab); }

}
