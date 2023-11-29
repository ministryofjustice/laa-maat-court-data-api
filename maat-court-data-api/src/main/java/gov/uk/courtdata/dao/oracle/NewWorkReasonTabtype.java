package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableArray;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class NewWorkReasonTabtype implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.NEW_WORK_REASON_TABTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.ARRAY;

  MutableArray _array;

private static final NewWorkReasonTabtype _NewWorkReasonTabtypeFactory = new NewWorkReasonTabtype();

  public static ORADataFactory getORADataFactory()
  { return _NewWorkReasonTabtypeFactory; }
  /* constructors */
  public NewWorkReasonTabtype()
  {
    this((NewWorkReasonType[])null);
  }

  public NewWorkReasonTabtype(NewWorkReasonType[] a)
  {
    _array = new MutableArray(2002, a, NewWorkReasonType.getORADataFactory());
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
    NewWorkReasonTabtype a = new NewWorkReasonTabtype();
    a._array = new MutableArray(2002, (ARRAY) d, NewWorkReasonType.getORADataFactory());
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
  public NewWorkReasonType[] getArray() throws SQLException
  {
    return (NewWorkReasonType[]) _array.getObjectArray(
      new NewWorkReasonType[_array.length()]);
  }

  public NewWorkReasonType[] getArray(long index, int count) throws SQLException
  {
    return (NewWorkReasonType[]) _array.getObjectArray(index,
      new NewWorkReasonType[_array.sliceLength(index, count)]);
  }

  public void setArray(NewWorkReasonType[] a) throws SQLException
  {
    _array.setObjectArray(a);
  }

  public void setArray(NewWorkReasonType[] a, long index) throws SQLException
  {
    _array.setObjectArray(a, index);
  }

  public NewWorkReasonType getElement(long index) throws SQLException
  {
    return (NewWorkReasonType) _array.getObjectElement(index);
  }

  public void setElement(NewWorkReasonType a, long index) throws SQLException
  {
    _array.setObjectElement(a, index);
  }

}
