package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class IOJType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.IOJ_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,2002,2,2002,12,12,91,91,2002,12,93 };
  protected static ORADataFactory[] _factory = new ORADataFactory[11];
  static
  {
    _factory[1] = NewWorkReasonType.getORADataFactory();
    _factory[3] = AssStatusType.getORADataFactory();
    _factory[8] = IOJDecisionReasonType.getORADataFactory();
  }
  protected static final IOJType _IOJTypeFactory = new IOJType();

  public static ORADataFactory getORADataFactory()
  { return _IOJTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[11], _sqlType, _factory); }
  public IOJType()
  { _init_struct(true); }
  public IOJType(java.math.BigDecimal id, NewWorkReasonType newWorkReasonObject, java.math.BigDecimal cmuId, AssStatusType appealStatus, String appealSetupResult, String appealDecisionResult, java.sql.Timestamp receivedDate, java.sql.Timestamp decisionDate, IOJDecisionReasonType decisionReasonObject, String notes, java.sql.Timestamp timeStamp) throws SQLException
  { _init_struct(true);
    setId(id);
    setNewWorkReasonObject(newWorkReasonObject);
    setCmuId(cmuId);
    setAppealStatus(appealStatus);
    setAppealSetupResult(appealSetupResult);
    setAppealDecisionResult(appealDecisionResult);
    setReceivedDate(receivedDate);
    setDecisionDate(decisionDate);
    setDecisionReasonObject(decisionReasonObject);
    setNotes(notes);
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
  protected ORAData create(IOJType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new IOJType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setId(java.math.BigDecimal id) throws SQLException
  { _struct.setAttribute(0, id); }


  public NewWorkReasonType getNewWorkReasonObject() throws SQLException
  { return (NewWorkReasonType) _struct.getAttribute(1); }

  public void setNewWorkReasonObject(NewWorkReasonType newWorkReasonObject) throws SQLException
  { _struct.setAttribute(1, newWorkReasonObject); }


  public java.math.BigDecimal getCmuId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(2); }

  public void setCmuId(java.math.BigDecimal cmuId) throws SQLException
  { _struct.setAttribute(2, cmuId); }


  public AssStatusType getAppealStatus() throws SQLException
  { return (AssStatusType) _struct.getAttribute(3); }

  public void setAppealStatus(AssStatusType appealStatus) throws SQLException
  { _struct.setAttribute(3, appealStatus); }


  public String getAppealSetupResult() throws SQLException
  { return (String) _struct.getAttribute(4); }

  public void setAppealSetupResult(String appealSetupResult) throws SQLException
  { _struct.setAttribute(4, appealSetupResult); }


  public String getAppealDecisionResult() throws SQLException
  { return (String) _struct.getAttribute(5); }

  public void setAppealDecisionResult(String appealDecisionResult) throws SQLException
  { _struct.setAttribute(5, appealDecisionResult); }


  public java.sql.Timestamp getReceivedDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(6); }

  public void setReceivedDate(java.sql.Timestamp receivedDate) throws SQLException
  { _struct.setAttribute(6, receivedDate); }


  public java.sql.Timestamp getDecisionDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(7); }

  public void setDecisionDate(java.sql.Timestamp decisionDate) throws SQLException
  { _struct.setAttribute(7, decisionDate); }


  public IOJDecisionReasonType getDecisionReasonObject() throws SQLException
  { return (IOJDecisionReasonType) _struct.getAttribute(8); }

  public void setDecisionReasonObject(IOJDecisionReasonType decisionReasonObject) throws SQLException
  { _struct.setAttribute(8, decisionReasonObject); }


  public String getNotes() throws SQLException
  { return (String) _struct.getAttribute(9); }

  public void setNotes(String notes) throws SQLException
  { _struct.setAttribute(9, notes); }


  public java.sql.Timestamp getTimeStamp() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(10); }

  public void setTimeStamp(java.sql.Timestamp timeStamp) throws SQLException
  { _struct.setAttribute(10, timeStamp); }

}
