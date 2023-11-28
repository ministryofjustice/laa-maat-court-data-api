package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class HrDetailType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.HR_DETAILTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,2002,91,2002,12,2,91,12,2002,12,93 };
  protected static ORADataFactory[] _factory = new ORADataFactory[11];
  static
  {
    _factory[1] = FrequencyType.getORADataFactory();
    _factory[3] = HrDetailDescriptionType.getORADataFactory();
    _factory[8] = HrReasonType.getORADataFactory();
  }
  protected static final HrDetailType _HrDetailTypeFactory = new HrDetailType();

  public static ORADataFactory getORADataFactory()
  { return _HrDetailTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[11], _sqlType, _factory); }
  public HrDetailType()
  { _init_struct(true); }
  public HrDetailType(java.math.BigDecimal id, FrequencyType frequencyObject, java.sql.Timestamp dateReceived, HrDetailDescriptionType hrDetailDescriptionObject, String otherDescription, java.math.BigDecimal amountNumber, java.sql.Timestamp dateDue, String accepted, HrReasonType hrReasonObject, String hrReasonNote, java.sql.Timestamp timeStamp) throws SQLException
  { _init_struct(true);
    setId(id);
    setFrequencyObject(frequencyObject);
    setDateReceived(dateReceived);
    setHrDetailDescriptionObject(hrDetailDescriptionObject);
    setOtherDescription(otherDescription);
    setAmountNumber(amountNumber);
    setDateDue(dateDue);
    setAccepted(accepted);
    setHrReasonObject(hrReasonObject);
    setHrReasonNote(hrReasonNote);
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
  protected ORAData create(HrDetailType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new HrDetailType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setId(java.math.BigDecimal id) throws SQLException
  { _struct.setAttribute(0, id); }


  public FrequencyType getFrequencyObject() throws SQLException
  { return (FrequencyType) _struct.getAttribute(1); }

  public void setFrequencyObject(FrequencyType frequencyObject) throws SQLException
  { _struct.setAttribute(1, frequencyObject); }


  public java.sql.Timestamp getDateReceived() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(2); }

  public void setDateReceived(java.sql.Timestamp dateReceived) throws SQLException
  { _struct.setAttribute(2, dateReceived); }


  public HrDetailDescriptionType getHrDetailDescriptionObject() throws SQLException
  { return (HrDetailDescriptionType) _struct.getAttribute(3); }

  public void setHrDetailDescriptionObject(HrDetailDescriptionType hrDetailDescriptionObject) throws SQLException
  { _struct.setAttribute(3, hrDetailDescriptionObject); }


  public String getOtherDescription() throws SQLException
  { return (String) _struct.getAttribute(4); }

  public void setOtherDescription(String otherDescription) throws SQLException
  { _struct.setAttribute(4, otherDescription); }


  public java.math.BigDecimal getAmountNumber() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(5); }

  public void setAmountNumber(java.math.BigDecimal amountNumber) throws SQLException
  { _struct.setAttribute(5, amountNumber); }


  public java.sql.Timestamp getDateDue() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(6); }

  public void setDateDue(java.sql.Timestamp dateDue) throws SQLException
  { _struct.setAttribute(6, dateDue); }


  public String getAccepted() throws SQLException
  { return (String) _struct.getAttribute(7); }

  public void setAccepted(String accepted) throws SQLException
  { _struct.setAttribute(7, accepted); }


  public HrReasonType getHrReasonObject() throws SQLException
  { return (HrReasonType) _struct.getAttribute(8); }

  public void setHrReasonObject(HrReasonType hrReasonObject) throws SQLException
  { _struct.setAttribute(8, hrReasonObject); }


  public String getHrReasonNote() throws SQLException
  { return (String) _struct.getAttribute(9); }

  public void setHrReasonNote(String hrReasonNote) throws SQLException
  { _struct.setAttribute(9, hrReasonNote); }


  public java.sql.Timestamp getTimeStamp() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(10); }

  public void setTimeStamp(java.sql.Timestamp timeStamp) throws SQLException
  { _struct.setAttribute(10, timeStamp); }

}
