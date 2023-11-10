package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableArray;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class ExtraEvidenceTabtype implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.EXTRA_EVIDENCE_TABTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.ARRAY;

  MutableArray _array;

private static final ExtraEvidenceTabtype _ExtraEvidenceTabtypeFactory = new ExtraEvidenceTabtype();

  public static ORADataFactory getORADataFactory()
  { return _ExtraEvidenceTabtypeFactory; }
  /* constructors */
  public ExtraEvidenceTabtype()
  {
    this((ExtraEvidenceType[])null);
  }

  public ExtraEvidenceTabtype(ExtraEvidenceType[] a)
  {
    _array = new MutableArray(2002, a, ExtraEvidenceType.getORADataFactory());
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
    ExtraEvidenceTabtype a = new ExtraEvidenceTabtype();
    a._array = new MutableArray(2002, (ARRAY) d, ExtraEvidenceType.getORADataFactory());
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
  public ExtraEvidenceType[] getArray() throws SQLException
  {
    return (ExtraEvidenceType[]) _array.getObjectArray(
      new ExtraEvidenceType[_array.length()]);
  }

  public ExtraEvidenceType[] getArray(long index, int count) throws SQLException
  {
    return (ExtraEvidenceType[]) _array.getObjectArray(index,
      new ExtraEvidenceType[_array.sliceLength(index, count)]);
  }

  public void setArray(ExtraEvidenceType[] a) throws SQLException
  {
    _array.setObjectArray(a);
  }

  public void setArray(ExtraEvidenceType[] a, long index) throws SQLException
  {
    _array.setObjectArray(a, index);
  }

  public ExtraEvidenceType getElement(long index) throws SQLException
  {
    return (ExtraEvidenceType) _array.getObjectElement(index);
  }

  public void setElement(ExtraEvidenceType a, long index) throws SQLException
  {
    _array.setObjectElement(a, index);
  }

}
