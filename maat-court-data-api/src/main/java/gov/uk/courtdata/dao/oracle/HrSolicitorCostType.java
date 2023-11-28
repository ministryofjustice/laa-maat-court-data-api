package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class HrSolicitorCostType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.HR_SOLICITOR_COSTS_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,2,2,2,2 };
  protected static ORADataFactory[] _factory = new ORADataFactory[5];
  protected static final HrSolicitorCostType _HrSolicitorCostTypeFactory = new HrSolicitorCostType();

  public static ORADataFactory getORADataFactory()
  { return _HrSolicitorCostTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[5], _sqlType, _factory); }
  public HrSolicitorCostType()
  { _init_struct(true); }
  public HrSolicitorCostType(java.math.BigDecimal solicitorRate, java.math.BigDecimal solicitorHours, java.math.BigDecimal solicitorVat, java.math.BigDecimal solicitorDisb, java.math.BigDecimal solicitorEstTotalCost) throws SQLException
  { _init_struct(true);
    setSolicitorRate(solicitorRate);
    setSolicitorHours(solicitorHours);
    setSolicitorVat(solicitorVat);
    setSolicitorDisb(solicitorDisb);
    setSolicitorEstTotalCost(solicitorEstTotalCost);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(HrSolicitorCostType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new HrSolicitorCostType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getSolicitorRate() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setSolicitorRate(java.math.BigDecimal solicitorRate) throws SQLException
  { _struct.setAttribute(0, solicitorRate); }


  public java.math.BigDecimal getSolicitorHours() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(1); }

  public void setSolicitorHours(java.math.BigDecimal solicitorHours) throws SQLException
  { _struct.setAttribute(1, solicitorHours); }


  public java.math.BigDecimal getSolicitorVat() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(2); }

  public void setSolicitorVat(java.math.BigDecimal solicitorVat) throws SQLException
  { _struct.setAttribute(2, solicitorVat); }


  public java.math.BigDecimal getSolicitorDisb() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(3); }

  public void setSolicitorDisb(java.math.BigDecimal solicitorDisb) throws SQLException
  { _struct.setAttribute(3, solicitorDisb); }


  public java.math.BigDecimal getSolicitorEstTotalCost() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(4); }

  public void setSolicitorEstTotalCost(java.math.BigDecimal solicitorEstTotalCost) throws SQLException
  { _struct.setAttribute(4, solicitorEstTotalCost); }

}
