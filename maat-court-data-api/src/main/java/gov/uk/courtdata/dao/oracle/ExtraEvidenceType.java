package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class ExtraEvidenceType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.EXTRA_EVIDENCE_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,2002,12,12,12,91,93 };
  protected static ORADataFactory[] _factory = new ORADataFactory[7];
  static
  {
    _factory[1] = EvidenceTypeType.getORADataFactory();
  }
  protected static final ExtraEvidenceType _ExtraEvidenceTypeFactory = new ExtraEvidenceType();

  public static ORADataFactory getORADataFactory()
  { return _ExtraEvidenceTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[7], _sqlType, _factory); }
  public ExtraEvidenceType()
  { _init_struct(true); }
  public ExtraEvidenceType(java.math.BigDecimal id, EvidenceTypeType evidenceTypeObject, String otherText, String mandatory, String adhoc, java.sql.Timestamp dateReceived, java.sql.Timestamp timeStamp) throws SQLException
  { _init_struct(true);
    setId(id);
    setEvidenceTypeObject(evidenceTypeObject);
    setOtherText(otherText);
    setMandatory(mandatory);
    setAdhoc(adhoc);
    setDateReceived(dateReceived);
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
  protected ORAData create(ExtraEvidenceType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new ExtraEvidenceType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setId(java.math.BigDecimal id) throws SQLException
  { _struct.setAttribute(0, id); }


  public EvidenceTypeType getEvidenceTypeObject() throws SQLException
  { return (EvidenceTypeType) _struct.getAttribute(1); }

  public void setEvidenceTypeObject(EvidenceTypeType evidenceTypeObject) throws SQLException
  { _struct.setAttribute(1, evidenceTypeObject); }


  public String getOtherText() throws SQLException
  { return (String) _struct.getAttribute(2); }

  public void setOtherText(String otherText) throws SQLException
  { _struct.setAttribute(2, otherText); }


  public String getMandatory() throws SQLException
  { return (String) _struct.getAttribute(3); }

  public void setMandatory(String mandatory) throws SQLException
  { _struct.setAttribute(3, mandatory); }


  public String getAdhoc() throws SQLException
  { return (String) _struct.getAttribute(4); }

  public void setAdhoc(String adhoc) throws SQLException
  { _struct.setAttribute(4, adhoc); }


  public java.sql.Timestamp getDateReceived() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(5); }

  public void setDateReceived(java.sql.Timestamp dateReceived) throws SQLException
  { _struct.setAttribute(5, dateReceived); }


  public java.sql.Timestamp getTimeStamp() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(6); }

  public void setTimeStamp(java.sql.Timestamp timeStamp) throws SQLException
  { _struct.setAttribute(6, timeStamp); }

}
