package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class IncomeEvidenceType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.INCOME_EVIDENCE_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,2002,91,2,93 };
  protected static ORADataFactory[] _factory = new ORADataFactory[5];
  static
  {
    _factory[1] = EvidenceTypeType.getORADataFactory();
  }
  protected static final IncomeEvidenceType _IncomeEvidenceTypeFactory = new IncomeEvidenceType();

  public static ORADataFactory getORADataFactory()
  { return _IncomeEvidenceTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[5], _sqlType, _factory); }
  public IncomeEvidenceType()
  { _init_struct(true); }
  public IncomeEvidenceType(java.math.BigDecimal id, EvidenceTypeType evidenceTypeObject, java.sql.Timestamp dateReceived, java.math.BigDecimal applId, java.sql.Timestamp timeStamp) throws SQLException
  { _init_struct(true);
    setId(id);
    setEvidenceTypeObject(evidenceTypeObject);
    setDateReceived(dateReceived);
    setApplId(applId);
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
  protected ORAData create(IncomeEvidenceType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new IncomeEvidenceType();
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


  public java.sql.Timestamp getDateReceived() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(2); }

  public void setDateReceived(java.sql.Timestamp dateReceived) throws SQLException
  { _struct.setAttribute(2, dateReceived); }


  public java.math.BigDecimal getApplId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(3); }

  public void setApplId(java.math.BigDecimal applId) throws SQLException
  { _struct.setAttribute(3, applId); }


  public java.sql.Timestamp getTimeStamp() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(4); }

  public void setTimeStamp(java.sql.Timestamp timeStamp) throws SQLException
  { _struct.setAttribute(4, timeStamp); }

}
