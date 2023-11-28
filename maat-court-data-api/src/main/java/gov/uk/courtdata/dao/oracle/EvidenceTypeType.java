package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class EvidenceTypeType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.EVIDENCE_TYPE_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 12,12 };
  protected static ORADataFactory[] _factory = new ORADataFactory[2];
  protected static final EvidenceTypeType _EvidenceTypeTypeFactory = new EvidenceTypeType();

  public static ORADataFactory getORADataFactory()
  { return _EvidenceTypeTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[2], _sqlType, _factory); }
  public EvidenceTypeType()
  { _init_struct(true); }
  public EvidenceTypeType(String evidence, String description) throws SQLException
  { _init_struct(true);
    setEvidence(evidence);
    setDescription(description);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(EvidenceTypeType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new EvidenceTypeType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public String getEvidence() throws SQLException
  { return (String) _struct.getAttribute(0); }

  public void setEvidence(String evidence) throws SQLException
  { _struct.setAttribute(0, evidence); }


  public String getDescription() throws SQLException
  { return (String) _struct.getAttribute(1); }

  public void setDescription(String description) throws SQLException
  { _struct.setAttribute(1, description); }

}
