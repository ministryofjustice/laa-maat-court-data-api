package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableArray;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class DigiMeansAssessTabtype implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.DIGI_MEANS_ASSESS_TABTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.ARRAY;

  MutableArray _array;

private static final DigiMeansAssessTabtype _DigiMeansAssessTabtypeFactory = new DigiMeansAssessTabtype();

  public static ORADataFactory getORADataFactory()
  { return _DigiMeansAssessTabtypeFactory; }
  /* constructors */
  public DigiMeansAssessTabtype()
  {
    this((DigiMeansAssessType[])null);
  }

  public DigiMeansAssessTabtype(DigiMeansAssessType[] a)
  {
    _array = new MutableArray(2002, a, DigiMeansAssessType.getORADataFactory());
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
    DigiMeansAssessTabtype a = new DigiMeansAssessTabtype();
    a._array = new MutableArray(2002, (ARRAY) d, DigiMeansAssessType.getORADataFactory());
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
  public DigiMeansAssessType[] getArray() throws SQLException
  {
    return (DigiMeansAssessType[]) _array.getObjectArray(
      new DigiMeansAssessType[_array.length()]);
  }

  public DigiMeansAssessType[] getArray(long index, int count) throws SQLException
  {
    return (DigiMeansAssessType[]) _array.getObjectArray(index,
      new DigiMeansAssessType[_array.sliceLength(index, count)]);
  }

  public void setArray(DigiMeansAssessType[] a) throws SQLException
  {
    _array.setObjectArray(a);
  }

  public void setArray(DigiMeansAssessType[] a, long index) throws SQLException
  {
    _array.setObjectArray(a, index);
  }

  public DigiMeansAssessType getElement(long index) throws SQLException
  {
    return (DigiMeansAssessType) _array.getObjectElement(index);
  }

  public void setElement(DigiMeansAssessType a, long index) throws SQLException
  {
    _array.setObjectElement(a, index);
  }

}
