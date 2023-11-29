package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class NewWorkReasonType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.NEW_WORK_REASON_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 12,12,12 };
  protected static ORADataFactory[] _factory = new ORADataFactory[3];
  protected static final NewWorkReasonType _NewWorkReasonTypeFactory = new NewWorkReasonType();

  public static ORADataFactory getORADataFactory()
  { return _NewWorkReasonTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[3], _sqlType, _factory); }
  public NewWorkReasonType()
  { _init_struct(true); }
  public NewWorkReasonType(String code, String type, String description) throws SQLException
  { _init_struct(true);
    setCode(code);
    setType(type);
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
  protected ORAData create(NewWorkReasonType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new NewWorkReasonType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public String getCode() throws SQLException
  { return (String) _struct.getAttribute(0); }

  public void setCode(String code) throws SQLException
  { _struct.setAttribute(0, code); }


  public String getType() throws SQLException
  { return (String) _struct.getAttribute(1); }

  public void setType(String type) throws SQLException
  { _struct.setAttribute(1, type); }


  public String getDescription() throws SQLException
  { return (String) _struct.getAttribute(2); }

  public void setDescription(String description) throws SQLException
  { _struct.setAttribute(2, description); }

}
