package gov.uk.courtdata.dto.application;

import gov.uk.courtdata.enums.SearchOperators;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Collection;


@Data
@SuperBuilder
@NoArgsConstructor
public class SearchApplicationCriteriaDTO {
	 private static final Log log = LogFactory.getLog(SearchApplicationCriteriaDTO.class);

	private SearchOperators globalOperator;
	private Collection<SearchCriteriaItemDTO> expressions = new ArrayList<SearchCriteriaItemDTO>();

}
