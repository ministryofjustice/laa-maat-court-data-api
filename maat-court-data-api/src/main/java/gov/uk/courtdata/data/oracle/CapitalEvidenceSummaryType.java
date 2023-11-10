package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class CapitalEvidenceSummaryType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.CAPITAL_EVIDENCE_SUMMARY_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 91,91,91,91,2,91,91,12,93 };
  protected static ORADataFactory[] _factory = new ORADataFactory[9];
  protected static final CapitalEvidenceSummaryType _CapitalEvidenceSummaryTypeFactory = new CapitalEvidenceSummaryType();

  public static ORADataFactory getORADataFactory()
  { return _CapitalEvidenceSummaryTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[9], _sqlType, _factory); }
  public CapitalEvidenceSummaryType()
  { _init_struct(true); }
  public CapitalEvidenceSummaryType(java.sql.Timestamp evidenceDueDate, java.sql.Timestamp evidenceReceivedDate, java.sql.Timestamp capitalAllowWithheldDate, java.sql.Timestamp capitalAllowReinstatedDate, java.math.BigDecimal capitalAllowance, java.sql.Timestamp firstReminderDate, java.sql.Timestamp secondReminderDate, String capitalNote, java.sql.Timestamp timeStamp) throws SQLException
  { _init_struct(true);
    setEvidenceDueDate(evidenceDueDate);
    setEvidenceReceivedDate(evidenceReceivedDate);
    setCapitalAllowWithheldDate(capitalAllowWithheldDate);
    setCapitalAllowReinstatedDate(capitalAllowReinstatedDate);
    setCapitalAllowance(capitalAllowance);
    setFirstReminderDate(firstReminderDate);
    setSecondReminderDate(secondReminderDate);
    setCapitalNote(capitalNote);
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
  protected ORAData create(CapitalEvidenceSummaryType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new CapitalEvidenceSummaryType();
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


  public java.sql.Timestamp getCapitalAllowWithheldDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(2); }

  public void setCapitalAllowWithheldDate(java.sql.Timestamp capitalAllowWithheldDate) throws SQLException
  { _struct.setAttribute(2, capitalAllowWithheldDate); }


  public java.sql.Timestamp getCapitalAllowReinstatedDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(3); }

  public void setCapitalAllowReinstatedDate(java.sql.Timestamp capitalAllowReinstatedDate) throws SQLException
  { _struct.setAttribute(3, capitalAllowReinstatedDate); }


  public java.math.BigDecimal getCapitalAllowance() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(4); }

  public void setCapitalAllowance(java.math.BigDecimal capitalAllowance) throws SQLException
  { _struct.setAttribute(4, capitalAllowance); }


  public java.sql.Timestamp getFirstReminderDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(5); }

  public void setFirstReminderDate(java.sql.Timestamp firstReminderDate) throws SQLException
  { _struct.setAttribute(5, firstReminderDate); }


  public java.sql.Timestamp getSecondReminderDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(6); }

  public void setSecondReminderDate(java.sql.Timestamp secondReminderDate) throws SQLException
  { _struct.setAttribute(6, secondReminderDate); }


  public String getCapitalNote() throws SQLException
  { return (String) _struct.getAttribute(7); }

  public void setCapitalNote(String capitalNote) throws SQLException
  { _struct.setAttribute(7, capitalNote); }


  public java.sql.Timestamp getTimeStamp() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(8); }

  public void setTimeStamp(java.sql.Timestamp timeStamp) throws SQLException
  { _struct.setAttribute(8, timeStamp); }

}
