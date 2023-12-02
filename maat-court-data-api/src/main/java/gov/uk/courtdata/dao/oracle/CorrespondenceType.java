package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class CorrespondenceType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.CORRESPONDENCE_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,2002,91,91,2,12,2003,91 };
  protected static ORADataFactory[] _factory = new ORADataFactory[8];
  static
  {
    _factory[1] = CorrespondenceTypeType.getORADataFactory();
    _factory[6] = PrintDatesTabtype.getORADataFactory();
  }
  protected static final CorrespondenceType _CorrespondenceTypeFactory = new CorrespondenceType();

  public static ORADataFactory getORADataFactory()
  { return _CorrespondenceTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[8], _sqlType, _factory); }
  public CorrespondenceType()
  { _init_struct(true); }
  public CorrespondenceType(java.math.BigDecimal id, CorrespondenceTypeType correspondenceTypeObject, java.sql.Timestamp generatedDate, java.sql.Timestamp printedDate, java.math.BigDecimal fiasId, String templateName, PrintDatesTabtype printDatesTab, java.sql.Timestamp originalEmailDate) throws SQLException
  { _init_struct(true);
    setId(id);
    setCorrespondenceTypeObject(correspondenceTypeObject);
    setGeneratedDate(generatedDate);
    setPrintedDate(printedDate);
    setFiasId(fiasId);
    setTemplateName(templateName);
    setPrintDatesTab(printDatesTab);
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
  protected ORAData create(CorrespondenceType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new CorrespondenceType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setId(java.math.BigDecimal id) throws SQLException
  { _struct.setAttribute(0, id); }


  public CorrespondenceTypeType getCorrespondenceTypeObject() throws SQLException
  { return (CorrespondenceTypeType) _struct.getAttribute(1); }

  public void setCorrespondenceTypeObject(CorrespondenceTypeType correspondenceTypeObject) throws SQLException
  { _struct.setAttribute(1, correspondenceTypeObject); }


  public java.sql.Timestamp getGeneratedDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(2); }

  public void setGeneratedDate(java.sql.Timestamp generatedDate) throws SQLException
  { _struct.setAttribute(2, generatedDate); }


  public java.sql.Timestamp getPrintedDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(3); }

  public void setPrintedDate(java.sql.Timestamp printedDate) throws SQLException
  { _struct.setAttribute(3, printedDate); }


  public java.math.BigDecimal getFiasId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(4); }

  public void setFiasId(java.math.BigDecimal fiasId) throws SQLException
  { _struct.setAttribute(4, fiasId); }


  public String getTemplateName() throws SQLException
  { return (String) _struct.getAttribute(5); }

  public void setTemplateName(String templateName) throws SQLException
  { _struct.setAttribute(5, templateName); }


  public PrintDatesTabtype getPrintDatesTab() throws SQLException
  { return (PrintDatesTabtype) _struct.getAttribute(6); }

  public void setPrintDatesTab(PrintDatesTabtype printDatesTab) throws SQLException
  { _struct.setAttribute(6, printDatesTab); }


  public java.sql.Timestamp getOriginalEmailDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(7); }

  public void setOriginalEmailDate(java.sql.Timestamp originalEmailDate) throws SQLException
  { _struct.setAttribute(7, originalEmailDate); }

}
