package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class CmuTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.CMUTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final CmuTypeRef _CmuTypeRefFactory = new CmuTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _CmuTypeRefFactory; }
  /* constructor */
  public CmuTypeRef()
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
    CmuTypeRef r = new CmuTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static CmuTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (CmuTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to CmuTypeRef: "+exn.toString()); }
  }

  public CmuType getValue() throws SQLException
  {
     return (CmuType) CmuType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(CmuType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
