package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class CapitalEquityType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.CAPITAL_EQUITY_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 12,12,12,12,12,12,2,2,2,2003,2003,2003,2002,2002 };
  protected static ORADataFactory[] _factory = new ORADataFactory[14];
  static
  {
    _factory[9] = EquityTabtype.getORADataFactory();
    _factory[10] = CapitalPropertiesTabType.getORADataFactory();
    _factory[11] = CapitalOtherTabType.getORADataFactory();
    _factory[12] = CapitalEvidenceSummaryType.getORADataFactory();
    _factory[13] = MotorVehicleOwnerType.getORADataFactory();
  }
  protected static final CapitalEquityType _CapitalEquityTypeFactory = new CapitalEquityType();

  public static ORADataFactory getORADataFactory()
  { return _CapitalEquityTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[14], _sqlType, _factory); }
  public CapitalEquityType()
  { _init_struct(true); }
  public CapitalEquityType(String available, String noCapitalDeclared, String sufficientVeriToCoverCase, String sufficientDeclToCoverCase, String verifiedEquityToCoverCase, String declaredEquityToCoverCase, java.math.BigDecimal totalCapital, java.math.BigDecimal totalEquity, java.math.BigDecimal totalCapitalAndEquity, EquityTabtype equityTab, CapitalPropertiesTabType capitalPropertiesTab, CapitalOtherTabType capitalOtherTab, CapitalEvidenceSummaryType capitalEvidenceSummaryObj, MotorVehicleOwnerType motorVehicleOwnerObj) throws SQLException
  { _init_struct(true);
    setAvailable(available);
    setNoCapitalDeclared(noCapitalDeclared);
    setSufficientVeriToCoverCase(sufficientVeriToCoverCase);
    setSufficientDeclToCoverCase(sufficientDeclToCoverCase);
    setVerifiedEquityToCoverCase(verifiedEquityToCoverCase);
    setDeclaredEquityToCoverCase(declaredEquityToCoverCase);
    setTotalCapital(totalCapital);
    setTotalEquity(totalEquity);
    setTotalCapitalAndEquity(totalCapitalAndEquity);
    setEquityTab(equityTab);
    setCapitalPropertiesTab(capitalPropertiesTab);
    setCapitalOtherTab(capitalOtherTab);
    setCapitalEvidenceSummaryObj(capitalEvidenceSummaryObj);
    setMotorVehicleOwnerObj(motorVehicleOwnerObj);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(CapitalEquityType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new CapitalEquityType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public String getAvailable() throws SQLException
  { return (String) _struct.getAttribute(0); }

  public void setAvailable(String available) throws SQLException
  { _struct.setAttribute(0, available); }


  public String getNoCapitalDeclared() throws SQLException
  { return (String) _struct.getAttribute(1); }

  public void setNoCapitalDeclared(String noCapitalDeclared) throws SQLException
  { _struct.setAttribute(1, noCapitalDeclared); }


  public String getSufficientVeriToCoverCase() throws SQLException
  { return (String) _struct.getAttribute(2); }

  public void setSufficientVeriToCoverCase(String sufficientVeriToCoverCase) throws SQLException
  { _struct.setAttribute(2, sufficientVeriToCoverCase); }


  public String getSufficientDeclToCoverCase() throws SQLException
  { return (String) _struct.getAttribute(3); }

  public void setSufficientDeclToCoverCase(String sufficientDeclToCoverCase) throws SQLException
  { _struct.setAttribute(3, sufficientDeclToCoverCase); }


  public String getVerifiedEquityToCoverCase() throws SQLException
  { return (String) _struct.getAttribute(4); }

  public void setVerifiedEquityToCoverCase(String verifiedEquityToCoverCase) throws SQLException
  { _struct.setAttribute(4, verifiedEquityToCoverCase); }


  public String getDeclaredEquityToCoverCase() throws SQLException
  { return (String) _struct.getAttribute(5); }

  public void setDeclaredEquityToCoverCase(String declaredEquityToCoverCase) throws SQLException
  { _struct.setAttribute(5, declaredEquityToCoverCase); }


  public java.math.BigDecimal getTotalCapital() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(6); }

  public void setTotalCapital(java.math.BigDecimal totalCapital) throws SQLException
  { _struct.setAttribute(6, totalCapital); }


  public java.math.BigDecimal getTotalEquity() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(7); }

  public void setTotalEquity(java.math.BigDecimal totalEquity) throws SQLException
  { _struct.setAttribute(7, totalEquity); }


  public java.math.BigDecimal getTotalCapitalAndEquity() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(8); }

  public void setTotalCapitalAndEquity(java.math.BigDecimal totalCapitalAndEquity) throws SQLException
  { _struct.setAttribute(8, totalCapitalAndEquity); }


  public EquityTabtype getEquityTab() throws SQLException
  { return (EquityTabtype) _struct.getAttribute(9); }

  public void setEquityTab(EquityTabtype equityTab) throws SQLException
  { _struct.setAttribute(9, equityTab); }


  public CapitalPropertiesTabType getCapitalPropertiesTab() throws SQLException
  { return (CapitalPropertiesTabType) _struct.getAttribute(10); }

  public void setCapitalPropertiesTab(CapitalPropertiesTabType capitalPropertiesTab) throws SQLException
  { _struct.setAttribute(10, capitalPropertiesTab); }


  public CapitalOtherTabType getCapitalOtherTab() throws SQLException
  { return (CapitalOtherTabType) _struct.getAttribute(11); }

  public void setCapitalOtherTab(CapitalOtherTabType capitalOtherTab) throws SQLException
  { _struct.setAttribute(11, capitalOtherTab); }


  public CapitalEvidenceSummaryType getCapitalEvidenceSummaryObj() throws SQLException
  { return (CapitalEvidenceSummaryType) _struct.getAttribute(12); }

  public void setCapitalEvidenceSummaryObj(CapitalEvidenceSummaryType capitalEvidenceSummaryObj) throws SQLException
  { _struct.setAttribute(12, capitalEvidenceSummaryObj); }


  public MotorVehicleOwnerType getMotorVehicleOwnerObj() throws SQLException
  { return (MotorVehicleOwnerType) _struct.getAttribute(13); }

  public void setMotorVehicleOwnerObj(MotorVehicleOwnerType motorVehicleOwnerObj) throws SQLException
  { _struct.setAttribute(13, motorVehicleOwnerObj); }

}
