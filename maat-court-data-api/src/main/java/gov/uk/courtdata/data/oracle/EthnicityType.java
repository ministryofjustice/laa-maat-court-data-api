package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class EthnicityType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.ETHNICITY_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,12,12,12 };
  protected static ORADataFactory[] _factory = new ORADataFactory[4];
  protected static final EthnicityType _EthnicityTypeFactory = new EthnicityType();

  public static ORADataFactory getORADataFactory()
  { return _EthnicityTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[4], _sqlType, _factory); }
  public EthnicityType()
  { _init_struct(true); }
  public EthnicityType(java.math.BigDecimal id, String level1, String level2, String description) throws SQLException
  { _init_struct(true);
    setId(id);
    setLevel1(level1);
    setLevel2(level2);
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
  protected ORAData create(EthnicityType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new EthnicityType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setId(java.math.BigDecimal id) throws SQLException
  { _struct.setAttribute(0, id); }


  public String getLevel1() throws SQLException
  { return (String) _struct.getAttribute(1); }

  public void setLevel1(String level1) throws SQLException
  { _struct.setAttribute(1, level1); }


  public String getLevel2() throws SQLException
  { return (String) _struct.getAttribute(2); }

  public void setLevel2(String level2) throws SQLException
  { _struct.setAttribute(2, level2); }


  public String getDescription() throws SQLException
  { return (String) _struct.getAttribute(3); }

  public void setDescription(String description) throws SQLException
  { _struct.setAttribute(3, description); }

}
