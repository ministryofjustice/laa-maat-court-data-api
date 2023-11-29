package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class AppSearchDetailsType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.APP_SEARCH_DETAILS_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 12,12,12,12 };
  protected static ORADataFactory[] _factory = new ORADataFactory[4];
  protected static final AppSearchDetailsType _AppSearchDetailsTypeFactory = new AppSearchDetailsType();

  public static ORADataFactory getORADataFactory()
  { return _AppSearchDetailsTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[4], _sqlType, _factory); }
  public AppSearchDetailsType()
  { _init_struct(true); }
  public AppSearchDetailsType(String parameter, String operator, String value, String andOr) throws SQLException
  { _init_struct(true);
    setParameter(parameter);
    setOperator(operator);
    setValue(value);
    setAndOr(andOr);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(AppSearchDetailsType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new AppSearchDetailsType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public String getParameter() throws SQLException
  { return (String) _struct.getAttribute(0); }

  public void setParameter(String parameter) throws SQLException
  { _struct.setAttribute(0, parameter); }


  public String getOperator() throws SQLException
  { return (String) _struct.getAttribute(1); }

  public void setOperator(String operator) throws SQLException
  { _struct.setAttribute(1, operator); }


  public String getValue() throws SQLException
  { return (String) _struct.getAttribute(2); }

  public void setValue(String value) throws SQLException
  { _struct.setAttribute(2, value); }


  public String getAndOr() throws SQLException
  { return (String) _struct.getAttribute(3); }

  public void setAndOr(String andOr) throws SQLException
  { _struct.setAttribute(3, andOr); }

}
