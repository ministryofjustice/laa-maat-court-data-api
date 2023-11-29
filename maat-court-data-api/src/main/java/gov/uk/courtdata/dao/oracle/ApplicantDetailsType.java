package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class ApplicantDetailsType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.APPLICANT_DETAILS_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,12,12,12,12,91,12,12,12,2002,2003,2002,12,12,12,12,12,2002,2002,2,2002,91,93,12 };
  protected static ORADataFactory[] _factory = new ORADataFactory[24];
  static
  {
    _factory[9] = EthnicityType.getORADataFactory();
    _factory[10] = DisabilitiesTabtype.getORADataFactory();
    _factory[11] = EmpStatusType.getORADataFactory();
    _factory[17] = AddressType.getORADataFactory();
    _factory[18] = AddressType.getORADataFactory();
    _factory[20] = ApplPaymentDetailsType.getORADataFactory();
  }
  protected static final ApplicantDetailsType _ApplicantDetailsTypeFactory = new ApplicantDetailsType();

  public static ORADataFactory getORADataFactory()
  { return _ApplicantDetailsTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[24], _sqlType, _factory); }
  public ApplicantDetailsType()
  { _init_struct(true); }
  public ApplicantDetailsType(java.math.BigDecimal applId, String gender, String firstName, String lastName, String otherNames, java.sql.Timestamp dob, String niNumber, String foreignId, String disabled, EthnicityType ethnicityObject, DisabilitiesTabtype appDisabilitiesTab, EmpStatusType empStatusObject, String phoneMobile, String phoneHome, String email, String noFixedAbode, String useSuppAddrForPost, AddressType homeAddressObject, AddressType postAddressObject, java.math.BigDecimal aphiId, ApplPaymentDetailsType applPaymentDetailsObject, java.sql.Timestamp specialInvestigation, java.sql.Timestamp timeStamp, String phoneWork) throws SQLException
  { _init_struct(true);
    setApplId(applId);
    setGender(gender);
    setFirstName(firstName);
    setLastName(lastName);
    setOtherNames(otherNames);
    setDob(dob);
    setNiNumber(niNumber);
    setForeignId(foreignId);
    setDisabled(disabled);
    setEthnicityObject(ethnicityObject);
    setAppDisabilitiesTab(appDisabilitiesTab);
    setEmpStatusObject(empStatusObject);
    setPhoneMobile(phoneMobile);
    setPhoneHome(phoneHome);
    setEmail(email);
    setNoFixedAbode(noFixedAbode);
    setUseSuppAddrForPost(useSuppAddrForPost);
    setHomeAddressObject(homeAddressObject);
    setPostAddressObject(postAddressObject);
    setAphiId(aphiId);
    setApplPaymentDetailsObject(applPaymentDetailsObject);
    setSpecialInvestigation(specialInvestigation);
    setTimeStamp(timeStamp);
    setPhoneWork(phoneWork);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(ApplicantDetailsType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new ApplicantDetailsType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getApplId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setApplId(java.math.BigDecimal applId) throws SQLException
  { _struct.setAttribute(0, applId); }


  public String getGender() throws SQLException
  { return (String) _struct.getAttribute(1); }

  public void setGender(String gender) throws SQLException
  { _struct.setAttribute(1, gender); }


  public String getFirstName() throws SQLException
  { return (String) _struct.getAttribute(2); }

  public void setFirstName(String firstName) throws SQLException
  { _struct.setAttribute(2, firstName); }


  public String getLastName() throws SQLException
  { return (String) _struct.getAttribute(3); }

  public void setLastName(String lastName) throws SQLException
  { _struct.setAttribute(3, lastName); }


  public String getOtherNames() throws SQLException
  { return (String) _struct.getAttribute(4); }

  public void setOtherNames(String otherNames) throws SQLException
  { _struct.setAttribute(4, otherNames); }


  public java.sql.Timestamp getDob() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(5); }

  public void setDob(java.sql.Timestamp dob) throws SQLException
  { _struct.setAttribute(5, dob); }


  public String getNiNumber() throws SQLException
  { return (String) _struct.getAttribute(6); }

  public void setNiNumber(String niNumber) throws SQLException
  { _struct.setAttribute(6, niNumber); }


  public String getForeignId() throws SQLException
  { return (String) _struct.getAttribute(7); }

  public void setForeignId(String foreignId) throws SQLException
  { _struct.setAttribute(7, foreignId); }


  public String getDisabled() throws SQLException
  { return (String) _struct.getAttribute(8); }

  public void setDisabled(String disabled) throws SQLException
  { _struct.setAttribute(8, disabled); }


  public EthnicityType getEthnicityObject() throws SQLException
  { return (EthnicityType) _struct.getAttribute(9); }

  public void setEthnicityObject(EthnicityType ethnicityObject) throws SQLException
  { _struct.setAttribute(9, ethnicityObject); }


  public DisabilitiesTabtype getAppDisabilitiesTab() throws SQLException
  { return (DisabilitiesTabtype) _struct.getAttribute(10); }

  public void setAppDisabilitiesTab(DisabilitiesTabtype appDisabilitiesTab) throws SQLException
  { _struct.setAttribute(10, appDisabilitiesTab); }


  public EmpStatusType getEmpStatusObject() throws SQLException
  { return (EmpStatusType) _struct.getAttribute(11); }

  public void setEmpStatusObject(EmpStatusType empStatusObject) throws SQLException
  { _struct.setAttribute(11, empStatusObject); }


  public String getPhoneMobile() throws SQLException
  { return (String) _struct.getAttribute(12); }

  public void setPhoneMobile(String phoneMobile) throws SQLException
  { _struct.setAttribute(12, phoneMobile); }


  public String getPhoneHome() throws SQLException
  { return (String) _struct.getAttribute(13); }

  public void setPhoneHome(String phoneHome) throws SQLException
  { _struct.setAttribute(13, phoneHome); }


  public String getEmail() throws SQLException
  { return (String) _struct.getAttribute(14); }

  public void setEmail(String email) throws SQLException
  { _struct.setAttribute(14, email); }


  public String getNoFixedAbode() throws SQLException
  { return (String) _struct.getAttribute(15); }

  public void setNoFixedAbode(String noFixedAbode) throws SQLException
  { _struct.setAttribute(15, noFixedAbode); }


  public String getUseSuppAddrForPost() throws SQLException
  { return (String) _struct.getAttribute(16); }

  public void setUseSuppAddrForPost(String useSuppAddrForPost) throws SQLException
  { _struct.setAttribute(16, useSuppAddrForPost); }


  public AddressType getHomeAddressObject() throws SQLException
  { return (AddressType) _struct.getAttribute(17); }

  public void setHomeAddressObject(AddressType homeAddressObject) throws SQLException
  { _struct.setAttribute(17, homeAddressObject); }


  public AddressType getPostAddressObject() throws SQLException
  { return (AddressType) _struct.getAttribute(18); }

  public void setPostAddressObject(AddressType postAddressObject) throws SQLException
  { _struct.setAttribute(18, postAddressObject); }


  public java.math.BigDecimal getAphiId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(19); }

  public void setAphiId(java.math.BigDecimal aphiId) throws SQLException
  { _struct.setAttribute(19, aphiId); }


  public ApplPaymentDetailsType getApplPaymentDetailsObject() throws SQLException
  { return (ApplPaymentDetailsType) _struct.getAttribute(20); }

  public void setApplPaymentDetailsObject(ApplPaymentDetailsType applPaymentDetailsObject) throws SQLException
  { _struct.setAttribute(20, applPaymentDetailsObject); }


  public java.sql.Timestamp getSpecialInvestigation() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(21); }

  public void setSpecialInvestigation(java.sql.Timestamp specialInvestigation) throws SQLException
  { _struct.setAttribute(21, specialInvestigation); }


  public java.sql.Timestamp getTimeStamp() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(22); }

  public void setTimeStamp(java.sql.Timestamp timeStamp) throws SQLException
  { _struct.setAttribute(22, timeStamp); }


  public String getPhoneWork() throws SQLException
  { return (String) _struct.getAttribute(23); }

  public void setPhoneWork(String phoneWork) throws SQLException
  { _struct.setAttribute(23, phoneWork); }

}
