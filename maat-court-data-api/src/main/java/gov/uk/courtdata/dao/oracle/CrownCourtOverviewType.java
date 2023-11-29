package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class CrownCourtOverviewType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.CROWN_COURT_OVERVIEW_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 12,2002,2002,2003,2002,2003,2002 };
  protected static ORADataFactory[] _factory = new ORADataFactory[7];
  static
  {
    _factory[1] = CrownCourtSummaryType.getORADataFactory();
    _factory[2] = ContributionsType.getORADataFactory();
    _factory[3] = ContributionSummaryTabType.getORADataFactory();
    _factory[4] = ApplPaymentDetailsType.getORADataFactory();
    _factory[5] = CorrespondenceTabType.getORADataFactory();
    _factory[6] = AppealsType.getORADataFactory();
  }
  protected static final CrownCourtOverviewType _CrownCourtOverviewTypeFactory = new CrownCourtOverviewType();

  public static ORADataFactory getORADataFactory()
  { return _CrownCourtOverviewTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[7], _sqlType, _factory); }
  public CrownCourtOverviewType()
  { _init_struct(true); }
  public CrownCourtOverviewType(String available, CrownCourtSummaryType crownCourtSummaryObject, ContributionsType contributionsObject, ContributionSummaryTabType contributionSummaryTab, ApplPaymentDetailsType applPaymentDetailsObject, CorrespondenceTabType correspondenceTab, AppealsType appealObject) throws SQLException
  { _init_struct(true);
    setAvailable(available);
    setCrownCourtSummaryObject(crownCourtSummaryObject);
    setContributionsObject(contributionsObject);
    setContributionSummaryTab(contributionSummaryTab);
    setApplPaymentDetailsObject(applPaymentDetailsObject);
    setCorrespondenceTab(correspondenceTab);
    setAppealObject(appealObject);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(CrownCourtOverviewType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new CrownCourtOverviewType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public String getAvailable() throws SQLException
  { return (String) _struct.getAttribute(0); }

  public void setAvailable(String available) throws SQLException
  { _struct.setAttribute(0, available); }


  public CrownCourtSummaryType getCrownCourtSummaryObject() throws SQLException
  { return (CrownCourtSummaryType) _struct.getAttribute(1); }

  public void setCrownCourtSummaryObject(CrownCourtSummaryType crownCourtSummaryObject) throws SQLException
  { _struct.setAttribute(1, crownCourtSummaryObject); }


  public ContributionsType getContributionsObject() throws SQLException
  { return (ContributionsType) _struct.getAttribute(2); }

  public void setContributionsObject(ContributionsType contributionsObject) throws SQLException
  { _struct.setAttribute(2, contributionsObject); }


  public ContributionSummaryTabType getContributionSummaryTab() throws SQLException
  { return (ContributionSummaryTabType) _struct.getAttribute(3); }

  public void setContributionSummaryTab(ContributionSummaryTabType contributionSummaryTab) throws SQLException
  { _struct.setAttribute(3, contributionSummaryTab); }


  public ApplPaymentDetailsType getApplPaymentDetailsObject() throws SQLException
  { return (ApplPaymentDetailsType) _struct.getAttribute(4); }

  public void setApplPaymentDetailsObject(ApplPaymentDetailsType applPaymentDetailsObject) throws SQLException
  { _struct.setAttribute(4, applPaymentDetailsObject); }


  public CorrespondenceTabType getCorrespondenceTab() throws SQLException
  { return (CorrespondenceTabType) _struct.getAttribute(5); }

  public void setCorrespondenceTab(CorrespondenceTabType correspondenceTab) throws SQLException
  { _struct.setAttribute(5, correspondenceTab); }


  public AppealsType getAppealObject() throws SQLException
  { return (AppealsType) _struct.getAttribute(6); }

  public void setAppealObject(AppealsType appealObject) throws SQLException
  { _struct.setAttribute(6, appealObject); }

}
