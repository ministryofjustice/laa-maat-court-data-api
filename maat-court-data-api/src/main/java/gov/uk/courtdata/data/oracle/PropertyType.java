package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class PropertyType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.PROPERTY_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,2002,2002,2002,2,2,12,2,2,2,2,2,2,2002,12,93,2003 };
  protected static ORADataFactory[] _factory = new ORADataFactory[17];
  static
  {
    _factory[1] = AddressType.getORADataFactory();
    _factory[2] = ResidentialStatusType.getORADataFactory();
    _factory[3] = PropertyTypeType.getORADataFactory();
    _factory[13] = ResidentialStatusType.getORADataFactory();
    _factory[16] = ThirdPartyTabtype.getORADataFactory();
  }
  protected static final PropertyType _PropertyTypeFactory = new PropertyType();

  public static ORADataFactory getORADataFactory()
  { return _PropertyTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[17], _sqlType, _factory); }
  public PropertyType()
  { _init_struct(true); }
  public PropertyType(java.math.BigDecimal id, AddressType addressObject, ResidentialStatusType residentialStatusObject, PropertyTypeType propertyTypeObject, java.math.BigDecimal percentageOwnedApplicant, java.math.BigDecimal percentageOwnedPartner, String bedrooms, java.math.BigDecimal verifiedEquityAmount, java.math.BigDecimal declaredMarketValue, java.math.BigDecimal declaredMortgageCharges, java.math.BigDecimal declaredEquityAmount, java.math.BigDecimal verifiedMarketValue, java.math.BigDecimal verifiedMortgageCharges, ResidentialStatusType verifiedResidentialStatus, String tennantInPlace, java.sql.Timestamp timeStamp, ThirdPartyTabtype thirdPartyOwners) throws SQLException
  { _init_struct(true);
    setId(id);
    setAddressObject(addressObject);
    setResidentialStatusObject(residentialStatusObject);
    setPropertyTypeObject(propertyTypeObject);
    setPercentageOwnedApplicant(percentageOwnedApplicant);
    setPercentageOwnedPartner(percentageOwnedPartner);
    setBedrooms(bedrooms);
    setVerifiedEquityAmount(verifiedEquityAmount);
    setDeclaredMarketValue(declaredMarketValue);
    setDeclaredMortgageCharges(declaredMortgageCharges);
    setDeclaredEquityAmount(declaredEquityAmount);
    setVerifiedMarketValue(verifiedMarketValue);
    setVerifiedMortgageCharges(verifiedMortgageCharges);
    setVerifiedResidentialStatus(verifiedResidentialStatus);
    setTennantInPlace(tennantInPlace);
    setTimeStamp(timeStamp);
    setThirdPartyOwners(thirdPartyOwners);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(PropertyType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new PropertyType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setId(java.math.BigDecimal id) throws SQLException
  { _struct.setAttribute(0, id); }


  public AddressType getAddressObject() throws SQLException
  { return (AddressType) _struct.getAttribute(1); }

  public void setAddressObject(AddressType addressObject) throws SQLException
  { _struct.setAttribute(1, addressObject); }


  public ResidentialStatusType getResidentialStatusObject() throws SQLException
  { return (ResidentialStatusType) _struct.getAttribute(2); }

  public void setResidentialStatusObject(ResidentialStatusType residentialStatusObject) throws SQLException
  { _struct.setAttribute(2, residentialStatusObject); }


  public PropertyTypeType getPropertyTypeObject() throws SQLException
  { return (PropertyTypeType) _struct.getAttribute(3); }

  public void setPropertyTypeObject(PropertyTypeType propertyTypeObject) throws SQLException
  { _struct.setAttribute(3, propertyTypeObject); }


  public java.math.BigDecimal getPercentageOwnedApplicant() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(4); }

  public void setPercentageOwnedApplicant(java.math.BigDecimal percentageOwnedApplicant) throws SQLException
  { _struct.setAttribute(4, percentageOwnedApplicant); }


  public java.math.BigDecimal getPercentageOwnedPartner() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(5); }

  public void setPercentageOwnedPartner(java.math.BigDecimal percentageOwnedPartner) throws SQLException
  { _struct.setAttribute(5, percentageOwnedPartner); }


  public String getBedrooms() throws SQLException
  { return (String) _struct.getAttribute(6); }

  public void setBedrooms(String bedrooms) throws SQLException
  { _struct.setAttribute(6, bedrooms); }


  public java.math.BigDecimal getVerifiedEquityAmount() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(7); }

  public void setVerifiedEquityAmount(java.math.BigDecimal verifiedEquityAmount) throws SQLException
  { _struct.setAttribute(7, verifiedEquityAmount); }


  public java.math.BigDecimal getDeclaredMarketValue() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(8); }

  public void setDeclaredMarketValue(java.math.BigDecimal declaredMarketValue) throws SQLException
  { _struct.setAttribute(8, declaredMarketValue); }


  public java.math.BigDecimal getDeclaredMortgageCharges() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(9); }

  public void setDeclaredMortgageCharges(java.math.BigDecimal declaredMortgageCharges) throws SQLException
  { _struct.setAttribute(9, declaredMortgageCharges); }


  public java.math.BigDecimal getDeclaredEquityAmount() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(10); }

  public void setDeclaredEquityAmount(java.math.BigDecimal declaredEquityAmount) throws SQLException
  { _struct.setAttribute(10, declaredEquityAmount); }


  public java.math.BigDecimal getVerifiedMarketValue() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(11); }

  public void setVerifiedMarketValue(java.math.BigDecimal verifiedMarketValue) throws SQLException
  { _struct.setAttribute(11, verifiedMarketValue); }


  public java.math.BigDecimal getVerifiedMortgageCharges() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(12); }

  public void setVerifiedMortgageCharges(java.math.BigDecimal verifiedMortgageCharges) throws SQLException
  { _struct.setAttribute(12, verifiedMortgageCharges); }


  public ResidentialStatusType getVerifiedResidentialStatus() throws SQLException
  { return (ResidentialStatusType) _struct.getAttribute(13); }

  public void setVerifiedResidentialStatus(ResidentialStatusType verifiedResidentialStatus) throws SQLException
  { _struct.setAttribute(13, verifiedResidentialStatus); }


  public String getTennantInPlace() throws SQLException
  { return (String) _struct.getAttribute(14); }

  public void setTennantInPlace(String tennantInPlace) throws SQLException
  { _struct.setAttribute(14, tennantInPlace); }


  public java.sql.Timestamp getTimeStamp() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(15); }

  public void setTimeStamp(java.sql.Timestamp timeStamp) throws SQLException
  { _struct.setAttribute(15, timeStamp); }


  public ThirdPartyTabtype getThirdPartyOwners() throws SQLException
  { return (ThirdPartyTabtype) _struct.getAttribute(16); }

  public void setThirdPartyOwners(ThirdPartyTabtype thirdPartyOwners) throws SQLException
  { _struct.setAttribute(16, thirdPartyOwners); }

}
