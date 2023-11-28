package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class DrcFileType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.DRC_FILE_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,12,91,91,12,12 };
  protected static ORADataFactory[] _factory = new ORADataFactory[6];
  protected static final DrcFileType _DrcFileTypeFactory = new DrcFileType();

  public static ORADataFactory getORADataFactory()
  { return _DrcFileTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[6], _sqlType, _factory); }
  public DrcFileType()
  { _init_struct(true); }
  public DrcFileType(java.math.BigDecimal contribFileId, String fileName, java.sql.Timestamp dateSent, java.sql.Timestamp dateAcknowledged, String transferStatus, String acknowledgeCode) throws SQLException
  { _init_struct(true);
    setContribFileId(contribFileId);
    setFileName(fileName);
    setDateSent(dateSent);
    setDateAcknowledged(dateAcknowledged);
    setTransferStatus(transferStatus);
    setAcknowledgeCode(acknowledgeCode);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(DrcFileType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new DrcFileType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getContribFileId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setContribFileId(java.math.BigDecimal contribFileId) throws SQLException
  { _struct.setAttribute(0, contribFileId); }


  public String getFileName() throws SQLException
  { return (String) _struct.getAttribute(1); }

  public void setFileName(String fileName) throws SQLException
  { _struct.setAttribute(1, fileName); }


  public java.sql.Timestamp getDateSent() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(2); }

  public void setDateSent(java.sql.Timestamp dateSent) throws SQLException
  { _struct.setAttribute(2, dateSent); }


  public java.sql.Timestamp getDateAcknowledged() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(3); }

  public void setDateAcknowledged(java.sql.Timestamp dateAcknowledged) throws SQLException
  { _struct.setAttribute(3, dateAcknowledged); }


  public String getTransferStatus() throws SQLException
  { return (String) _struct.getAttribute(4); }

  public void setTransferStatus(String transferStatus) throws SQLException
  { _struct.setAttribute(4, transferStatus); }


  public String getAcknowledgeCode() throws SQLException
  { return (String) _struct.getAttribute(5); }

  public void setAcknowledgeCode(String acknowledgeCode) throws SQLException
  { _struct.setAttribute(5, acknowledgeCode); }

}
