package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class MvoRegType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.MVO_REG_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 12 };
  protected static ORADataFactory[] _factory = new ORADataFactory[1];
  protected static final MvoRegType _MvoRegTypeFactory = new MvoRegType();

  public static ORADataFactory getORADataFactory()
  { return _MvoRegTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[1], _sqlType, _factory); }
  public MvoRegType()
  { _init_struct(true); }
  public MvoRegType(String registration) throws SQLException
  { _init_struct(true);
    setRegistration(registration);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(MvoRegType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new MvoRegType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public String getRegistration() throws SQLException
  { return (String) _struct.getAttribute(0); }

  public void setRegistration(String registration) throws SQLException
  { _struct.setAttribute(0, registration); }

}
