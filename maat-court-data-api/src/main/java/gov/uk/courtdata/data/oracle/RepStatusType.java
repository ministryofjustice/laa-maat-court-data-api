package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class RepStatusType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.REP_STATUS_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 12,12,12,12 };
  protected static ORADataFactory[] _factory = new ORADataFactory[4];
  protected static final RepStatusType _RepStatusTypeFactory = new RepStatusType();

  public static ORADataFactory getORADataFactory()
  { return _RepStatusTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[4], _sqlType, _factory); }
  public RepStatusType()
  { _init_struct(true); }
  public RepStatusType(String status, String description, String updateAllowed, String removeContribs) throws SQLException
  { _init_struct(true);
    setStatus(status);
    setDescription(description);
    setUpdateAllowed(updateAllowed);
    setRemoveContribs(removeContribs);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(RepStatusType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new RepStatusType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public String getStatus() throws SQLException
  { return (String) _struct.getAttribute(0); }

  public void setStatus(String status) throws SQLException
  { _struct.setAttribute(0, status); }


  public String getDescription() throws SQLException
  { return (String) _struct.getAttribute(1); }

  public void setDescription(String description) throws SQLException
  { _struct.setAttribute(1, description); }


  public String getUpdateAllowed() throws SQLException
  { return (String) _struct.getAttribute(2); }

  public void setUpdateAllowed(String updateAllowed) throws SQLException
  { _struct.setAttribute(2, updateAllowed); }


  public String getRemoveContribs() throws SQLException
  { return (String) _struct.getAttribute(3); }

  public void setRemoveContribs(String removeContribs) throws SQLException
  { _struct.setAttribute(3, removeContribs); }

}
