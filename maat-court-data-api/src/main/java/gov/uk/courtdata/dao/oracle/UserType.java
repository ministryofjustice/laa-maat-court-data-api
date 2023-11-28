package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class UserType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.USER_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 12,12,12,12 };
  protected static ORADataFactory[] _factory = new ORADataFactory[4];
  protected static final UserType _UserTypeFactory = new UserType();

  public static ORADataFactory getORADataFactory()
  { return _UserTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[4], _sqlType, _factory); }
  public UserType()
  { _init_struct(true); }
  public UserType(String userName, String firstName, String initials, String lastName) throws SQLException
  { _init_struct(true);
    setUserName(userName);
    setFirstName(firstName);
    setInitials(initials);
    setLastName(lastName);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(UserType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new UserType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public String getUserName() throws SQLException
  { return (String) _struct.getAttribute(0); }

  public void setUserName(String userName) throws SQLException
  { _struct.setAttribute(0, userName); }


  public String getFirstName() throws SQLException
  { return (String) _struct.getAttribute(1); }

  public void setFirstName(String firstName) throws SQLException
  { _struct.setAttribute(1, firstName); }


  public String getInitials() throws SQLException
  { return (String) _struct.getAttribute(2); }

  public void setInitials(String initials) throws SQLException
  { _struct.setAttribute(2, initials); }


  public String getLastName() throws SQLException
  { return (String) _struct.getAttribute(3); }

  public void setLastName(String lastName) throws SQLException
  { _struct.setAttribute(3, lastName); }

}
