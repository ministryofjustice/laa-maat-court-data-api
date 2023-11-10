package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class ExtraEvidenceTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.EXTRA_EVIDENCE_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final ExtraEvidenceTypeRef _ExtraEvidenceTypeRefFactory = new ExtraEvidenceTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _ExtraEvidenceTypeRefFactory; }
  /* constructor */
  public ExtraEvidenceTypeRef()
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
    ExtraEvidenceTypeRef r = new ExtraEvidenceTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static ExtraEvidenceTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (ExtraEvidenceTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to ExtraEvidenceTypeRef: "+exn.toString()); }
  }

  public ExtraEvidenceType getValue() throws SQLException
  {
     return (ExtraEvidenceType) ExtraEvidenceType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(ExtraEvidenceType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
