package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class SupplierType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.SUPPLIER_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 12,12,2002 };
  protected static ORADataFactory[] _factory = new ORADataFactory[3];
  static
  {
    _factory[2] = AddressType.getORADataFactory();
  }
  protected static final SupplierType _SupplierTypeFactory = new SupplierType();

  public static ORADataFactory getORADataFactory()
  { return _SupplierTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[3], _sqlType, _factory); }
  public SupplierType()
  { _init_struct(true); }
  public SupplierType(String accountCode, String accountName, AddressType addressObject) throws SQLException
  { _init_struct(true);
    setAccountCode(accountCode);
    setAccountName(accountName);
    setAddressObject(addressObject);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(SupplierType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new SupplierType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public String getAccountCode() throws SQLException
  { return (String) _struct.getAttribute(0); }

  public void setAccountCode(String accountCode) throws SQLException
  { _struct.setAttribute(0, accountCode); }


  public String getAccountName() throws SQLException
  { return (String) _struct.getAttribute(1); }

  public void setAccountName(String accountName) throws SQLException
  { _struct.setAttribute(1, accountName); }


  public AddressType getAddressObject() throws SQLException
  { return (AddressType) _struct.getAttribute(2); }

  public void setAddressObject(AddressType addressObject) throws SQLException
  { _struct.setAttribute(2, addressObject); }

}
