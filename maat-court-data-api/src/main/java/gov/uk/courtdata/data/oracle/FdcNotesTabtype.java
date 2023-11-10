package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableArray;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class FdcNotesTabtype implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.FDC_NOTES_TABTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.ARRAY;

  MutableArray _array;

private static final FdcNotesTabtype _FdcNotesTabtypeFactory = new FdcNotesTabtype();

  public static ORADataFactory getORADataFactory()
  { return _FdcNotesTabtypeFactory; }
  /* constructors */
  public FdcNotesTabtype()
  {
    this((FdcNotesType[])null);
  }

  public FdcNotesTabtype(FdcNotesType[] a)
  {
    _array = new MutableArray(2002, a, FdcNotesType.getORADataFactory());
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
    FdcNotesTabtype a = new FdcNotesTabtype();
    a._array = new MutableArray(2002, (ARRAY) d, FdcNotesType.getORADataFactory());
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
  public FdcNotesType[] getArray() throws SQLException
  {
    return (FdcNotesType[]) _array.getObjectArray(
      new FdcNotesType[_array.length()]);
  }

  public FdcNotesType[] getArray(long index, int count) throws SQLException
  {
    return (FdcNotesType[]) _array.getObjectArray(index,
      new FdcNotesType[_array.sliceLength(index, count)]);
  }

  public void setArray(FdcNotesType[] a) throws SQLException
  {
    _array.setObjectArray(a);
  }

  public void setArray(FdcNotesType[] a, long index) throws SQLException
  {
    _array.setObjectArray(a, index);
  }

  public FdcNotesType getElement(long index) throws SQLException
  {
    return (FdcNotesType) _array.getObjectElement(index);
  }

  public void setElement(FdcNotesType a, long index) throws SQLException
  {
    _array.setObjectElement(a, index);
  }

}
