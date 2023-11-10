/**
 * 
 */
package gov.uk.courtdata.data.helper;

import gov.uk.courtdata.data.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.data.oracle.AppSearchDetailsTabType;
import gov.uk.courtdata.data.oracle.AppSearchDetailsType;
import gov.uk.courtdata.dto.application.SearchApplicationCriteriaDTO;
import gov.uk.courtdata.dto.application.SearchCriteriaItemDTO;
import gov.uk.courtdata.enums.SearchOperators;
import gov.uk.courtdata.enums.SearchPropertyDataTypes;
import gov.uk.courtdata.validator.GlobalSearchOperatorException;
import gov.uk.courtdata.validator.MAATApplicationException;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

/**
 * Helper class for the ApplicationDAO
 * <br> 
 * 
 * @author SWAN-D
 *
 */
public class ApplicationDAOHelper 
{
	

	/**
	 * 
	 */
	public ApplicationDAOHelper() 
	{
	}

	/**
	 * This method will iterate the collection of search parameters in the criteria to generate 
	 * a collection of corresponding oracle types to pass to the database interface method.
	 * @param criteria
	 * @return
	 */
	public AppSearchDetailsTabType preProcessCriteria( SearchApplicationCriteriaDTO criteria )
	          throws GlobalSearchOperatorException, MAATApplicationException
	{
		AppSearchDetailsTabType results = null;
		/*
		 * First validate that the global operator is valid, it can only be AND or OR
		 */
		
		if ( criteria.getGlobalOperator().getOperator().equals( SearchOperators.AND.getOperator() ) ||
			 criteria.getGlobalOperator().getOperator().equals( SearchOperators.OR.getOperator()) )
		{
			try 
			{
				String 					global			= criteria.getGlobalOperator().getOperator();
				ConvertorHelper			convertorHelper = new ConvertorHelper();
				AppSearchDetailsType[] 	list			= new AppSearchDetailsType[ criteria.getExpressions().size()];
				int						index			= 0;
				
				Iterator<SearchCriteriaItemDTO> it = criteria.getExpressions().iterator();
				while ( it.hasNext() ) 
				{ 
					SearchCriteriaItemDTO 	dto 	= it.next();
					AppSearchDetailsType	type	=   new AppSearchDetailsType();
					type.setAndOr( 			convertorHelper.toString( global ) );
					type.setOperator(		convertorHelper.toString( dto.getOperator() ) );
					type.setParameter(		convertorHelper.toString( dto.getPropertyName() ));	
					if(dto.getDataType() != null && dto.getDataType().equalsIgnoreCase(SearchPropertyDataTypes.DATE.getOperator()))
					{
						type.setValue(	getOracleDateFormat(dto.getValue()));
					}
					else 
					{
						type.setValue(	convertorHelper.toString( dto.getValue() ));			
					}
					list[index++]			= type;
				}
				results = new AppSearchDetailsTabType( list ); 
			}
			catch ( SQLException sx )
			{
				throw new MAATApplicationException( sx );
			}
		}
		else
		{
			throw new GlobalSearchOperatorException("Invalid Global Logic operator must be either AND or OR");
		}
		
		return results;
	}

	private String getOracleDateFormat(String aDate) throws MAATApplicationException{
	
		String dbFormatDate = null;
		
		if(aDate != null){
			try{
				DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK);
				
				Date formattedDate = df.parse(aDate);
		
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
				
				dbFormatDate = sdf.format(formattedDate);
			}catch(Exception e){
				throw new MAATApplicationException("Error formatting date - " + e.getMessage());
			}
		}
		
		return dbFormatDate;
	}
}
