package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class Parametertype implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "EMI_DATA.PARAMETERTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,12,12,12,12,12,12,12,12,12,12,12 };
  protected static ORADataFactory[] _factory = new ORADataFactory[12];
  protected static final Parametertype _ParametertypeFactory = new Parametertype();

  public static ORADataFactory getORADataFactory()
  { return _ParametertypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[12], _sqlType, _factory); }
  public Parametertype()
  { _init_struct(true); }
  public Parametertype(java.math.BigDecimal id, String orderBy, String description, String name, String value, String type, String mandatory, String validateInput, String validStatus, String validateMinimum, String validateMaximum, String validPattern) throws SQLException
  { _init_struct(true);
    setId(id);
    setOrderBy(orderBy);
    setDescription(description);
    setName(name);
    setValue(value);
    setType(type);
    setMandatory(mandatory);
    setValidateInput(validateInput);
    setValidStatus(validStatus);
    setValidateMinimum(validateMinimum);
    setValidateMaximum(validateMaximum);
    setValidPattern(validPattern);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(Parametertype o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new Parametertype();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setId(java.math.BigDecimal id) throws SQLException
  { _struct.setAttribute(0, id); }


  public String getOrderBy() throws SQLException
  { return (String) _struct.getAttribute(1); }

  public void setOrderBy(String orderBy) throws SQLException
  { _struct.setAttribute(1, orderBy); }


  public String getDescription() throws SQLException
  { return (String) _struct.getAttribute(2); }

  public void setDescription(String description) throws SQLException
  { _struct.setAttribute(2, description); }


  public String getName() throws SQLException
  { return (String) _struct.getAttribute(3); }

  public void setName(String name) throws SQLException
  { _struct.setAttribute(3, name); }


  public String getValue() throws SQLException
  { return (String) _struct.getAttribute(4); }

  public void setValue(String value) throws SQLException
  { _struct.setAttribute(4, value); }


  public String getType() throws SQLException
  { return (String) _struct.getAttribute(5); }

  public void setType(String type) throws SQLException
  { _struct.setAttribute(5, type); }


  public String getMandatory() throws SQLException
  { return (String) _struct.getAttribute(6); }

  public void setMandatory(String mandatory) throws SQLException
  { _struct.setAttribute(6, mandatory); }


  public String getValidateInput() throws SQLException
  { return (String) _struct.getAttribute(7); }

  public void setValidateInput(String validateInput) throws SQLException
  { _struct.setAttribute(7, validateInput); }


  public String getValidStatus() throws SQLException
  { return (String) _struct.getAttribute(8); }

  public void setValidStatus(String validStatus) throws SQLException
  { _struct.setAttribute(8, validStatus); }


  public String getValidateMinimum() throws SQLException
  { return (String) _struct.getAttribute(9); }

  public void setValidateMinimum(String validateMinimum) throws SQLException
  { _struct.setAttribute(9, validateMinimum); }


  public String getValidateMaximum() throws SQLException
  { return (String) _struct.getAttribute(10); }

  public void setValidateMaximum(String validateMaximum) throws SQLException
  { _struct.setAttribute(10, validateMaximum); }


  public String getValidPattern() throws SQLException
  { return (String) _struct.getAttribute(11); }

  public void setValidPattern(String validPattern) throws SQLException
  { _struct.setAttribute(11, validPattern); }

}
