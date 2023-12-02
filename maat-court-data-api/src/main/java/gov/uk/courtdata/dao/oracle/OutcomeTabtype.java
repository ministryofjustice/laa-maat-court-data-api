package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableArray;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class OutcomeTabtype implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.OUTCOME_TABTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.ARRAY;

  MutableArray _array;

private static final OutcomeTabtype _OutcomeTabtypeFactory = new OutcomeTabtype();

  public static ORADataFactory getORADataFactory()
  { return _OutcomeTabtypeFactory; }
  /* constructors */
  public OutcomeTabtype()
  {
    this((OutcomeType[])null);
  }

  public OutcomeTabtype(OutcomeType[] a)
  {
    _array = new MutableArray(2002, a, OutcomeType.getORADataFactory());
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
    OutcomeTabtype a = new OutcomeTabtype();
    a._array = new MutableArray(2002, (ARRAY) d, OutcomeType.getORADataFactory());
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
  public OutcomeType[] getArray() throws SQLException
  {
    return (OutcomeType[]) _array.getObjectArray(
      new OutcomeType[_array.length()]);
  }

  public OutcomeType[] getArray(long index, int count) throws SQLException
  {
    return (OutcomeType[]) _array.getObjectArray(index,
      new OutcomeType[_array.sliceLength(index, count)]);
  }

  public void setArray(OutcomeType[] a) throws SQLException
  {
    _array.setObjectArray(a);
  }

  public void setArray(OutcomeType[] a, long index) throws SQLException
  {
    _array.setObjectArray(a, index);
  }

  public OutcomeType getElement(long index) throws SQLException
  {
    return (OutcomeType) _array.getObjectElement(index);
  }

  public void setElement(OutcomeType a, long index) throws SQLException
  {
    _array.setObjectElement(a, index);
  }

}
