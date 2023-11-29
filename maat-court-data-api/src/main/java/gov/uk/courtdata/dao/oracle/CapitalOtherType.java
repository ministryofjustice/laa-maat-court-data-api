package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class CapitalOtherType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.CAPITAL_OTHER_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,2002,12,2002,2,2,91,2003,2003,91,12,12,93,12,12,12 };
  protected static ORADataFactory[] _factory = new ORADataFactory[16];
  static
  {
    _factory[1] = CapitalTypeType.getORADataFactory();
    _factory[3] = AssStatusType.getORADataFactory();
    _factory[7] = CapitalEvidenceTabType.getORADataFactory();
    _factory[8] = ExtraEvidenceTabtype.getORADataFactory();
  }
  protected static final CapitalOtherType _CapitalOtherTypeFactory = new CapitalOtherType();

  public static ORADataFactory getORADataFactory()
  { return _CapitalOtherTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[16], _sqlType, _factory); }
  public CapitalOtherType()
  { _init_struct(true); }
  public CapitalOtherType(java.math.BigDecimal id, CapitalTypeType capitalTypeObject, String otherDescription, AssStatusType assetStatusObject, java.math.BigDecimal assetAmount, java.math.BigDecimal verifiedAmount, java.sql.Timestamp dateEntered, CapitalEvidenceTabType capitalEvidenceTab, ExtraEvidenceTabtype extraEvidenceTab, java.sql.Timestamp verifiedDate, String verifiedBy, String undeclared, java.sql.Timestamp timeStamp, String accountOwner, String bankName, String branch) throws SQLException
  { _init_struct(true);
    setId(id);
    setCapitalTypeObject(capitalTypeObject);
    setOtherDescription(otherDescription);
    setAssetStatusObject(assetStatusObject);
    setAssetAmount(assetAmount);
    setVerifiedAmount(verifiedAmount);
    setDateEntered(dateEntered);
    setCapitalEvidenceTab(capitalEvidenceTab);
    setExtraEvidenceTab(extraEvidenceTab);
    setVerifiedDate(verifiedDate);
    setVerifiedBy(verifiedBy);
    setUndeclared(undeclared);
    setTimeStamp(timeStamp);
    setAccountOwner(accountOwner);
    setBankName(bankName);
    setBranch(branch);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(CapitalOtherType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new CapitalOtherType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setId(java.math.BigDecimal id) throws SQLException
  { _struct.setAttribute(0, id); }


  public CapitalTypeType getCapitalTypeObject() throws SQLException
  { return (CapitalTypeType) _struct.getAttribute(1); }

  public void setCapitalTypeObject(CapitalTypeType capitalTypeObject) throws SQLException
  { _struct.setAttribute(1, capitalTypeObject); }


  public String getOtherDescription() throws SQLException
  { return (String) _struct.getAttribute(2); }

  public void setOtherDescription(String otherDescription) throws SQLException
  { _struct.setAttribute(2, otherDescription); }


  public AssStatusType getAssetStatusObject() throws SQLException
  { return (AssStatusType) _struct.getAttribute(3); }

  public void setAssetStatusObject(AssStatusType assetStatusObject) throws SQLException
  { _struct.setAttribute(3, assetStatusObject); }


  public java.math.BigDecimal getAssetAmount() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(4); }

  public void setAssetAmount(java.math.BigDecimal assetAmount) throws SQLException
  { _struct.setAttribute(4, assetAmount); }


  public java.math.BigDecimal getVerifiedAmount() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(5); }

  public void setVerifiedAmount(java.math.BigDecimal verifiedAmount) throws SQLException
  { _struct.setAttribute(5, verifiedAmount); }


  public java.sql.Timestamp getDateEntered() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(6); }

  public void setDateEntered(java.sql.Timestamp dateEntered) throws SQLException
  { _struct.setAttribute(6, dateEntered); }


  public CapitalEvidenceTabType getCapitalEvidenceTab() throws SQLException
  { return (CapitalEvidenceTabType) _struct.getAttribute(7); }

  public void setCapitalEvidenceTab(CapitalEvidenceTabType capitalEvidenceTab) throws SQLException
  { _struct.setAttribute(7, capitalEvidenceTab); }


  public ExtraEvidenceTabtype getExtraEvidenceTab() throws SQLException
  { return (ExtraEvidenceTabtype) _struct.getAttribute(8); }

  public void setExtraEvidenceTab(ExtraEvidenceTabtype extraEvidenceTab) throws SQLException
  { _struct.setAttribute(8, extraEvidenceTab); }


  public java.sql.Timestamp getVerifiedDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(9); }

  public void setVerifiedDate(java.sql.Timestamp verifiedDate) throws SQLException
  { _struct.setAttribute(9, verifiedDate); }


  public String getVerifiedBy() throws SQLException
  { return (String) _struct.getAttribute(10); }

  public void setVerifiedBy(String verifiedBy) throws SQLException
  { _struct.setAttribute(10, verifiedBy); }


  public String getUndeclared() throws SQLException
  { return (String) _struct.getAttribute(11); }

  public void setUndeclared(String undeclared) throws SQLException
  { _struct.setAttribute(11, undeclared); }


  public java.sql.Timestamp getTimeStamp() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(12); }

  public void setTimeStamp(java.sql.Timestamp timeStamp) throws SQLException
  { _struct.setAttribute(12, timeStamp); }


  public String getAccountOwner() throws SQLException
  { return (String) _struct.getAttribute(13); }

  public void setAccountOwner(String accountOwner) throws SQLException
  { _struct.setAttribute(13, accountOwner); }


  public String getBankName() throws SQLException
  { return (String) _struct.getAttribute(14); }

  public void setBankName(String bankName) throws SQLException
  { _struct.setAttribute(14, bankName); }


  public String getBranch() throws SQLException
  { return (String) _struct.getAttribute(15); }

  public void setBranch(String branch) throws SQLException
  { _struct.setAttribute(15, branch); }

}
