package gov.uk.courtdata.dto.application;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class SearchCriteriaItemDTO {

	
	private String propertyName;
	private String operator;
	private String value;
	private String dataType;

}
