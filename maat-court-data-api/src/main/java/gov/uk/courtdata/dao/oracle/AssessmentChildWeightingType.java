package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class AssessmentChildWeightingType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.ASSESSMENT_CHILD_WEIGHTINGTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,2,2,2,2,2,93 };
  protected static ORADataFactory[] _factory = new ORADataFactory[7];
  protected static final AssessmentChildWeightingType _AssessmentChildWeightingTypeFactory = new AssessmentChildWeightingType();

  public static ORADataFactory getORADataFactory()
  { return _AssessmentChildWeightingTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[7], _sqlType, _factory); }
  public AssessmentChildWeightingType()
  { _init_struct(true); }
  public AssessmentChildWeightingType(java.math.BigDecimal id, java.math.BigDecimal childWeightingId, java.math.BigDecimal lowerAgeRange, java.math.BigDecimal upperAgeRange, java.math.BigDecimal weightingFactor, java.math.BigDecimal noOfChildren, java.sql.Timestamp timeStamp) throws SQLException
  { _init_struct(true);
    setId(id);
    setChildWeightingId(childWeightingId);
    setLowerAgeRange(lowerAgeRange);
    setUpperAgeRange(upperAgeRange);
    setWeightingFactor(weightingFactor);
    setNoOfChildren(noOfChildren);
    setTimeStamp(timeStamp);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(AssessmentChildWeightingType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new AssessmentChildWeightingType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setId(java.math.BigDecimal id) throws SQLException
  { _struct.setAttribute(0, id); }


  public java.math.BigDecimal getChildWeightingId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(1); }

  public void setChildWeightingId(java.math.BigDecimal childWeightingId) throws SQLException
  { _struct.setAttribute(1, childWeightingId); }


  public java.math.BigDecimal getLowerAgeRange() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(2); }

  public void setLowerAgeRange(java.math.BigDecimal lowerAgeRange) throws SQLException
  { _struct.setAttribute(2, lowerAgeRange); }


  public java.math.BigDecimal getUpperAgeRange() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(3); }

  public void setUpperAgeRange(java.math.BigDecimal upperAgeRange) throws SQLException
  { _struct.setAttribute(3, upperAgeRange); }


  public java.math.BigDecimal getWeightingFactor() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(4); }

  public void setWeightingFactor(java.math.BigDecimal weightingFactor) throws SQLException
  { _struct.setAttribute(4, weightingFactor); }


  public java.math.BigDecimal getNoOfChildren() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(5); }

  public void setNoOfChildren(java.math.BigDecimal noOfChildren) throws SQLException
  { _struct.setAttribute(5, noOfChildren); }


  public java.sql.Timestamp getTimeStamp() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(6); }

  public void setTimeStamp(java.sql.Timestamp timeStamp) throws SQLException
  { _struct.setAttribute(6, timeStamp); }

}
