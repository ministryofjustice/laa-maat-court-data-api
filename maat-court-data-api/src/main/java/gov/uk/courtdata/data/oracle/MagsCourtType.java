package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class MagsCourtType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.MAGS_COURT_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 12,12,12 };
  protected static ORADataFactory[] _factory = new ORADataFactory[3];
  protected static final MagsCourtType _MagsCourtTypeFactory = new MagsCourtType();

  public static ORADataFactory getORADataFactory()
  { return _MagsCourtTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[3], _sqlType, _factory); }
  public MagsCourtType()
  { _init_struct(true); }
  public MagsCourtType(String court, String description, String wales) throws SQLException
  { _init_struct(true);
    setCourt(court);
    setDescription(description);
    setWales(wales);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(MagsCourtType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new MagsCourtType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public String getCourt() throws SQLException
  { return (String) _struct.getAttribute(0); }

  public void setCourt(String court) throws SQLException
  { _struct.setAttribute(0, court); }


  public String getDescription() throws SQLException
  { return (String) _struct.getAttribute(1); }

  public void setDescription(String description) throws SQLException
  { _struct.setAttribute(1, description); }


  public String getWales() throws SQLException
  { return (String) _struct.getAttribute(2); }

  public void setWales(String wales) throws SQLException
  { _struct.setAttribute(2, wales); }

}
