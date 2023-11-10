package gov.uk.courtdata.data.convertor;


import gov.uk.courtdata.data.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.data.oracle.Di_eform_type;
import gov.uk.courtdata.dto.application.EformDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;

public class EformConvertor extends Convertor {

		
		/* (non-Javadoc)
		 * @see uk.gov.lsc.maat.bus.data.dao.convertor.Convertor#getDTO()
		 */
		public EformDTO getDTO(){
			
			return (EformDTO)getDto();
		}

		/**
		 * Returns the instance of the OracleType class cast appropriately
		 * @see Convertor#getOracleType()
		 */
		@Override
		public Di_eform_type getOracleType() throws MAATApplicationException,MAATSystemException
		{
			if ( getDbType() == null )
			{
				setType( new Di_eform_type() );
			}
			
			if ( getDbType() instanceof Di_eform_type )
			{
				return (Di_eform_type)getDbType();
			}
			else
			{
				/*
				 * fatal error ???? write a handler in the GenericDTO
				 */
				//throw new DAOApplicationException( Constants.INVALID_DTO_TYPE_CLASS );
				return null;  // temp fix, could cause null pointer exception
			}
		}

		

		/**
		 * sets the local instance of the dto
		 * @see Convertor#setDTO(Object)
		 */
		@Override
		public void setDTO(Object dto) throws MAATApplicationException
		{
			if ( dto instanceof EformDTO  )
				this.setDto(dto);
			else
				throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + EformDTO.class.getName());
		}




		/**
		 * Updates the local instance of the DTO by converting the data in the
		 * oracle type object passed as a parameter
		 * @see Convertor#setDTOFromType(Object)
		 */
		@Override
		public void setDTOFromType(Object oracleType)
				throws MAATApplicationException, MAATSystemException{
			// save it
			this.setType( oracleType );
			this.setDto( new EformDTO() );	// create the new DTO
		
			try{
				
				ConvertorHelper convertorHelper = new ConvertorHelper();
				getDTO().setUsn(convertorHelper.toLong(	getOracleType().getUsn() ));
				getDTO().setMaatRef(convertorHelper.toLong(getOracleType().getMaatRef()));
				
				getDTO().setXmlDoc(convertorHelper.clobToString(getOracleType().getXmlDoc() ));
				
			}catch (NullPointerException nex){
				/*
				 * This will happen if the dto object has not been set
				 */
				
				throw new MAATApplicationException( "EformConvertor - the embedded dto is null");
			}
			catch (SQLException ex ){
				
				throw new MAATSystemException( ex );
			}		
		}

		/**
		 * Updates the local instance of the Oracle type by converting the data in the
		 * dto object passed as a parameter
		 * @see Convertor#setTypeFromDTO(Object)
		 */
		@Override
		public void setTypeFromDTO(Object dto) throws MAATApplicationException,	MAATSystemException{
			/*
			 * update the oracle type object converting all of the dto attributes to oracleType attributes
			 */
			try	{
				setType( null );	// force new type to be instantiated 
				setDTO( dto );	// if the dto class type is not right for this conversion an exception is thrown
				ConvertorHelper convertorHelper = new ConvertorHelper();
				getOracleType().setUsn(convertorHelper.toLong( 	getDTO().getUsn() ));
			
			}catch (NullPointerException nex){
				/*
				 * This will happen if the dto object has not been set
				 */
				
				throw new MAATApplicationException( "EformConvertor - the embedded dto is null");
			}catch (SQLException ex ){
				
				throw new MAATSystemException( ex );
			}
		}


		
}
