package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class AssessmentType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.ASSESSMENTTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2002,2002 };
  protected static ORADataFactory[] _factory = new ORADataFactory[2];
  static
  {
    _factory[0] = FinAssessmentType.getORADataFactory();
    _factory[1] = IOJType.getORADataFactory();
  }
  protected static final AssessmentType _AssessmentTypeFactory = new AssessmentType();

  public static ORADataFactory getORADataFactory()
  { return _AssessmentTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[2], _sqlType, _factory); }
  public AssessmentType()
  { _init_struct(true); }
  public AssessmentType(FinAssessmentType finAssessmentObject, IOJType iojObject) throws SQLException
  { _init_struct(true);
    setFinAssessmentObject(finAssessmentObject);
    setIojObject(iojObject);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(AssessmentType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new AssessmentType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public FinAssessmentType getFinAssessmentObject() throws SQLException
  { return (FinAssessmentType) _struct.getAttribute(0); }

  public void setFinAssessmentObject(FinAssessmentType finAssessmentObject) throws SQLException
  { _struct.setAttribute(0, finAssessmentObject); }


  public IOJType getIojObject() throws SQLException
  { return (IOJType) _struct.getAttribute(1); }

  public void setIojObject(IOJType iojObject) throws SQLException
  { _struct.setAttribute(1, iojObject); }

}
