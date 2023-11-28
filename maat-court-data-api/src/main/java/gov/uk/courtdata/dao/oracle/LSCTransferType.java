package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class LSCTransferType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.LSC_TRANSFERSTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,12,2002,2002,2002,2002,2002,2002,91,91,91,91,2002,2002,2002,2002,93 };
  protected static ORADataFactory[] _factory = new ORADataFactory[17];
  static
  {
    _factory[2] = TransferStatusType.getORADataFactory();
    _factory[3] = AreaType.getORADataFactory();
    _factory[4] = AreaType.getORADataFactory();
    _factory[5] = CmuType.getORADataFactory();
    _factory[6] = CmuType.getORADataFactory();
    _factory[7] = TransferTypeType.getORADataFactory();
    _factory[12] = UserType.getORADataFactory();
    _factory[13] = UserType.getORADataFactory();
    _factory[14] = UserType.getORADataFactory();
    _factory[15] = UserType.getORADataFactory();
  }
  protected static final LSCTransferType _LSCTransferTypeFactory = new LSCTransferType();

  public static ORADataFactory getORADataFactory()
  { return _LSCTransferTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[17], _sqlType, _factory); }
  public LSCTransferType()
  { _init_struct(true); }
  public LSCTransferType(java.math.BigDecimal id, String userCreated, TransferStatusType statusObject, AreaType areaFromObject, AreaType areaToObject, CmuType cmuFromObject, CmuType cmuToObject, TransferTypeType transferTypeObject, java.sql.Timestamp dateHmcsSent, java.sql.Timestamp dateLscReceived, java.sql.Timestamp dateLscReturned, java.sql.Timestamp dateHmcsReceived, UserType hmcsSentByUserObject, UserType lscReceivedByUserObject, UserType lscReturnedByUserObject, UserType hmcsReceivedByUserObject, java.sql.Timestamp timeStamp) throws SQLException
  { _init_struct(true);
    setId(id);
    setUserCreated(userCreated);
    setStatusObject(statusObject);
    setAreaFromObject(areaFromObject);
    setAreaToObject(areaToObject);
    setCmuFromObject(cmuFromObject);
    setCmuToObject(cmuToObject);
    setTransferTypeObject(transferTypeObject);
    setDateHmcsSent(dateHmcsSent);
    setDateLscReceived(dateLscReceived);
    setDateLscReturned(dateLscReturned);
    setDateHmcsReceived(dateHmcsReceived);
    setHmcsSentByUserObject(hmcsSentByUserObject);
    setLscReceivedByUserObject(lscReceivedByUserObject);
    setLscReturnedByUserObject(lscReturnedByUserObject);
    setHmcsReceivedByUserObject(hmcsReceivedByUserObject);
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
  protected ORAData create(LSCTransferType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new LSCTransferType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setId(java.math.BigDecimal id) throws SQLException
  { _struct.setAttribute(0, id); }


  public String getUserCreated() throws SQLException
  { return (String) _struct.getAttribute(1); }

  public void setUserCreated(String userCreated) throws SQLException
  { _struct.setAttribute(1, userCreated); }


  public TransferStatusType getStatusObject() throws SQLException
  { return (TransferStatusType) _struct.getAttribute(2); }

  public void setStatusObject(TransferStatusType statusObject) throws SQLException
  { _struct.setAttribute(2, statusObject); }


  public AreaType getAreaFromObject() throws SQLException
  { return (AreaType) _struct.getAttribute(3); }

  public void setAreaFromObject(AreaType areaFromObject) throws SQLException
  { _struct.setAttribute(3, areaFromObject); }


  public AreaType getAreaToObject() throws SQLException
  { return (AreaType) _struct.getAttribute(4); }

  public void setAreaToObject(AreaType areaToObject) throws SQLException
  { _struct.setAttribute(4, areaToObject); }


  public CmuType getCmuFromObject() throws SQLException
  { return (CmuType) _struct.getAttribute(5); }

  public void setCmuFromObject(CmuType cmuFromObject) throws SQLException
  { _struct.setAttribute(5, cmuFromObject); }


  public CmuType getCmuToObject() throws SQLException
  { return (CmuType) _struct.getAttribute(6); }

  public void setCmuToObject(CmuType cmuToObject) throws SQLException
  { _struct.setAttribute(6, cmuToObject); }


  public TransferTypeType getTransferTypeObject() throws SQLException
  { return (TransferTypeType) _struct.getAttribute(7); }

  public void setTransferTypeObject(TransferTypeType transferTypeObject) throws SQLException
  { _struct.setAttribute(7, transferTypeObject); }


  public java.sql.Timestamp getDateHmcsSent() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(8); }

  public void setDateHmcsSent(java.sql.Timestamp dateHmcsSent) throws SQLException
  { _struct.setAttribute(8, dateHmcsSent); }


  public java.sql.Timestamp getDateLscReceived() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(9); }

  public void setDateLscReceived(java.sql.Timestamp dateLscReceived) throws SQLException
  { _struct.setAttribute(9, dateLscReceived); }


  public java.sql.Timestamp getDateLscReturned() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(10); }

  public void setDateLscReturned(java.sql.Timestamp dateLscReturned) throws SQLException
  { _struct.setAttribute(10, dateLscReturned); }


  public java.sql.Timestamp getDateHmcsReceived() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(11); }

  public void setDateHmcsReceived(java.sql.Timestamp dateHmcsReceived) throws SQLException
  { _struct.setAttribute(11, dateHmcsReceived); }


  public UserType getHmcsSentByUserObject() throws SQLException
  { return (UserType) _struct.getAttribute(12); }

  public void setHmcsSentByUserObject(UserType hmcsSentByUserObject) throws SQLException
  { _struct.setAttribute(12, hmcsSentByUserObject); }


  public UserType getLscReceivedByUserObject() throws SQLException
  { return (UserType) _struct.getAttribute(13); }

  public void setLscReceivedByUserObject(UserType lscReceivedByUserObject) throws SQLException
  { _struct.setAttribute(13, lscReceivedByUserObject); }


  public UserType getLscReturnedByUserObject() throws SQLException
  { return (UserType) _struct.getAttribute(14); }

  public void setLscReturnedByUserObject(UserType lscReturnedByUserObject) throws SQLException
  { _struct.setAttribute(14, lscReturnedByUserObject); }


  public UserType getHmcsReceivedByUserObject() throws SQLException
  { return (UserType) _struct.getAttribute(15); }

  public void setHmcsReceivedByUserObject(UserType hmcsReceivedByUserObject) throws SQLException
  { _struct.setAttribute(15, hmcsReceivedByUserObject); }


  public java.sql.Timestamp getTimeStamp() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(16); }

  public void setTimeStamp(java.sql.Timestamp timeStamp) throws SQLException
  { _struct.setAttribute(16, timeStamp); }

}
