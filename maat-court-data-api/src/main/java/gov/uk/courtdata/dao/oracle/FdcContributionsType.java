package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class FdcContributionsType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.FDC_CONTRIBUTIONS_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,2,91,91,91,12,12,12,2,2,2,2,2,2,2003,2003,2002,2003,2,12 };
  protected static ORADataFactory[] _factory = new ORADataFactory[20];
  static
  {
    _factory[14] = FdcItemsTabtype.getORADataFactory();
    _factory[15] = FdcItemsTabtype.getORADataFactory();
    _factory[16] = DrcFileType.getORADataFactory();
    _factory[17] = FdcNotesTabtype.getORADataFactory();
  }
  protected static final FdcContributionsType _FdcContributionsTypeFactory = new FdcContributionsType();

  public static ORADataFactory getORADataFactory()
  { return _FdcContributionsTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[20], _sqlType, _factory); }
  public FdcContributionsType()
  { _init_struct(true); }
  public FdcContributionsType(java.math.BigDecimal id, java.math.BigDecimal repId, java.sql.Timestamp dateCreated, java.sql.Timestamp dateCalculated, java.sql.Timestamp dateReplaced, String status, String lgfsComplete, String agfsComplete, java.math.BigDecimal finalCost, java.math.BigDecimal vat, java.math.BigDecimal lgfsCost, java.math.BigDecimal agfsCost, java.math.BigDecimal lgfsVat, java.math.BigDecimal agfsVat, FdcItemsTabtype lgfsCosts, FdcItemsTabtype agfsCosts, DrcFileType drcFile, FdcNotesTabtype notesTab, java.math.BigDecimal jaPercent, String accelerate) throws SQLException
  { _init_struct(true);
    setId(id);
    setRepId(repId);
    setDateCreated(dateCreated);
    setDateCalculated(dateCalculated);
    setDateReplaced(dateReplaced);
    setStatus(status);
    setLgfsComplete(lgfsComplete);
    setAgfsComplete(agfsComplete);
    setFinalCost(finalCost);
    setVat(vat);
    setLgfsCost(lgfsCost);
    setAgfsCost(agfsCost);
    setLgfsVat(lgfsVat);
    setAgfsVat(agfsVat);
    setLgfsCosts(lgfsCosts);
    setAgfsCosts(agfsCosts);
    setDrcFile(drcFile);
    setNotesTab(notesTab);
    setJaPercent(jaPercent);
    setAccelerate(accelerate);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(FdcContributionsType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new FdcContributionsType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setId(java.math.BigDecimal id) throws SQLException
  { _struct.setAttribute(0, id); }


  public java.math.BigDecimal getRepId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(1); }

  public void setRepId(java.math.BigDecimal repId) throws SQLException
  { _struct.setAttribute(1, repId); }


  public java.sql.Timestamp getDateCreated() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(2); }

  public void setDateCreated(java.sql.Timestamp dateCreated) throws SQLException
  { _struct.setAttribute(2, dateCreated); }


  public java.sql.Timestamp getDateCalculated() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(3); }

  public void setDateCalculated(java.sql.Timestamp dateCalculated) throws SQLException
  { _struct.setAttribute(3, dateCalculated); }


  public java.sql.Timestamp getDateReplaced() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(4); }

  public void setDateReplaced(java.sql.Timestamp dateReplaced) throws SQLException
  { _struct.setAttribute(4, dateReplaced); }


  public String getStatus() throws SQLException
  { return (String) _struct.getAttribute(5); }

  public void setStatus(String status) throws SQLException
  { _struct.setAttribute(5, status); }


  public String getLgfsComplete() throws SQLException
  { return (String) _struct.getAttribute(6); }

  public void setLgfsComplete(String lgfsComplete) throws SQLException
  { _struct.setAttribute(6, lgfsComplete); }


  public String getAgfsComplete() throws SQLException
  { return (String) _struct.getAttribute(7); }

  public void setAgfsComplete(String agfsComplete) throws SQLException
  { _struct.setAttribute(7, agfsComplete); }


  public java.math.BigDecimal getFinalCost() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(8); }

  public void setFinalCost(java.math.BigDecimal finalCost) throws SQLException
  { _struct.setAttribute(8, finalCost); }


  public java.math.BigDecimal getVat() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(9); }

  public void setVat(java.math.BigDecimal vat) throws SQLException
  { _struct.setAttribute(9, vat); }


  public java.math.BigDecimal getLgfsCost() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(10); }

  public void setLgfsCost(java.math.BigDecimal lgfsCost) throws SQLException
  { _struct.setAttribute(10, lgfsCost); }


  public java.math.BigDecimal getAgfsCost() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(11); }

  public void setAgfsCost(java.math.BigDecimal agfsCost) throws SQLException
  { _struct.setAttribute(11, agfsCost); }


  public java.math.BigDecimal getLgfsVat() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(12); }

  public void setLgfsVat(java.math.BigDecimal lgfsVat) throws SQLException
  { _struct.setAttribute(12, lgfsVat); }


  public java.math.BigDecimal getAgfsVat() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(13); }

  public void setAgfsVat(java.math.BigDecimal agfsVat) throws SQLException
  { _struct.setAttribute(13, agfsVat); }


  public FdcItemsTabtype getLgfsCosts() throws SQLException
  { return (FdcItemsTabtype) _struct.getAttribute(14); }

  public void setLgfsCosts(FdcItemsTabtype lgfsCosts) throws SQLException
  { _struct.setAttribute(14, lgfsCosts); }


  public FdcItemsTabtype getAgfsCosts() throws SQLException
  { return (FdcItemsTabtype) _struct.getAttribute(15); }

  public void setAgfsCosts(FdcItemsTabtype agfsCosts) throws SQLException
  { _struct.setAttribute(15, agfsCosts); }


  public DrcFileType getDrcFile() throws SQLException
  { return (DrcFileType) _struct.getAttribute(16); }

  public void setDrcFile(DrcFileType drcFile) throws SQLException
  { _struct.setAttribute(16, drcFile); }


  public FdcNotesTabtype getNotesTab() throws SQLException
  { return (FdcNotesTabtype) _struct.getAttribute(17); }

  public void setNotesTab(FdcNotesTabtype notesTab) throws SQLException
  { _struct.setAttribute(17, notesTab); }


  public java.math.BigDecimal getJaPercent() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(18); }

  public void setJaPercent(java.math.BigDecimal jaPercent) throws SQLException
  { _struct.setAttribute(18, jaPercent); }


  public String getAccelerate() throws SQLException
  { return (String) _struct.getAttribute(19); }

  public void setAccelerate(String accelerate) throws SQLException
  { _struct.setAttribute(19, accelerate); }

}
