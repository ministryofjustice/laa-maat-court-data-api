package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class UserQueryType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.USER_QUERY_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,12,12,12 };
  protected static ORADataFactory[] _factory = new ORADataFactory[4];
  protected static final UserQueryType _UserQueryTypeFactory = new UserQueryType();

  public static ORADataFactory getORADataFactory()
  { return _UserQueryTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[4], _sqlType, _factory); }
  public UserQueryType()
  { _init_struct(true); }
  public UserQueryType(java.math.BigDecimal areaId, String userName, String firstName, String surname) throws SQLException
  { _init_struct(true);
    setAreaId(areaId);
    setUserName(userName);
    setFirstName(firstName);
    setSurname(surname);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(UserQueryType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new UserQueryType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getAreaId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setAreaId(java.math.BigDecimal areaId) throws SQLException
  { _struct.setAttribute(0, areaId); }


  public String getUserName() throws SQLException
  { return (String) _struct.getAttribute(1); }

  public void setUserName(String userName) throws SQLException
  { _struct.setAttribute(1, userName); }


  public String getFirstName() throws SQLException
  { return (String) _struct.getAttribute(2); }

  public void setFirstName(String firstName) throws SQLException
  { _struct.setAttribute(2, firstName); }


  public String getSurname() throws SQLException
  { return (String) _struct.getAttribute(3); }

  public void setSurname(String surname) throws SQLException
  { _struct.setAttribute(3, surname); }

}
