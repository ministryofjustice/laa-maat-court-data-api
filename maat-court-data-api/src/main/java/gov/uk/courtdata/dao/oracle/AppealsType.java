package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class AppealsType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.APPEALS_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 12,91,91,91,2002,91 };
  protected static ORADataFactory[] _factory = new ORADataFactory[6];
  static
  {
    _factory[4] = AppealTypeType.getORADataFactory();
  }
  protected static final AppealsType _AppealsTypeFactory = new AppealsType();

  public static ORADataFactory getORADataFactory()
  { return _AppealsTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[6], _sqlType, _factory); }
  public AppealsType()
  { _init_struct(true); }
  public AppealsType(String available, java.sql.Timestamp appealReceivedDate, java.sql.Timestamp appealSentenceOrderDate, java.sql.Timestamp appealSentOrdDtDateSet, AppealTypeType appealTypeObject, java.sql.Timestamp appealTypeDateSet) throws SQLException
  { _init_struct(true);
    setAvailable(available);
    setAppealReceivedDate(appealReceivedDate);
    setAppealSentenceOrderDate(appealSentenceOrderDate);
    setAppealSentOrdDtDateSet(appealSentOrdDtDateSet);
    setAppealTypeObject(appealTypeObject);
    setAppealTypeDateSet(appealTypeDateSet);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(AppealsType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new AppealsType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public String getAvailable() throws SQLException
  { return (String) _struct.getAttribute(0); }

  public void setAvailable(String available) throws SQLException
  { _struct.setAttribute(0, available); }


  public java.sql.Timestamp getAppealReceivedDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(1); }

  public void setAppealReceivedDate(java.sql.Timestamp appealReceivedDate) throws SQLException
  { _struct.setAttribute(1, appealReceivedDate); }


  public java.sql.Timestamp getAppealSentenceOrderDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(2); }

  public void setAppealSentenceOrderDate(java.sql.Timestamp appealSentenceOrderDate) throws SQLException
  { _struct.setAttribute(2, appealSentenceOrderDate); }


  public java.sql.Timestamp getAppealSentOrdDtDateSet() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(3); }

  public void setAppealSentOrdDtDateSet(java.sql.Timestamp appealSentOrdDtDateSet) throws SQLException
  { _struct.setAttribute(3, appealSentOrdDtDateSet); }


  public AppealTypeType getAppealTypeObject() throws SQLException
  { return (AppealTypeType) _struct.getAttribute(4); }

  public void setAppealTypeObject(AppealTypeType appealTypeObject) throws SQLException
  { _struct.setAttribute(4, appealTypeObject); }


  public java.sql.Timestamp getAppealTypeDateSet() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(5); }

  public void setAppealTypeDateSet(java.sql.Timestamp appealTypeDateSet) throws SQLException
  { _struct.setAttribute(5, appealTypeDateSet); }

}
