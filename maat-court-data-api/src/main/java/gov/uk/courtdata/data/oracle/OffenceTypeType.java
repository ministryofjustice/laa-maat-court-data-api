package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class OffenceTypeType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.OFFENCE_TYPE_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 12,12,2 };
  protected static ORADataFactory[] _factory = new ORADataFactory[3];
  protected static final OffenceTypeType _OffenceTypeTypeFactory = new OffenceTypeType();

  public static ORADataFactory getORADataFactory()
  { return _OffenceTypeTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[3], _sqlType, _factory); }
  public OffenceTypeType()
  { _init_struct(true); }
  public OffenceTypeType(String offenceType, String description, java.math.BigDecimal contribsCap) throws SQLException
  { _init_struct(true);
    setOffenceType(offenceType);
    setDescription(description);
    setContribsCap(contribsCap);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(OffenceTypeType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new OffenceTypeType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public String getOffenceType() throws SQLException
  { return (String) _struct.getAttribute(0); }

  public void setOffenceType(String offenceType) throws SQLException
  { _struct.setAttribute(0, offenceType); }


  public String getDescription() throws SQLException
  { return (String) _struct.getAttribute(1); }

  public void setDescription(String description) throws SQLException
  { _struct.setAttribute(1, description); }


  public java.math.BigDecimal getContribsCap() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(2); }

  public void setContribsCap(java.math.BigDecimal contribsCap) throws SQLException
  { _struct.setAttribute(2, contribsCap); }

}
