package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class MotorVehicleOwnerTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.MOTOR_VEHICLE_OWNER_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final MotorVehicleOwnerTypeRef _MotorVehicleOwnerTypeRefFactory = new MotorVehicleOwnerTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _MotorVehicleOwnerTypeRefFactory; }
  /* constructor */
  public MotorVehicleOwnerTypeRef()
  {
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _ref;
  }

  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    MotorVehicleOwnerTypeRef r = new MotorVehicleOwnerTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static MotorVehicleOwnerTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (MotorVehicleOwnerTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to MotorVehicleOwnerTypeRef: "+exn.toString()); }
  }

  public MotorVehicleOwnerType getValue() throws SQLException
  {
     return (MotorVehicleOwnerType) MotorVehicleOwnerType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(MotorVehicleOwnerType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
