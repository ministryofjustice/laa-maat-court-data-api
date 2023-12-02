package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class ContributionSummaryType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.CONTRIBUTION_SUMMARY_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,2,2,12,12,91,91,12,91,91 };
  protected static ORADataFactory[] _factory = new ORADataFactory[10];
  protected static final ContributionSummaryType _ContributionSummaryTypeFactory = new ContributionSummaryType();

  public static ORADataFactory getORADataFactory()
  { return _ContributionSummaryTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[10], _sqlType, _factory); }
  public ContributionSummaryType()
  { _init_struct(true); }
  public ContributionSummaryType(java.math.BigDecimal id, java.math.BigDecimal monthlyContribs, java.math.BigDecimal upfrontContribs, String basedOn, String upliftApplied, java.sql.Timestamp effectiveDate, java.sql.Timestamp calcDate, String fileName, java.sql.Timestamp dateFileSent, java.sql.Timestamp dateFileReceived) throws SQLException
  { _init_struct(true);
    setId(id);
    setMonthlyContribs(monthlyContribs);
    setUpfrontContribs(upfrontContribs);
    setBasedOn(basedOn);
    setUpliftApplied(upliftApplied);
    setEffectiveDate(effectiveDate);
    setCalcDate(calcDate);
    setFileName(fileName);
    setDateFileSent(dateFileSent);
    setDateFileReceived(dateFileReceived);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(ContributionSummaryType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new ContributionSummaryType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setId(java.math.BigDecimal id) throws SQLException
  { _struct.setAttribute(0, id); }


  public java.math.BigDecimal getMonthlyContribs() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(1); }

  public void setMonthlyContribs(java.math.BigDecimal monthlyContribs) throws SQLException
  { _struct.setAttribute(1, monthlyContribs); }


  public java.math.BigDecimal getUpfrontContribs() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(2); }

  public void setUpfrontContribs(java.math.BigDecimal upfrontContribs) throws SQLException
  { _struct.setAttribute(2, upfrontContribs); }


  public String getBasedOn() throws SQLException
  { return (String) _struct.getAttribute(3); }

  public void setBasedOn(String basedOn) throws SQLException
  { _struct.setAttribute(3, basedOn); }


  public String getUpliftApplied() throws SQLException
  { return (String) _struct.getAttribute(4); }

  public void setUpliftApplied(String upliftApplied) throws SQLException
  { _struct.setAttribute(4, upliftApplied); }


  public java.sql.Timestamp getEffectiveDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(5); }

  public void setEffectiveDate(java.sql.Timestamp effectiveDate) throws SQLException
  { _struct.setAttribute(5, effectiveDate); }


  public java.sql.Timestamp getCalcDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(6); }

  public void setCalcDate(java.sql.Timestamp calcDate) throws SQLException
  { _struct.setAttribute(6, calcDate); }


  public String getFileName() throws SQLException
  { return (String) _struct.getAttribute(7); }

  public void setFileName(String fileName) throws SQLException
  { _struct.setAttribute(7, fileName); }


  public java.sql.Timestamp getDateFileSent() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(8); }

  public void setDateFileSent(java.sql.Timestamp dateFileSent) throws SQLException
  { _struct.setAttribute(8, dateFileSent); }


  public java.sql.Timestamp getDateFileReceived() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(9); }

  public void setDateFileReceived(java.sql.Timestamp dateFileReceived) throws SQLException
  { _struct.setAttribute(9, dateFileReceived); }

}
