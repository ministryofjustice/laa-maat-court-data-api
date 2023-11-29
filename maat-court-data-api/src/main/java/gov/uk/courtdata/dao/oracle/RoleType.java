package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class RoleType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.ROLETYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 12,12,93 };
  protected static ORADataFactory[] _factory = new ORADataFactory[3];
  protected static final RoleType _RoleTypeFactory = new RoleType();

  public static ORADataFactory getORADataFactory()
  { return _RoleTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[3], _sqlType, _factory); }
  public RoleType()
  { _init_struct(true); }
  public RoleType(String roleName, String roleDescription, java.sql.Timestamp timeStamp) throws SQLException
  { _init_struct(true);
    setRoleName(roleName);
    setRoleDescription(roleDescription);
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
  protected ORAData create(RoleType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new RoleType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public String getRoleName() throws SQLException
  { return (String) _struct.getAttribute(0); }

  public void setRoleName(String roleName) throws SQLException
  { _struct.setAttribute(0, roleName); }


  public String getRoleDescription() throws SQLException
  { return (String) _struct.getAttribute(1); }

  public void setRoleDescription(String roleDescription) throws SQLException
  { _struct.setAttribute(1, roleDescription); }


  public java.sql.Timestamp getTimeStamp() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(2); }

  public void setTimeStamp(java.sql.Timestamp timeStamp) throws SQLException
  { _struct.setAttribute(2, timeStamp); }

}
