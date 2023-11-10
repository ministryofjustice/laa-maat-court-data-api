package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class HrSectionType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.HR_SECTION_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2002,2003,2,2,2 };
  protected static ORADataFactory[] _factory = new ORADataFactory[5];
  static
  {
    _factory[0] = HrDetailTypeType.getORADataFactory();
    _factory[1] = HRDetailTabType.getORADataFactory();
  }
  protected static final HrSectionType _HrSectionTypeFactory = new HrSectionType();

  public static ORADataFactory getORADataFactory()
  { return _HrSectionTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[5], _sqlType, _factory); }
  public HrSectionType()
  { _init_struct(true); }
  public HrSectionType(HrDetailTypeType hrDetailTypeObject, HRDetailTabType hrDetailTab, java.math.BigDecimal appAnnualTotal, java.math.BigDecimal partnerAnnualTotal, java.math.BigDecimal annualTotal) throws SQLException
  { _init_struct(true);
    setHrDetailTypeObject(hrDetailTypeObject);
    setHrDetailTab(hrDetailTab);
    setAppAnnualTotal(appAnnualTotal);
    setPartnerAnnualTotal(partnerAnnualTotal);
    setAnnualTotal(annualTotal);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(HrSectionType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new HrSectionType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public HrDetailTypeType getHrDetailTypeObject() throws SQLException
  { return (HrDetailTypeType) _struct.getAttribute(0); }

  public void setHrDetailTypeObject(HrDetailTypeType hrDetailTypeObject) throws SQLException
  { _struct.setAttribute(0, hrDetailTypeObject); }


  public HRDetailTabType getHrDetailTab() throws SQLException
  { return (HRDetailTabType) _struct.getAttribute(1); }

  public void setHrDetailTab(HRDetailTabType hrDetailTab) throws SQLException
  { _struct.setAttribute(1, hrDetailTab); }


  public java.math.BigDecimal getAppAnnualTotal() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(2); }

  public void setAppAnnualTotal(java.math.BigDecimal appAnnualTotal) throws SQLException
  { _struct.setAttribute(2, appAnnualTotal); }


  public java.math.BigDecimal getPartnerAnnualTotal() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(3); }

  public void setPartnerAnnualTotal(java.math.BigDecimal partnerAnnualTotal) throws SQLException
  { _struct.setAttribute(3, partnerAnnualTotal); }


  public java.math.BigDecimal getAnnualTotal() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(4); }

  public void setAnnualTotal(java.math.BigDecimal annualTotal) throws SQLException
  { _struct.setAttribute(4, annualTotal); }

}
