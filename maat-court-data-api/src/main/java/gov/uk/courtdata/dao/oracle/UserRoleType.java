package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class UserRoleType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.USER_ROLETYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 12,12 };
  protected static ORADataFactory[] _factory = new ORADataFactory[2];
  protected static final UserRoleType _UserRoleTypeFactory = new UserRoleType();

  public static ORADataFactory getORADataFactory()
  { return _UserRoleTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[2], _sqlType, _factory); }
  public UserRoleType()
  { _init_struct(true); }
  public UserRoleType(String roleName, String enabled) throws SQLException
  { _init_struct(true);
    setRoleName(roleName);
    setEnabled(enabled);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(UserRoleType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new UserRoleType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public String getRoleName() throws SQLException
  { return (String) _struct.getAttribute(0); }

  public void setRoleName(String roleName) throws SQLException
  { _struct.setAttribute(0, roleName); }


  public String getEnabled() throws SQLException
  { return (String) _struct.getAttribute(1); }

  public void setEnabled(String enabled) throws SQLException
  { _struct.setAttribute(1, enabled); }

}
