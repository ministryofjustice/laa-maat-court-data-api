package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableArray;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class ApplicantLinksTabtype implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.APPLICANT_LINKS_TABTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.ARRAY;

  MutableArray _array;

private static final ApplicantLinksTabtype _ApplicantLinksTabtypeFactory = new ApplicantLinksTabtype();

  public static ORADataFactory getORADataFactory()
  { return _ApplicantLinksTabtypeFactory; }
  /* constructors */
  public ApplicantLinksTabtype()
  {
    this((ApplicantLinkType[])null);
  }

  public ApplicantLinksTabtype(ApplicantLinkType[] a)
  {
    _array = new MutableArray(2002, a, ApplicantLinkType.getORADataFactory());
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
    ApplicantLinksTabtype a = new ApplicantLinksTabtype();
    a._array = new MutableArray(2002, (ARRAY) d, ApplicantLinkType.getORADataFactory());
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
  public ApplicantLinkType[] getArray() throws SQLException
  {
    return (ApplicantLinkType[]) _array.getObjectArray(
      new ApplicantLinkType[_array.length()]);
  }

  public ApplicantLinkType[] getArray(long index, int count) throws SQLException
  {
    return (ApplicantLinkType[]) _array.getObjectArray(index,
      new ApplicantLinkType[_array.sliceLength(index, count)]);
  }

  public void setArray(ApplicantLinkType[] a) throws SQLException
  {
    _array.setObjectArray(a);
  }

  public void setArray(ApplicantLinkType[] a, long index) throws SQLException
  {
    _array.setObjectArray(a, index);
  }

  public ApplicantLinkType getElement(long index) throws SQLException
  {
    return (ApplicantLinkType) _array.getObjectElement(index);
  }

  public void setElement(ApplicantLinkType a, long index) throws SQLException
  {
    _array.setObjectElement(a, index);
  }

}
