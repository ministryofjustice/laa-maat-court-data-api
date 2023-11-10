package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class CapitalEvidenceTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.CAPITAL_EVIDENCE_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final CapitalEvidenceTypeRef _CapitalEvidenceTypeRefFactory = new CapitalEvidenceTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _CapitalEvidenceTypeRefFactory; }
  /* constructor */
  public CapitalEvidenceTypeRef()
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
    CapitalEvidenceTypeRef r = new CapitalEvidenceTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static CapitalEvidenceTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (CapitalEvidenceTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to CapitalEvidenceTypeRef: "+exn.toString()); }
  }

  public CapitalEvidenceType getValue() throws SQLException
  {
     return (CapitalEvidenceType) CapitalEvidenceType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(CapitalEvidenceType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
