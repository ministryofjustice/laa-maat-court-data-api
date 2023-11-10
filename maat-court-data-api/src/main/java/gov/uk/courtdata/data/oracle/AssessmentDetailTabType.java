package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableArray;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class AssessmentDetailTabType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.ASSESSMENT_DETAIL_TABTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.ARRAY;

  MutableArray _array;

private static final AssessmentDetailTabType _AssessmentDetailTabTypeFactory = new AssessmentDetailTabType();

  public static ORADataFactory getORADataFactory()
  { return _AssessmentDetailTabTypeFactory; }
  /* constructors */
  public AssessmentDetailTabType()
  {
    this((AssessmentDetailType[])null);
  }

  public AssessmentDetailTabType(AssessmentDetailType[] a)
  {
    _array = new MutableArray(2002, a, AssessmentDetailType.getORADataFactory());
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
    AssessmentDetailTabType a = new AssessmentDetailTabType();
    a._array = new MutableArray(2002, (ARRAY) d, AssessmentDetailType.getORADataFactory());
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
  public AssessmentDetailType[] getArray() throws SQLException
  {
    return (AssessmentDetailType[]) _array.getObjectArray(
      new AssessmentDetailType[_array.length()]);
  }

  public AssessmentDetailType[] getArray(long index, int count) throws SQLException
  {
    return (AssessmentDetailType[]) _array.getObjectArray(index,
      new AssessmentDetailType[_array.sliceLength(index, count)]);
  }

  public void setArray(AssessmentDetailType[] a) throws SQLException
  {
    _array.setObjectArray(a);
  }

  public void setArray(AssessmentDetailType[] a, long index) throws SQLException
  {
    _array.setObjectArray(a, index);
  }

  public AssessmentDetailType getElement(long index) throws SQLException
  {
    return (AssessmentDetailType) _array.getObjectElement(index);
  }

  public void setElement(AssessmentDetailType a, long index) throws SQLException
  {
    _array.setObjectElement(a, index);
  }

}
