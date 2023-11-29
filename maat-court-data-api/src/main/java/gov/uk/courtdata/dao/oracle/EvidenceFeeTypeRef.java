package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class EvidenceFeeTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.EVIDENCE_FEE_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final EvidenceFeeTypeRef _EvidenceFeeTypeRefFactory = new EvidenceFeeTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _EvidenceFeeTypeRefFactory; }
  /* constructor */
  public EvidenceFeeTypeRef()
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
    EvidenceFeeTypeRef r = new EvidenceFeeTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static EvidenceFeeTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (EvidenceFeeTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to EvidenceFeeTypeRef: "+exn.toString()); }
  }

  public EvidenceFeeType getValue() throws SQLException
  {
     return (EvidenceFeeType) EvidenceFeeType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(EvidenceFeeType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
