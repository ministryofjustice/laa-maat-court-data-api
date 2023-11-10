package gov.uk.courtdata.dto.application;

import gov.uk.courtdata.enums.SearchOperators;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


/**
 * This value object holds the users entered search criteria. 
 * Its used by the main MAAT Search page, to retrieve MAAT Applications (aka RepOrder/Case).
 * 
 * 
 * @author LING-G
 *
 */
public class SearchApplicationCriteriaDTO {
	 private static final Log log = LogFactory.getLog(SearchApplicationCriteriaDTO.class);

	private SearchOperators globalOperator;
	private Collection<SearchCriteriaItemDTO> expressions = new ArrayList<SearchCriteriaItemDTO>();
	
	
	
		


	public String toString(){
		StringBuffer sb =  new StringBuffer();
		if (isEmpty()){
			sb.append("None");
		} else {
			
			Iterator iter = getExpressions().iterator();
			while (iter.hasNext()) {
				
				// the expression  [xxx EQ 'abc"]
				sb.append( iter.next().toString());
				
				
				// and / or  another expression
				if (iter.hasNext()){
					if(this.globalOperator != null){
						sb.append(" ").append(this.globalOperator.getOperator()).append(" ");
					}
				}
			}
			
		}
		
		return sb.toString();
	}

	

	
	public SearchOperators getGlobalOperator() {
		return globalOperator;
	}
	public void setGlobalOperator(SearchOperators globalOperator) {
		this.globalOperator = globalOperator;
	}




	public Collection<SearchCriteriaItemDTO> getExpressions() {
		return expressions;
	}




	public void setExpressions(Collection<SearchCriteriaItemDTO> expressions) {
		this.expressions = expressions;
	}





	public void addCriteria(SearchCriteriaItemDTO searchCriteriaItemDTO) {
		expressions.add(searchCriteriaItemDTO);
		
	}







public boolean hasCriteria() {
	
	return expressions != null && !expressions.isEmpty();
}




public static SearchApplicationCriteriaDTO example() {
	SearchApplicationCriteriaDTO dto = new SearchApplicationCriteriaDTO();
	dto.setGlobalOperator( SearchOperators.EQUALS);
	
	Collection<SearchCriteriaItemDTO> expressions = new ArrayList<SearchCriteriaItemDTO>();
	
	SearchCriteriaItemDTO item = new SearchCriteriaItemDTO();
	item.setOperator("=");
	item.setPropertyName("FirstName");
	item.setValue("Gavin");
	dto.getExpressions().add(item);
	return dto;
}




public boolean isEmpty() {
	return this.expressions.isEmpty();
}
}
