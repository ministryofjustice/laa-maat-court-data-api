package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class FdcNotesType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.FDC_NOTES_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,2,12,93,12,93 };
  protected static ORADataFactory[] _factory = new ORADataFactory[6];
  protected static final FdcNotesType _FdcNotesTypeFactory = new FdcNotesType();

  public static ORADataFactory getORADataFactory()
  { return _FdcNotesTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[6], _sqlType, _factory); }
  public FdcNotesType()
  { _init_struct(true); }
  public FdcNotesType(java.math.BigDecimal id, java.math.BigDecimal fdcId, String note, java.sql.Timestamp dateCreated, String userCreated, java.sql.Timestamp timeStamp) throws SQLException
  { _init_struct(true);
    setId(id);
    setFdcId(fdcId);
    setNote(note);
    setDateCreated(dateCreated);
    setUserCreated(userCreated);
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
  protected ORAData create(FdcNotesType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new FdcNotesType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setId(java.math.BigDecimal id) throws SQLException
  { _struct.setAttribute(0, id); }


  public java.math.BigDecimal getFdcId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(1); }

  public void setFdcId(java.math.BigDecimal fdcId) throws SQLException
  { _struct.setAttribute(1, fdcId); }


  public String getNote() throws SQLException
  { return (String) _struct.getAttribute(2); }

  public void setNote(String note) throws SQLException
  { _struct.setAttribute(2, note); }


  public java.sql.Timestamp getDateCreated() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(3); }

  public void setDateCreated(java.sql.Timestamp dateCreated) throws SQLException
  { _struct.setAttribute(3, dateCreated); }


  public String getUserCreated() throws SQLException
  { return (String) _struct.getAttribute(4); }

  public void setUserCreated(String userCreated) throws SQLException
  { _struct.setAttribute(4, userCreated); }


  public java.sql.Timestamp getTimeStamp() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(5); }

  public void setTimeStamp(java.sql.Timestamp timeStamp) throws SQLException
  { _struct.setAttribute(5, timeStamp); }

}
