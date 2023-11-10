package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class AssSectionSummaryTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.ASS_SECTION_SUMMARY_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final AssSectionSummaryTypeRef _AssSectionSummaryTypeRefFactory = new AssSectionSummaryTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _AssSectionSummaryTypeRefFactory; }
  /* constructor */
  public AssSectionSummaryTypeRef()
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
    AssSectionSummaryTypeRef r = new AssSectionSummaryTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static AssSectionSummaryTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (AssSectionSummaryTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to AssSectionSummaryTypeRef: "+exn.toString()); }
  }

  public AssSectionSummaryType getValue() throws SQLException
  {
     return (AssSectionSummaryType) AssSectionSummaryType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(AssSectionSummaryType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
