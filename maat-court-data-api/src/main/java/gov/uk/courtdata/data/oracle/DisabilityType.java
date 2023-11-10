package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class DisabilityType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.DISABILITY_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,12,12,93 };
  protected static ORADataFactory[] _factory = new ORADataFactory[4];
  protected static final DisabilityType _DisabilityTypeFactory = new DisabilityType();

  public static ORADataFactory getORADataFactory()
  { return _DisabilityTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[4], _sqlType, _factory); }
  public DisabilityType()
  { _init_struct(true); }
  public DisabilityType(java.math.BigDecimal id, String disability, String description, java.sql.Timestamp timeStamp) throws SQLException
  { _init_struct(true);
    setId(id);
    setDisability(disability);
    setDescription(description);
    setTimeStamp(timeStamp);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(DisabilityType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new DisabilityType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setId(java.math.BigDecimal id) throws SQLException
  { _struct.setAttribute(0, id); }


  public String getDisability() throws SQLException
  { return (String) _struct.getAttribute(1); }

  public void setDisability(String disability) throws SQLException
  { _struct.setAttribute(1, disability); }


  public String getDescription() throws SQLException
  { return (String) _struct.getAttribute(2); }

  public void setDescription(String description) throws SQLException
  { _struct.setAttribute(2, description); }


  public java.sql.Timestamp getTimeStamp() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(3); }

  public void setTimeStamp(java.sql.Timestamp timeStamp) throws SQLException
  { _struct.setAttribute(3, timeStamp); }

}
