package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class HrProgressType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.HR_PROGRESS_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,2002,2002,91,91,91,93 };
  protected static ORADataFactory[] _factory = new ORADataFactory[7];
  static
  {
    _factory[1] = HrProgressActionType.getORADataFactory();
    _factory[2] = HrProgressResponseType.getORADataFactory();
  }
  protected static final HrProgressType _HrProgressTypeFactory = new HrProgressType();

  public static ORADataFactory getORADataFactory()
  { return _HrProgressTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[7], _sqlType, _factory); }
  public HrProgressType()
  { _init_struct(true); }
  public HrProgressType(java.math.BigDecimal id, HrProgressActionType progressActionObject, HrProgressResponseType responseRequiredObject, java.sql.Timestamp dateRequested, java.sql.Timestamp dateRequired, java.sql.Timestamp dateCompleted, java.sql.Timestamp timeStamp) throws SQLException
  { _init_struct(true);
    setId(id);
    setProgressActionObject(progressActionObject);
    setResponseRequiredObject(responseRequiredObject);
    setDateRequested(dateRequested);
    setDateRequired(dateRequired);
    setDateCompleted(dateCompleted);
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
  protected ORAData create(HrProgressType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new HrProgressType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setId(java.math.BigDecimal id) throws SQLException
  { _struct.setAttribute(0, id); }


  public HrProgressActionType getProgressActionObject() throws SQLException
  { return (HrProgressActionType) _struct.getAttribute(1); }

  public void setProgressActionObject(HrProgressActionType progressActionObject) throws SQLException
  { _struct.setAttribute(1, progressActionObject); }


  public HrProgressResponseType getResponseRequiredObject() throws SQLException
  { return (HrProgressResponseType) _struct.getAttribute(2); }

  public void setResponseRequiredObject(HrProgressResponseType responseRequiredObject) throws SQLException
  { _struct.setAttribute(2, responseRequiredObject); }


  public java.sql.Timestamp getDateRequested() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(3); }

  public void setDateRequested(java.sql.Timestamp dateRequested) throws SQLException
  { _struct.setAttribute(3, dateRequested); }


  public java.sql.Timestamp getDateRequired() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(4); }

  public void setDateRequired(java.sql.Timestamp dateRequired) throws SQLException
  { _struct.setAttribute(4, dateRequired); }


  public java.sql.Timestamp getDateCompleted() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(5); }

  public void setDateCompleted(java.sql.Timestamp dateCompleted) throws SQLException
  { _struct.setAttribute(5, dateCompleted); }


  public java.sql.Timestamp getTimeStamp() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(6); }

  public void setTimeStamp(java.sql.Timestamp timeStamp) throws SQLException
  { _struct.setAttribute(6, timeStamp); }

}
