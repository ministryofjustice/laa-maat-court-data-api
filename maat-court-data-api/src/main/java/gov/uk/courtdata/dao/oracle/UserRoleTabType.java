package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableArray;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class UserRoleTabType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.USER_ROLE_TABTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.ARRAY;

  MutableArray _array;

private static final UserRoleTabType _UserRoleTabTypeFactory = new UserRoleTabType();

  public static ORADataFactory getORADataFactory()
  { return _UserRoleTabTypeFactory; }
  /* constructors */
  public UserRoleTabType()
  {
    this((UserRoleType[])null);
  }

  public UserRoleTabType(UserRoleType[] a)
  {
    _array = new MutableArray(2002, a, UserRoleType.getORADataFactory());
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _array.toDatum(c, _SQL_NAME);
  }

  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    UserRoleTabType a = new UserRoleTabType();
    a._array = new MutableArray(2002, (ARRAY) d, UserRoleType.getORADataFactory());
    return a;
  }

  public int length() throws SQLException
  {
    return _array.length();
  }

  public int getBaseType() throws SQLException
  {
    return _array.getBaseType();
  }

  public String getBaseTypeName() throws SQLException
  {
    return _array.getBaseTypeName();
  }

  public ArrayDescriptor getDescriptor() throws SQLException
  {
    return _array.getDescriptor();
  }

  /* array accessor methods */
  public UserRoleType[] getArray() throws SQLException
  {
    return (UserRoleType[]) _array.getObjectArray(
      new UserRoleType[_array.length()]);
  }

  public UserRoleType[] getArray(long index, int count) throws SQLException
  {
    return (UserRoleType[]) _array.getObjectArray(index,
      new UserRoleType[_array.sliceLength(index, count)]);
  }

  public void setArray(UserRoleType[] a) throws SQLException
  {
    _array.setObjectArray(a);
  }

  public void setArray(UserRoleType[] a, long index) throws SQLException
  {
    _array.setObjectArray(a, index);
  }

  public UserRoleType getElement(long index) throws SQLException
  {
    return (UserRoleType) _array.getObjectElement(index);
  }

  public void setElement(UserRoleType a, long index) throws SQLException
  {
    _array.setObjectElement(a, index);
  }

}
