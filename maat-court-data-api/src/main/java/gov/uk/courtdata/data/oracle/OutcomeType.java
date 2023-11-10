package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class OutcomeType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.OUTCOME_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 12,12,91 };
  protected static ORADataFactory[] _factory = new ORADataFactory[3];
  protected static final OutcomeType _OutcomeTypeFactory = new OutcomeType();

  public static ORADataFactory getORADataFactory()
  { return _OutcomeTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[3], _sqlType, _factory); }
  public OutcomeType()
  { _init_struct(true); }
  public OutcomeType(String outcome, String description, java.sql.Timestamp dateSet) throws SQLException
  { _init_struct(true);
    setOutcome(outcome);
    setDescription(description);
    setDateSet(dateSet);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(OutcomeType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new OutcomeType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public String getOutcome() throws SQLException
  { return (String) _struct.getAttribute(0); }

  public void setOutcome(String outcome) throws SQLException
  { _struct.setAttribute(0, outcome); }


  public String getDescription() throws SQLException
  { return (String) _struct.getAttribute(1); }

  public void setDescription(String description) throws SQLException
  { _struct.setAttribute(1, description); }


  public java.sql.Timestamp getDateSet() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(2); }

  public void setDateSet(java.sql.Timestamp dateSet) throws SQLException
  { _struct.setAttribute(2, dateSet); }

}
