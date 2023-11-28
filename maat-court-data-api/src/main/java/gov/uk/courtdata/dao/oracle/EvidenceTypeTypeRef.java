package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class EvidenceTypeTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.EVIDENCE_TYPE_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final EvidenceTypeTypeRef _EvidenceTypeTypeRefFactory = new EvidenceTypeTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _EvidenceTypeTypeRefFactory; }
  /* constructor */
  public EvidenceTypeTypeRef()
  {
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _ref;
  }

  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    EvidenceTypeTypeRef r = new EvidenceTypeTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static EvidenceTypeTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (EvidenceTypeTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to EvidenceTypeTypeRef: "+exn.toString()); }
  }

  public EvidenceTypeType getValue() throws SQLException
  {
     return (EvidenceTypeType) EvidenceTypeType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(EvidenceTypeType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
