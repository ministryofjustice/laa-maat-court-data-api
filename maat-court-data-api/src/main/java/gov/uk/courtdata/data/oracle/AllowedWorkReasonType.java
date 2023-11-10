package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class AllowedWorkReasonType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.ALLOWED_WORK_REASON_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2003,2003,2003,2003,2003,2003 };
  protected static ORADataFactory[] _factory = new ORADataFactory[6];
  static
  {
    _factory[0] = NewWorkReasonTabtype.getORADataFactory();
    _factory[1] = NewWorkReasonTabtype.getORADataFactory();
    _factory[2] = NewWorkReasonTabtype.getORADataFactory();
    _factory[3] = NewWorkReasonTabtype.getORADataFactory();
    _factory[4] = NewWorkReasonTabtype.getORADataFactory();
    _factory[5] = NewWorkReasonTabtype.getORADataFactory();
  }
  protected static final AllowedWorkReasonType _AllowedWorkReasonTypeFactory = new AllowedWorkReasonType();

  public static ORADataFactory getORADataFactory()
  { return _AllowedWorkReasonTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[6], _sqlType, _factory); }
  public AllowedWorkReasonType()
  { _init_struct(true); }
  public AllowedWorkReasonType(NewWorkReasonTabtype passport, NewWorkReasonTabtype means, NewWorkReasonTabtype mchardship, NewWorkReasonTabtype cchardship, NewWorkReasonTabtype eligibility, NewWorkReasonTabtype ioj) throws SQLException
  { _init_struct(true);
    setPassport(passport);
    setMeans(means);
    setMchardship(mchardship);
    setCchardship(cchardship);
    setEligibility(eligibility);
    setIoj(ioj);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(AllowedWorkReasonType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new AllowedWorkReasonType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public NewWorkReasonTabtype getPassport() throws SQLException
  { return (NewWorkReasonTabtype) _struct.getAttribute(0); }

  public void setPassport(NewWorkReasonTabtype passport) throws SQLException
  { _struct.setAttribute(0, passport); }


  public NewWorkReasonTabtype getMeans() throws SQLException
  { return (NewWorkReasonTabtype) _struct.getAttribute(1); }

  public void setMeans(NewWorkReasonTabtype means) throws SQLException
  { _struct.setAttribute(1, means); }


  public NewWorkReasonTabtype getMchardship() throws SQLException
  { return (NewWorkReasonTabtype) _struct.getAttribute(2); }

  public void setMchardship(NewWorkReasonTabtype mchardship) throws SQLException
  { _struct.setAttribute(2, mchardship); }


  public NewWorkReasonTabtype getCchardship() throws SQLException
  { return (NewWorkReasonTabtype) _struct.getAttribute(3); }

  public void setCchardship(NewWorkReasonTabtype cchardship) throws SQLException
  { _struct.setAttribute(3, cchardship); }


  public NewWorkReasonTabtype getEligibility() throws SQLException
  { return (NewWorkReasonTabtype) _struct.getAttribute(4); }

  public void setEligibility(NewWorkReasonTabtype eligibility) throws SQLException
  { _struct.setAttribute(4, eligibility); }


  public NewWorkReasonTabtype getIoj() throws SQLException
  { return (NewWorkReasonTabtype) _struct.getAttribute(5); }

  public void setIoj(NewWorkReasonTabtype ioj) throws SQLException
  { _struct.setAttribute(5, ioj); }

}
