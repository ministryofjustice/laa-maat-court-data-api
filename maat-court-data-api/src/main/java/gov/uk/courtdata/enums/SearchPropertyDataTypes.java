package gov.uk.courtdata.enums;

public enum SearchPropertyDataTypes {
    NUMBER		("NUM"),
    BOOLEAN		("BOOL"),
    STRING		("STR"),  
    DATE		("DATE");

     private String	dataType;
    
    SearchPropertyDataTypes(String operator) {
    	this.dataType = operator;
    }
    
    public String getOperator() {
        return this.dataType;
    }      
    
    public static SearchPropertyDataTypes getDataTypeForValue(String value){
    	
    	SearchPropertyDataTypes matchedDataType = null;
    	
        for(SearchPropertyDataTypes enumValue : SearchPropertyDataTypes.values()){
            if(enumValue.getOperator().equals(value)){
            	matchedDataType = enumValue;
                break;
            }
        }
        return matchedDataType;
    }
}
