package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class FrequencyType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.FREQUENCY_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 12,12,2 };
  protected static ORADataFactory[] _factory = new ORADataFactory[3];
  protected static final FrequencyType _FrequencyTypeFactory = new FrequencyType();

  public static ORADataFactory getORADataFactory()
  { return _FrequencyTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[3], _sqlType, _factory); }
  public FrequencyType()
  { _init_struct(true); }
  public FrequencyType(String code, String description, java.math.BigDecimal annualWeighting) throws SQLException
  { _init_struct(true);
    setCode(code);
    setDescription(description);
    setAnnualWeighting(annualWeighting);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(FrequencyType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new FrequencyType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public String getCode() throws SQLException
  { return (String) _struct.getAttribute(0); }

  public void setCode(String code) throws SQLException
  { _struct.setAttribute(0, code); }


  public String getDescription() throws SQLException
  { return (String) _struct.getAttribute(1); }

  public void setDescription(String description) throws SQLException
  { _struct.setAttribute(1, description); }


  public java.math.BigDecimal getAnnualWeighting() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(2); }

  public void setAnnualWeighting(java.math.BigDecimal annualWeighting) throws SQLException
  { _struct.setAttribute(2, annualWeighting); }

}
