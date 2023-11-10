package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class MotorVehicleOwnerType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.MOTOR_VEHICLE_OWNER_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,12,2003,12 };
  protected static ORADataFactory[] _factory = new ORADataFactory[4];
  static
  {
    _factory[2] = MvoRegTabtype.getORADataFactory();
  }
  protected static final MotorVehicleOwnerType _MotorVehicleOwnerTypeFactory = new MotorVehicleOwnerType();

  public static ORADataFactory getORADataFactory()
  { return _MotorVehicleOwnerTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[4], _sqlType, _factory); }
  public MotorVehicleOwnerType()
  { _init_struct(true); }
  public MotorVehicleOwnerType(java.math.BigDecimal id, String noVehicleDeclared, MvoRegTabtype registrationTab, String available) throws SQLException
  { _init_struct(true);
    setId(id);
    setNoVehicleDeclared(noVehicleDeclared);
    setRegistrationTab(registrationTab);
    setAvailable(available);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(MotorVehicleOwnerType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new MotorVehicleOwnerType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setId(java.math.BigDecimal id) throws SQLException
  { _struct.setAttribute(0, id); }


  public String getNoVehicleDeclared() throws SQLException
  { return (String) _struct.getAttribute(1); }

  public void setNoVehicleDeclared(String noVehicleDeclared) throws SQLException
  { _struct.setAttribute(1, noVehicleDeclared); }


  public MvoRegTabtype getRegistrationTab() throws SQLException
  { return (MvoRegTabtype) _struct.getAttribute(2); }

  public void setRegistrationTab(MvoRegTabtype registrationTab) throws SQLException
  { _struct.setAttribute(2, registrationTab); }


  public String getAvailable() throws SQLException
  { return (String) _struct.getAttribute(3); }

  public void setAvailable(String available) throws SQLException
  { _struct.setAttribute(3, available); }

}
