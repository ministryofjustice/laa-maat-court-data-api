package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class FdcItemsType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.FDC_ITEMS_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,91,12,12,12,2,2,12,12,12,12 };
  protected static ORADataFactory[] _factory = new ORADataFactory[11];
  protected static final FdcItemsType _FdcItemsTypeFactory = new FdcItemsType();

  public static ORADataFactory getORADataFactory()
  { return _FdcItemsTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[11], _sqlType, _factory); }
  public FdcItemsType()
  { _init_struct(true); }
  public FdcItemsType(java.math.BigDecimal id, java.sql.Timestamp dateCreated, String caseNumber, String courtCode, String supplierCode, java.math.BigDecimal cost, java.math.BigDecimal vat, String adjustmentReason, String itemType, String paidAsClaimed, String latest) throws SQLException
  { _init_struct(true);
    setId(id);
    setDateCreated(dateCreated);
    setCaseNumber(caseNumber);
    setCourtCode(courtCode);
    setSupplierCode(supplierCode);
    setCost(cost);
    setVat(vat);
    setAdjustmentReason(adjustmentReason);
    setItemType(itemType);
    setPaidAsClaimed(paidAsClaimed);
    setLatest(latest);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(FdcItemsType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new FdcItemsType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setId(java.math.BigDecimal id) throws SQLException
  { _struct.setAttribute(0, id); }


  public java.sql.Timestamp getDateCreated() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(1); }

  public void setDateCreated(java.sql.Timestamp dateCreated) throws SQLException
  { _struct.setAttribute(1, dateCreated); }


  public String getCaseNumber() throws SQLException
  { return (String) _struct.getAttribute(2); }

  public void setCaseNumber(String caseNumber) throws SQLException
  { _struct.setAttribute(2, caseNumber); }


  public String getCourtCode() throws SQLException
  { return (String) _struct.getAttribute(3); }

  public void setCourtCode(String courtCode) throws SQLException
  { _struct.setAttribute(3, courtCode); }


  public String getSupplierCode() throws SQLException
  { return (String) _struct.getAttribute(4); }

  public void setSupplierCode(String supplierCode) throws SQLException
  { _struct.setAttribute(4, supplierCode); }


  public java.math.BigDecimal getCost() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(5); }

  public void setCost(java.math.BigDecimal cost) throws SQLException
  { _struct.setAttribute(5, cost); }


  public java.math.BigDecimal getVat() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(6); }

  public void setVat(java.math.BigDecimal vat) throws SQLException
  { _struct.setAttribute(6, vat); }


  public String getAdjustmentReason() throws SQLException
  { return (String) _struct.getAttribute(7); }

  public void setAdjustmentReason(String adjustmentReason) throws SQLException
  { _struct.setAttribute(7, adjustmentReason); }


  public String getItemType() throws SQLException
  { return (String) _struct.getAttribute(8); }

  public void setItemType(String itemType) throws SQLException
  { _struct.setAttribute(8, itemType); }


  public String getPaidAsClaimed() throws SQLException
  { return (String) _struct.getAttribute(9); }

  public void setPaidAsClaimed(String paidAsClaimed) throws SQLException
  { _struct.setAttribute(9, paidAsClaimed); }


  public String getLatest() throws SQLException
  { return (String) _struct.getAttribute(10); }

  public void setLatest(String latest) throws SQLException
  { _struct.setAttribute(10, latest); }

}
