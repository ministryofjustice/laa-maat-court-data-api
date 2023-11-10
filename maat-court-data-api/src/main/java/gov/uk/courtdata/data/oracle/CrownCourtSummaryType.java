package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class CrownCourtSummaryType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.CROWN_COURT_SUMMARY_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,12,91,91,12,12,2003,12,2002,91 };
  protected static ORADataFactory[] _factory = new ORADataFactory[10];
  static
  {
    _factory[6] = OutcomeTabtype.getORADataFactory();
    _factory[8] = EvidenceFeeType.getORADataFactory();
  }
  protected static final CrownCourtSummaryType _CrownCourtSummaryTypeFactory = new CrownCourtSummaryType();

  public static ORADataFactory getORADataFactory()
  { return _CrownCourtSummaryTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[10], _sqlType, _factory); }
  public CrownCourtSummaryType()
  { _init_struct(true); }
  public CrownCourtSummaryType(java.math.BigDecimal ccRepId, String ccRepType, java.sql.Timestamp ccReporderDate, java.sql.Timestamp ccWithdrawalDate, String ccReporderDecision, String ccImprisoned, OutcomeTabtype ccOutcomeTab, String benchWarrantIssued, EvidenceFeeType evidenceFeeObject, java.sql.Timestamp sentenceOrderDate) throws SQLException
  { _init_struct(true);
    setCcRepId(ccRepId);
    setCcRepType(ccRepType);
    setCcReporderDate(ccReporderDate);
    setCcWithdrawalDate(ccWithdrawalDate);
    setCcReporderDecision(ccReporderDecision);
    setCcImprisoned(ccImprisoned);
    setCcOutcomeTab(ccOutcomeTab);
    setBenchWarrantIssued(benchWarrantIssued);
    setEvidenceFeeObject(evidenceFeeObject);
    setSentenceOrderDate(sentenceOrderDate);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(CrownCourtSummaryType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new CrownCourtSummaryType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getCcRepId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setCcRepId(java.math.BigDecimal ccRepId) throws SQLException
  { _struct.setAttribute(0, ccRepId); }


  public String getCcRepType() throws SQLException
  { return (String) _struct.getAttribute(1); }

  public void setCcRepType(String ccRepType) throws SQLException
  { _struct.setAttribute(1, ccRepType); }


  public java.sql.Timestamp getCcReporderDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(2); }

  public void setCcReporderDate(java.sql.Timestamp ccReporderDate) throws SQLException
  { _struct.setAttribute(2, ccReporderDate); }


  public java.sql.Timestamp getCcWithdrawalDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(3); }

  public void setCcWithdrawalDate(java.sql.Timestamp ccWithdrawalDate) throws SQLException
  { _struct.setAttribute(3, ccWithdrawalDate); }


  public String getCcReporderDecision() throws SQLException
  { return (String) _struct.getAttribute(4); }

  public void setCcReporderDecision(String ccReporderDecision) throws SQLException
  { _struct.setAttribute(4, ccReporderDecision); }


  public String getCcImprisoned() throws SQLException
  { return (String) _struct.getAttribute(5); }

  public void setCcImprisoned(String ccImprisoned) throws SQLException
  { _struct.setAttribute(5, ccImprisoned); }


  public OutcomeTabtype getCcOutcomeTab() throws SQLException
  { return (OutcomeTabtype) _struct.getAttribute(6); }

  public void setCcOutcomeTab(OutcomeTabtype ccOutcomeTab) throws SQLException
  { _struct.setAttribute(6, ccOutcomeTab); }


  public String getBenchWarrantIssued() throws SQLException
  { return (String) _struct.getAttribute(7); }

  public void setBenchWarrantIssued(String benchWarrantIssued) throws SQLException
  { _struct.setAttribute(7, benchWarrantIssued); }


  public EvidenceFeeType getEvidenceFeeObject() throws SQLException
  { return (EvidenceFeeType) _struct.getAttribute(8); }

  public void setEvidenceFeeObject(EvidenceFeeType evidenceFeeObject) throws SQLException
  { _struct.setAttribute(8, evidenceFeeObject); }


  public java.sql.Timestamp getSentenceOrderDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(9); }

  public void setSentenceOrderDate(java.sql.Timestamp sentenceOrderDate) throws SQLException
  { _struct.setAttribute(9, sentenceOrderDate); }

}
