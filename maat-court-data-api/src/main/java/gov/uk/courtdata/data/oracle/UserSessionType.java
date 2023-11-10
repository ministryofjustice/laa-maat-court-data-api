package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class UserSessionType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.USER_SESSION_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 12,12,12,12,12,12,91,12,12,2,93,2002,12,12,12,2003 };
  protected static ORADataFactory[] _factory = new ORADataFactory[16];
  static
  {
    _factory[11] = AreaType.getORADataFactory();
    _factory[15] = UserRoleTabType.getORADataFactory();
  }
  protected static final UserSessionType _UserSessionTypeFactory = new UserSessionType();

  public static ORADataFactory getORADataFactory()
  { return _UserSessionTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[16], _sqlType, _factory); }
  public UserSessionType()
  { _init_struct(true); }
  public UserSessionType(String userName, String firstName, String initials, String surname, String enabled, String password, java.sql.Timestamp passwordExpiry, String locked, String loggedIn, java.math.BigDecimal loggingInAttempts, java.sql.Timestamp timeStamp, AreaType areaObject, String userSession, String appName, String appServer, UserRoleTabType userRoleTab) throws SQLException
  { _init_struct(true);
    setUserName(userName);
    setFirstName(firstName);
    setInitials(initials);
    setSurname(surname);
    setEnabled(enabled);
    setPassword(password);
    setPasswordExpiry(passwordExpiry);
    setLocked(locked);
    setLoggedIn(loggedIn);
    setLoggingInAttempts(loggingInAttempts);
    setTimeStamp(timeStamp);
    setAreaObject(areaObject);
    setUserSession(userSession);
    setAppName(appName);
    setAppServer(appServer);
    setUserRoleTab(userRoleTab);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(UserSessionType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new UserSessionType();
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


  public String getSurname() throws SQLException
  { return (String) _struct.getAttribute(3); }

  public void setSurname(String surname) throws SQLException
  { _struct.setAttribute(3, surname); }


  public String getEnabled() throws SQLException
  { return (String) _struct.getAttribute(4); }

  public void setEnabled(String enabled) throws SQLException
  { _struct.setAttribute(4, enabled); }


  public String getPassword() throws SQLException
  { return (String) _struct.getAttribute(5); }

  public void setPassword(String password) throws SQLException
  { _struct.setAttribute(5, password); }


  public java.sql.Timestamp getPasswordExpiry() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(6); }

  public void setPasswordExpiry(java.sql.Timestamp passwordExpiry) throws SQLException
  { _struct.setAttribute(6, passwordExpiry); }


  public String getLocked() throws SQLException
  { return (String) _struct.getAttribute(7); }

  public void setLocked(String locked) throws SQLException
  { _struct.setAttribute(7, locked); }


  public String getLoggedIn() throws SQLException
  { return (String) _struct.getAttribute(8); }

  public void setLoggedIn(String loggedIn) throws SQLException
  { _struct.setAttribute(8, loggedIn); }


  public java.math.BigDecimal getLoggingInAttempts() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(9); }

  public void setLoggingInAttempts(java.math.BigDecimal loggingInAttempts) throws SQLException
  { _struct.setAttribute(9, loggingInAttempts); }


  public java.sql.Timestamp getTimeStamp() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(10); }

  public void setTimeStamp(java.sql.Timestamp timeStamp) throws SQLException
  { _struct.setAttribute(10, timeStamp); }


  public AreaType getAreaObject() throws SQLException
  { return (AreaType) _struct.getAttribute(11); }

  public void setAreaObject(AreaType areaObject) throws SQLException
  { _struct.setAttribute(11, areaObject); }


  public String getUserSession() throws SQLException
  { return (String) _struct.getAttribute(12); }

  public void setUserSession(String userSession) throws SQLException
  { _struct.setAttribute(12, userSession); }


  public String getAppName() throws SQLException
  { return (String) _struct.getAttribute(13); }

  public void setAppName(String appName) throws SQLException
  { _struct.setAttribute(13, appName); }


  public String getAppServer() throws SQLException
  { return (String) _struct.getAttribute(14); }

  public void setAppServer(String appServer) throws SQLException
  { _struct.setAttribute(14, appServer); }


  public UserRoleTabType getUserRoleTab() throws SQLException
  { return (UserRoleTabType) _struct.getAttribute(15); }

  public void setUserRoleTab(UserRoleTabType userRoleTab) throws SQLException
  { _struct.setAttribute(15, userRoleTab); }

}
