package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableArray;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class CapitalEvidenceTabType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.CAPITAL_EVIDENCE_TABTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.ARRAY;

  MutableArray _array;

private static final CapitalEvidenceTabType _CapitalEvidenceTabTypeFactory = new CapitalEvidenceTabType();

  public static ORADataFactory getORADataFactory()
  { return _CapitalEvidenceTabTypeFactory; }
  /* constructors */
  public CapitalEvidenceTabType()
  {
    this((CapitalEvidenceType[])null);
  }

  public CapitalEvidenceTabType(CapitalEvidenceType[] a)
  {
    _array = new MutableArray(2002, a, CapitalEvidenceType.getORADataFactory());
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
    CapitalEvidenceTabType a = new CapitalEvidenceTabType();
    a._array = new MutableArray(2002, (ARRAY) d, CapitalEvidenceType.getORADataFactory());
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
  public CapitalEvidenceType[] getArray() throws SQLException
  {
    return (CapitalEvidenceType[]) _array.getObjectArray(
      new CapitalEvidenceType[_array.length()]);
  }

  public CapitalEvidenceType[] getArray(long index, int count) throws SQLException
  {
    return (CapitalEvidenceType[]) _array.getObjectArray(index,
      new CapitalEvidenceType[_array.sliceLength(index, count)]);
  }

  public void setArray(CapitalEvidenceType[] a) throws SQLException
  {
    _array.setObjectArray(a);
  }

  public void setArray(CapitalEvidenceType[] a, long index) throws SQLException
  {
    _array.setObjectArray(a, index);
  }

  public CapitalEvidenceType getElement(long index) throws SQLException
  {
    return (CapitalEvidenceType) _array.getObjectElement(index);
  }

  public void setElement(CapitalEvidenceType a, long index) throws SQLException
  {
    _array.setObjectElement(a, index);
  }

}
