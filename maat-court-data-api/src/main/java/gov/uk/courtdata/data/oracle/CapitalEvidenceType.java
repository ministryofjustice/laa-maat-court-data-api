package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class CapitalEvidenceType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.CAPITAL_EVIDENCE_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,2002,12,91,93 };
  protected static ORADataFactory[] _factory = new ORADataFactory[5];
  static
  {
    _factory[1] = EvidenceTypeType.getORADataFactory();
  }
  protected static final CapitalEvidenceType _CapitalEvidenceTypeFactory = new CapitalEvidenceType();

  public static ORADataFactory getORADataFactory()
  { return _CapitalEvidenceTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[5], _sqlType, _factory); }
  public CapitalEvidenceType()
  { _init_struct(true); }
  public CapitalEvidenceType(java.math.BigDecimal id, EvidenceTypeType evidenceTypeObject, String otherDescription, java.sql.Timestamp dateReceived, java.sql.Timestamp timeStamp) throws SQLException
  { _init_struct(true);
    setId(id);
    setEvidenceTypeObject(evidenceTypeObject);
    setOtherDescription(otherDescription);
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
  protected ORAData create(CapitalEvidenceType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new CapitalEvidenceType();
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


  public String getOtherDescription() throws SQLException
  { return (String) _struct.getAttribute(2); }

  public void setOtherDescription(String otherDescription) throws SQLException
  { _struct.setAttribute(2, otherDescription); }


  public java.sql.Timestamp getDateReceived() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(3); }

  public void setDateReceived(java.sql.Timestamp dateReceived) throws SQLException
  { _struct.setAttribute(3, dateReceived); }


  public java.sql.Timestamp getTimeStamp() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(4); }

  public void setTimeStamp(java.sql.Timestamp timeStamp) throws SQLException
  { _struct.setAttribute(4, timeStamp); }

}
