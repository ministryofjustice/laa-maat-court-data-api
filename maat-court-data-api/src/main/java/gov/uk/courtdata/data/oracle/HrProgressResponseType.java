package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class HrProgressResponseType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.HR_PROGRESS_RESPONSE_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 12,12 };
  protected static ORADataFactory[] _factory = new ORADataFactory[2];
  protected static final HrProgressResponseType _HrProgressResponseTypeFactory = new HrProgressResponseType();

  public static ORADataFactory getORADataFactory()
  { return _HrProgressResponseTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[2], _sqlType, _factory); }
  public HrProgressResponseType()
  { _init_struct(true); }
  public HrProgressResponseType(String response, String description) throws SQLException
  { _init_struct(true);
    setResponse(response);
    setDescription(description);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(HrProgressResponseType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new HrProgressResponseType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public String getResponse() throws SQLException
  { return (String) _struct.getAttribute(0); }

  public void setResponse(String response) throws SQLException
  { _struct.setAttribute(0, response); }


  public String getDescription() throws SQLException
  { return (String) _struct.getAttribute(1); }

  public void setDescription(String description) throws SQLException
  { _struct.setAttribute(1, description); }

}
