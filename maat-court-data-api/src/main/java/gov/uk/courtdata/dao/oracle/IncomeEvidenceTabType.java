package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableArray;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class IncomeEvidenceTabType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.INCOME_EVIDENCE_TABTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.ARRAY;

  MutableArray _array;

private static final IncomeEvidenceTabType _IncomeEvidenceTabTypeFactory = new IncomeEvidenceTabType();

  public static ORADataFactory getORADataFactory()
  { return _IncomeEvidenceTabTypeFactory; }
  /* constructors */
  public IncomeEvidenceTabType()
  {
    this((IncomeEvidenceType[])null);
  }

  public IncomeEvidenceTabType(IncomeEvidenceType[] a)
  {
    _array = new MutableArray(2002, a, IncomeEvidenceType.getORADataFactory());
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
    IncomeEvidenceTabType a = new IncomeEvidenceTabType();
    a._array = new MutableArray(2002, (ARRAY) d, IncomeEvidenceType.getORADataFactory());
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
  public IncomeEvidenceType[] getArray() throws SQLException
  {
    return (IncomeEvidenceType[]) _array.getObjectArray(
      new IncomeEvidenceType[_array.length()]);
  }

  public IncomeEvidenceType[] getArray(long index, int count) throws SQLException
  {
    return (IncomeEvidenceType[]) _array.getObjectArray(index,
      new IncomeEvidenceType[_array.sliceLength(index, count)]);
  }

  public void setArray(IncomeEvidenceType[] a) throws SQLException
  {
    _array.setObjectArray(a);
  }

  public void setArray(IncomeEvidenceType[] a, long index) throws SQLException
  {
    _array.setObjectArray(a, index);
  }

  public IncomeEvidenceType getElement(long index) throws SQLException
  {
    return (IncomeEvidenceType) _array.getObjectElement(index);
  }

  public void setElement(IncomeEvidenceType a, long index) throws SQLException
  {
    _array.setObjectElement(a, index);
  }

}
