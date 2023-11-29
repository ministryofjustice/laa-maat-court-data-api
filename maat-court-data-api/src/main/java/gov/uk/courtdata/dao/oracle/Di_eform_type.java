package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class Di_eform_type implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.DI_EFORM_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,2005,2 };
  protected static ORADataFactory[] _factory = new ORADataFactory[3];
  protected static final Di_eform_type _Di_eform_typeFactory = new Di_eform_type();

  public static ORADataFactory getORADataFactory()
  { return _Di_eform_typeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[3], _sqlType, _factory); }
  public Di_eform_type()
  { _init_struct(true); }
  public Di_eform_type(java.math.BigDecimal usn, oracle.sql.CLOB xmlDoc, java.math.BigDecimal maatRef) throws SQLException
  { _init_struct(true);
    setUsn(usn);
    setXmlDoc(xmlDoc);
    setMaatRef(maatRef);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(Di_eform_type o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new Di_eform_type();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getUsn() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setUsn(java.math.BigDecimal usn) throws SQLException
  { _struct.setAttribute(0, usn); }


  public oracle.sql.CLOB getXmlDoc() throws SQLException
  { return (oracle.sql.CLOB) _struct.getOracleAttribute(1); }

  public void setXmlDoc(oracle.sql.CLOB xmlDoc) throws SQLException
  { _struct.setOracleAttribute(1, xmlDoc); }


  public java.math.BigDecimal getMaatRef() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(2); }

  public void setMaatRef(java.math.BigDecimal maatRef) throws SQLException
  { _struct.setAttribute(2, maatRef); }

}
