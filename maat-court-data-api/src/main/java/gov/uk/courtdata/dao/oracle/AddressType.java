package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class AddressType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.ADDRESS_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,12,12,12,12,12,12,12 };
  protected static ORADataFactory[] _factory = new ORADataFactory[8];
  protected static final AddressType _AddressTypeFactory = new AddressType();

  public static ORADataFactory getORADataFactory()
  { return _AddressTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[8], _sqlType, _factory); }
  public AddressType()
  { _init_struct(true); }
  public AddressType(java.math.BigDecimal id, String line1, String line2, String line3, String city, String postcode, String county, String country) throws SQLException
  { _init_struct(true);
    setId(id);
    setLine1(line1);
    setLine2(line2);
    setLine3(line3);
    setCity(city);
    setPostcode(postcode);
    setCounty(county);
    setCountry(country);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(AddressType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new AddressType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setId(java.math.BigDecimal id) throws SQLException
  { _struct.setAttribute(0, id); }


  public String getLine1() throws SQLException
  { return (String) _struct.getAttribute(1); }

  public void setLine1(String line1) throws SQLException
  { _struct.setAttribute(1, line1); }


  public String getLine2() throws SQLException
  { return (String) _struct.getAttribute(2); }

  public void setLine2(String line2) throws SQLException
  { _struct.setAttribute(2, line2); }


  public String getLine3() throws SQLException
  { return (String) _struct.getAttribute(3); }

  public void setLine3(String line3) throws SQLException
  { _struct.setAttribute(3, line3); }


  public String getCity() throws SQLException
  { return (String) _struct.getAttribute(4); }

  public void setCity(String city) throws SQLException
  { _struct.setAttribute(4, city); }


  public String getPostcode() throws SQLException
  { return (String) _struct.getAttribute(5); }

  public void setPostcode(String postcode) throws SQLException
  { _struct.setAttribute(5, postcode); }


  public String getCounty() throws SQLException
  { return (String) _struct.getAttribute(6); }

  public void setCounty(String county) throws SQLException
  { _struct.setAttribute(6, county); }


  public String getCountry() throws SQLException
  { return (String) _struct.getAttribute(7); }

  public void setCountry(String country) throws SQLException
  { _struct.setAttribute(7, country); }

}
