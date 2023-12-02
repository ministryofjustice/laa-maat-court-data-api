package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableArray;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class Parametertabtype implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "EMI_DATA.PARAMETERTABTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.ARRAY;

  MutableArray _array;

private static final Parametertabtype _ParametertabtypeFactory = new Parametertabtype();

  public static ORADataFactory getORADataFactory()
  { return _ParametertabtypeFactory; }
  /* constructors */
  public Parametertabtype()
  {
    this((Parametertype[])null);
  }

  public Parametertabtype(Parametertype[] a)
  {
    _array = new MutableArray(2002, a, Parametertype.getORADataFactory());
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
    Parametertabtype a = new Parametertabtype();
    a._array = new MutableArray(2002, (ARRAY) d, Parametertype.getORADataFactory());
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
  public Parametertype[] getArray() throws SQLException
  {
    return (Parametertype[]) _array.getObjectArray(
      new Parametertype[_array.length()]);
  }

  public Parametertype[] getArray(long index, int count) throws SQLException
  {
    return (Parametertype[]) _array.getObjectArray(index,
      new Parametertype[_array.sliceLength(index, count)]);
  }

  public void setArray(Parametertype[] a) throws SQLException
  {
    _array.setObjectArray(a);
  }

  public void setArray(Parametertype[] a, long index) throws SQLException
  {
    _array.setObjectArray(a, index);
  }

  public Parametertype getElement(long index) throws SQLException
  {
    return (Parametertype) _array.getObjectElement(index);
  }

  public void setElement(Parametertype a, long index) throws SQLException
  {
    _array.setObjectElement(a, index);
  }

}
