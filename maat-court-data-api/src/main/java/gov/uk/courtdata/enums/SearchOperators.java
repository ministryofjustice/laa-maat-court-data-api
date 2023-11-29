package gov.uk.courtdata.enums;
/**
 * Search logical operators. 
 * 
 * @author LING-G
 *
 */
public enum SearchOperators {
    EQUALS		("="),
    LESSTHAN	("<"),
    GREATERTHAN	(">"),  
    NOTEQUALS	("<>"),
    OR			("OR"),
    AND			("AND");

     private String	operator;
    
    SearchOperators(String operator) {
    	this.operator = operator;
    }
    
    public String getOperator() {
        return this.operator;
    }      
    
    public static SearchOperators getOperatorForValue(String value){
    	
    	SearchOperators matchedOperator = null;
    	
        for(SearchOperators enumValue : SearchOperators.values()){
            if(enumValue.getOperator().equals(value)){
            	matchedOperator = enumValue;
                break;
            }
        }
        return matchedOperator;
    }
}
