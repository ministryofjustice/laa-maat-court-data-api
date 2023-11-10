package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class ThirdPartyType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.THIRD_PARTY_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,2,12,12,12,91,91,12,12 };
  protected static ORADataFactory[] _factory = new ORADataFactory[9];
  protected static final ThirdPartyType _ThirdPartyTypeFactory = new ThirdPartyType();

  public static ORADataFactory getORADataFactory()
  { return _ThirdPartyTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[9], _sqlType, _factory); }
  public ThirdPartyType()
  { _init_struct(true); }
  public ThirdPartyType(java.math.BigDecimal id, java.math.BigDecimal ropdId, String ownerName, String ownerRelation, String otherDescription, java.sql.Timestamp dateCreated, java.sql.Timestamp dateDeleted, String userCreated, String userModified) throws SQLException
  { _init_struct(true);
    setId(id);
    setRopdId(ropdId);
    setOwnerName(ownerName);
    setOwnerRelation(ownerRelation);
    setOtherDescription(otherDescription);
    setDateCreated(dateCreated);
    setDateDeleted(dateDeleted);
    setUserCreated(userCreated);
    setUserModified(userModified);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(ThirdPartyType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new ThirdPartyType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setId(java.math.BigDecimal id) throws SQLException
  { _struct.setAttribute(0, id); }


  public java.math.BigDecimal getRopdId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(1); }

  public void setRopdId(java.math.BigDecimal ropdId) throws SQLException
  { _struct.setAttribute(1, ropdId); }


  public String getOwnerName() throws SQLException
  { return (String) _struct.getAttribute(2); }

  public void setOwnerName(String ownerName) throws SQLException
  { _struct.setAttribute(2, ownerName); }


  public String getOwnerRelation() throws SQLException
  { return (String) _struct.getAttribute(3); }

  public void setOwnerRelation(String ownerRelation) throws SQLException
  { _struct.setAttribute(3, ownerRelation); }


  public String getOtherDescription() throws SQLException
  { return (String) _struct.getAttribute(4); }

  public void setOtherDescription(String otherDescription) throws SQLException
  { _struct.setAttribute(4, otherDescription); }


  public java.sql.Timestamp getDateCreated() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(5); }

  public void setDateCreated(java.sql.Timestamp dateCreated) throws SQLException
  { _struct.setAttribute(5, dateCreated); }


  public java.sql.Timestamp getDateDeleted() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(6); }

  public void setDateDeleted(java.sql.Timestamp dateDeleted) throws SQLException
  { _struct.setAttribute(6, dateDeleted); }


  public String getUserCreated() throws SQLException
  { return (String) _struct.getAttribute(7); }

  public void setUserCreated(String userCreated) throws SQLException
  { _struct.setAttribute(7, userCreated); }


  public String getUserModified() throws SQLException
  { return (String) _struct.getAttribute(8); }

  public void setUserModified(String userModified) throws SQLException
  { _struct.setAttribute(8, userModified); }

}
