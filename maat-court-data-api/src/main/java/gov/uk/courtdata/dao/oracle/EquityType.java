package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class EquityType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.EQUITY_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,2002,2002,91,91,12,12,93 };
  protected static ORADataFactory[] _factory = new ORADataFactory[8];
  static
  {
    _factory[1] = PropertyType.getORADataFactory();
    _factory[2] = AssStatusType.getORADataFactory();
  }
  protected static final EquityType _EquityTypeFactory = new EquityType();

  public static ORADataFactory getORADataFactory()
  { return _EquityTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[8], _sqlType, _factory); }
  public EquityType()
  { _init_struct(true); }
  public EquityType(java.math.BigDecimal id, PropertyType propertyObject, AssStatusType assetStatusObject, java.sql.Timestamp dateEntered, java.sql.Timestamp verifiedDate, String verifiedBy, String undeclared, java.sql.Timestamp timeStamp) throws SQLException
  { _init_struct(true);
    setId(id);
    setPropertyObject(propertyObject);
    setAssetStatusObject(assetStatusObject);
    setDateEntered(dateEntered);
    setVerifiedDate(verifiedDate);
    setVerifiedBy(verifiedBy);
    setUndeclared(undeclared);
    setTimeStamp(timeStamp);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(EquityType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new EquityType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setId(java.math.BigDecimal id) throws SQLException
  { _struct.setAttribute(0, id); }


  public PropertyType getPropertyObject() throws SQLException
  { return (PropertyType) _struct.getAttribute(1); }

  public void setPropertyObject(PropertyType propertyObject) throws SQLException
  { _struct.setAttribute(1, propertyObject); }


  public AssStatusType getAssetStatusObject() throws SQLException
  { return (AssStatusType) _struct.getAttribute(2); }

  public void setAssetStatusObject(AssStatusType assetStatusObject) throws SQLException
  { _struct.setAttribute(2, assetStatusObject); }


  public java.sql.Timestamp getDateEntered() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(3); }

  public void setDateEntered(java.sql.Timestamp dateEntered) throws SQLException
  { _struct.setAttribute(3, dateEntered); }


  public java.sql.Timestamp getVerifiedDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(4); }

  public void setVerifiedDate(java.sql.Timestamp verifiedDate) throws SQLException
  { _struct.setAttribute(4, verifiedDate); }


  public String getVerifiedBy() throws SQLException
  { return (String) _struct.getAttribute(5); }

  public void setVerifiedBy(String verifiedBy) throws SQLException
  { _struct.setAttribute(5, verifiedBy); }


  public String getUndeclared() throws SQLException
  { return (String) _struct.getAttribute(6); }

  public void setUndeclared(String undeclared) throws SQLException
  { _struct.setAttribute(6, undeclared); }


  public java.sql.Timestamp getTimeStamp() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(7); }

  public void setTimeStamp(java.sql.Timestamp timeStamp) throws SQLException
  { _struct.setAttribute(7, timeStamp); }

}
