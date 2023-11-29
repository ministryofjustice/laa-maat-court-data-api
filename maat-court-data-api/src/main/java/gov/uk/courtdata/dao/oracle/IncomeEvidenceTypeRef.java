package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class IncomeEvidenceTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.INCOME_EVIDENCE_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final IncomeEvidenceTypeRef _IncomeEvidenceTypeRefFactory = new IncomeEvidenceTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _IncomeEvidenceTypeRefFactory; }
  /* constructor */
  public IncomeEvidenceTypeRef()
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
    IncomeEvidenceTypeRef r = new IncomeEvidenceTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static IncomeEvidenceTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (IncomeEvidenceTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to IncomeEvidenceTypeRef: "+exn.toString()); }
  }

  public IncomeEvidenceType getValue() throws SQLException
  {
     return (IncomeEvidenceType) IncomeEvidenceType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(IncomeEvidenceType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
