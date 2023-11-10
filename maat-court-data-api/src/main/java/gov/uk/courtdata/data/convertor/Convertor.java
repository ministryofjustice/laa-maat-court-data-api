/**
 * 
 */
package gov.uk.courtdata.data.convertor;

import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;


/**
 * This generic class is extended by all of the DTO classes in this application. It defines 3 
 * abstract methods which must be implemented, but also holds the related oracle java type object.
 * @author SWAN-D
 *
 */
public abstract class Convertor {

	private Object					dbType	= null;
	private Object      			dto		= null;
	protected static final String	NOT_APPLICABLE = "NOT APPLICABLE";
	
	/**
	 * This method take the oracleType class passed as a parameter and
	 * converts the embedded oracle java classes to native java. 
	 */
	public abstract void setDTOFromType(Object oracleType ) throws MAATApplicationException, MAATSystemException;
	
	/**
	 * This method will convert the native java object attributes to populate
	 * the oracle java types of the embedded type class.
	 * @return
	 */
	public abstract void setTypeFromDTO(Object dto) throws MAATApplicationException, MAATSystemException;
	
	/**
	 * This method must be overridden to return the object cast to the appropriate 
	 * class type.
	 * @return
	 */
	public abstract Object getOracleType() throws MAATApplicationException, MAATSystemException;
	
	
	
	public void setType(Object dbType)
	{
		this.dbType = dbType;
	}
	
	public Object getDbType() 
	{
		return this.dbType;
	}

	/**
	 * @return the dto
	 */
	public abstract Object getDTO();

	/**
	 * @param dto the dto to set
	 */
	public abstract void setDTO(Object dto) throws MAATApplicationException;

	/**
	 * @return the dto
	 */
	protected Object getDto()
	{
		return dto;
	}

	/**
	 * @param dto the dto to set
	 */
	protected void setDto(Object dto)
	{
		this.dto = dto;
	}
}
