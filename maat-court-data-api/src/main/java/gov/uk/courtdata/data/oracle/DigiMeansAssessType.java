package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class DigiMeansAssessType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.DIGI_MEANS_ASSESS_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,2,91,91 };
  protected static ORADataFactory[] _factory = new ORADataFactory[4];
  protected static final DigiMeansAssessType _DigiMeansAssessTypeFactory = new DigiMeansAssessType();

  public static ORADataFactory getORADataFactory()
  { return _DigiMeansAssessTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[4], _sqlType, _factory); }
  public DigiMeansAssessType()
  { _init_struct(true); }
  public DigiMeansAssessType(java.math.BigDecimal id, java.math.BigDecimal maatId, java.sql.Timestamp dateCreated, java.sql.Timestamp originalEmailDate) throws SQLException
  { _init_struct(true);
    setId(id);
    setMaatId(maatId);
    setDateCreated(dateCreated);
    setOriginalEmailDate(originalEmailDate);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(DigiMeansAssessType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new DigiMeansAssessType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setId(java.math.BigDecimal id) throws SQLException
  { _struct.setAttribute(0, id); }


  public java.math.BigDecimal getMaatId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(1); }

  public void setMaatId(java.math.BigDecimal maatId) throws SQLException
  { _struct.setAttribute(1, maatId); }


  public java.sql.Timestamp getDateCreated() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(2); }

  public void setDateCreated(java.sql.Timestamp dateCreated) throws SQLException
  { _struct.setAttribute(2, dateCreated); }


  public java.sql.Timestamp getOriginalEmailDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(3); }

  public void setOriginalEmailDate(java.sql.Timestamp originalEmailDate) throws SQLException
  { _struct.setAttribute(3, originalEmailDate); }

}
