package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class IncomeEvidenceSummaryType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.INCOME_EVIDENCE_SUMMARY_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 91,91,91,91,91,91,12,12,12,2003,2003,2003 };
  protected static ORADataFactory[] _factory = new ORADataFactory[12];
  static
  {
    _factory[9] = IncomeEvidenceTabType.getORADataFactory();
    _factory[10] = IncomeEvidenceTabType.getORADataFactory();
    _factory[11] = ExtraEvidenceTabtype.getORADataFactory();
  }
  protected static final IncomeEvidenceSummaryType _IncomeEvidenceSummaryTypeFactory = new IncomeEvidenceSummaryType();

  public static ORADataFactory getORADataFactory()
  { return _IncomeEvidenceSummaryTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[12], _sqlType, _factory); }
  public IncomeEvidenceSummaryType()
  { _init_struct(true); }
  public IncomeEvidenceSummaryType(java.sql.Timestamp evidenceDueDate, java.sql.Timestamp evidenceReceivedDate, java.sql.Timestamp upliftAppliedDate, java.sql.Timestamp upliftRemovedDate, java.sql.Timestamp firstReminderDate, java.sql.Timestamp secondReminderDate, String incomeEvidenceNotes, String enabled, String upliftsAvailable, IncomeEvidenceTabType applicantIncomeEvidenceTab, IncomeEvidenceTabType partnerIncomeEvidenceTab, ExtraEvidenceTabtype extraEvidenceTab) throws SQLException
  { _init_struct(true);
    setEvidenceDueDate(evidenceDueDate);
    setEvidenceReceivedDate(evidenceReceivedDate);
    setUpliftAppliedDate(upliftAppliedDate);
    setUpliftRemovedDate(upliftRemovedDate);
    setFirstReminderDate(firstReminderDate);
    setSecondReminderDate(secondReminderDate);
    setIncomeEvidenceNotes(incomeEvidenceNotes);
    setEnabled(enabled);
    setUpliftsAvailable(upliftsAvailable);
    setApplicantIncomeEvidenceTab(applicantIncomeEvidenceTab);
    setPartnerIncomeEvidenceTab(partnerIncomeEvidenceTab);
    setExtraEvidenceTab(extraEvidenceTab);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(IncomeEvidenceSummaryType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new IncomeEvidenceSummaryType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.sql.Timestamp getEvidenceDueDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(0); }

  public void setEvidenceDueDate(java.sql.Timestamp evidenceDueDate) throws SQLException
  { _struct.setAttribute(0, evidenceDueDate); }


  public java.sql.Timestamp getEvidenceReceivedDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(1); }

  public void setEvidenceReceivedDate(java.sql.Timestamp evidenceReceivedDate) throws SQLException
  { _struct.setAttribute(1, evidenceReceivedDate); }


  public java.sql.Timestamp getUpliftAppliedDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(2); }

  public void setUpliftAppliedDate(java.sql.Timestamp upliftAppliedDate) throws SQLException
  { _struct.setAttribute(2, upliftAppliedDate); }


  public java.sql.Timestamp getUpliftRemovedDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(3); }

  public void setUpliftRemovedDate(java.sql.Timestamp upliftRemovedDate) throws SQLException
  { _struct.setAttribute(3, upliftRemovedDate); }


  public java.sql.Timestamp getFirstReminderDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(4); }

  public void setFirstReminderDate(java.sql.Timestamp firstReminderDate) throws SQLException
  { _struct.setAttribute(4, firstReminderDate); }


  public java.sql.Timestamp getSecondReminderDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(5); }

  public void setSecondReminderDate(java.sql.Timestamp secondReminderDate) throws SQLException
  { _struct.setAttribute(5, secondReminderDate); }


  public String getIncomeEvidenceNotes() throws SQLException
  { return (String) _struct.getAttribute(6); }

  public void setIncomeEvidenceNotes(String incomeEvidenceNotes) throws SQLException
  { _struct.setAttribute(6, incomeEvidenceNotes); }


  public String getEnabled() throws SQLException
  { return (String) _struct.getAttribute(7); }

  public void setEnabled(String enabled) throws SQLException
  { _struct.setAttribute(7, enabled); }


  public String getUpliftsAvailable() throws SQLException
  { return (String) _struct.getAttribute(8); }

  public void setUpliftsAvailable(String upliftsAvailable) throws SQLException
  { _struct.setAttribute(8, upliftsAvailable); }


  public IncomeEvidenceTabType getApplicantIncomeEvidenceTab() throws SQLException
  { return (IncomeEvidenceTabType) _struct.getAttribute(9); }

  public void setApplicantIncomeEvidenceTab(IncomeEvidenceTabType applicantIncomeEvidenceTab) throws SQLException
  { _struct.setAttribute(9, applicantIncomeEvidenceTab); }


  public IncomeEvidenceTabType getPartnerIncomeEvidenceTab() throws SQLException
  { return (IncomeEvidenceTabType) _struct.getAttribute(10); }

  public void setPartnerIncomeEvidenceTab(IncomeEvidenceTabType partnerIncomeEvidenceTab) throws SQLException
  { _struct.setAttribute(10, partnerIncomeEvidenceTab); }


  public ExtraEvidenceTabtype getExtraEvidenceTab() throws SQLException
  { return (ExtraEvidenceTabtype) _struct.getAttribute(11); }

  public void setExtraEvidenceTab(ExtraEvidenceTabtype extraEvidenceTab) throws SQLException
  { _struct.setAttribute(11, extraEvidenceTab); }

}
