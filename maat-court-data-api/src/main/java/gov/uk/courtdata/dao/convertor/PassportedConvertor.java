package gov.uk.courtdata.dao.convertor;


import java.sql.SQLException;


import gov.uk.courtdata.dao.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.dao.oracle.PassportAssessmentType;
import gov.uk.courtdata.dto.application.JobSeekerDTO;
import gov.uk.courtdata.dto.application.PartnerDTO;
import gov.uk.courtdata.dto.application.PassportedDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;


/**
 * @author TOWO-N
 */
public class PassportedConvertor extends Convertor {

    /**
     * Returns the instance of the DTO cast to the appropriate class
     *
     * @see Convertor#getDTO()
     */
    @Override
    public PassportedDTO getDTO() {
        return (PassportedDTO) this.getDto();
    }

    /**
     * sets the local instance of the dto
     *
     * @see Convertor#setDTO(java.lang.Object)
     */
    @Override
    public void setDTO(Object dto) throws MAATApplicationException {
        if (dto instanceof PassportedDTO) {
            this.setDto(dto);
        } else {
            throw new MAATApplicationException(" Invalid DTO type for conversion got " + dto.getClass()
                    .getName() + " wanted " + PassportedDTO.class.getName());
        }
    }

    @Override
    public PassportAssessmentType getOracleType() throws MAATApplicationException, MAATSystemException {

        if (getDbType() == null) {
            setType(new PassportAssessmentType());
        }

        if (getDbType() instanceof PassportAssessmentType passportAssessmentType) {
            return passportAssessmentType;
        } else {
            return null;  // temp fix, could cause null pointer exception
        }
    }

    /**
     * This method take the oracleType class passed as a parameter and
     * converts the embedded oracle java classes to native java.
     */
    public void setDTOFromType(Object oracleType) throws MAATApplicationException, MAATSystemException {

        this.setType(oracleType);
        this.setDto(new PassportedDTO());    // create the new DTO

        try {
            ConvertorHelper convertorHelper = new ConvertorHelper();
            getDTO().setTimestamp(convertorHelper.toLocalDateTime(getOracleType().getTimeStamp()));

            getDTO().setPassportedId(convertorHelper.toLong(getOracleType().getId()));
            getDTO().setUsn(convertorHelper.toLong(getOracleType().getUsn()));
            getDTO().setCmuId(convertorHelper.toLong(getOracleType().getCmuId()));
            getDTO().setBenefitClaimedByPartner(convertorHelper.toBoolean(getOracleType().getPartnerBenefitClaimed()));
            getDTO().setBenefitGaurenteedStatePension(
                    convertorHelper.toBoolean(getOracleType().getStatePensionCredit()));
            getDTO().setBenefitIncomeSupport(convertorHelper.toBoolean(getOracleType().getIncomeSupport()));
            getDTO().setBenefitUniversalCredit(convertorHelper.toBoolean(getOracleType().getUniversalCredit()));
            getDTO().setBenefitEmploymentSupport(convertorHelper.toBoolean(getOracleType().getEsa()));
            getDTO().setDate(convertorHelper.toDate(getOracleType().getAssDate()));
            getDTO().setNotes(convertorHelper.toString(getOracleType().getPassportNote()));
            getDTO().setResult(convertorHelper.toString(getOracleType().getResult()));
            getDTO().setDwpResult(convertorHelper.toString(getOracleType().getDwpResult()));
            getDTO().setUnder18HeardYouthCourt(convertorHelper.toBoolean(getOracleType().getUnder18HeardYouthCourt()));
            getDTO().setUnder18HeardMagsCourt(convertorHelper.toBoolean(getOracleType().getUnder18HeardMagsCourt()));
            getDTO().setUnder18FullEducation(convertorHelper.toBoolean(getOracleType().getUnder18FullEducation()));
            getDTO().setUnder16(convertorHelper.toBoolean(getOracleType().getUnder16()));
            getDTO().setBetween1617(convertorHelper.toBoolean(getOracleType().getBetween1617()));

            JobSeekerDTO jobSeeker = new JobSeekerDTO();
            jobSeeker.setIsJobSeeker(convertorHelper.toBoolean(getOracleType().getJobSeekers()));
            jobSeeker.setLastSignedOn(convertorHelper.toDate(getOracleType().getLastSignOnDate()));
            getDTO().setBenefitJobSeeker(jobSeeker);

            PartnerDTO partner = new PartnerDTO();
            partner.setDateOfBirth(convertorHelper.toDate(getOracleType().getPartnerDob()));
            partner.setFirstName(convertorHelper.toString(getOracleType().getPartnerFirstName()));
            partner.setNationaInsuranceNumber(convertorHelper.toString(getOracleType().getPartnerNiNo()));
            partner.setSurname(convertorHelper.toString(getOracleType().getPartnerSurname()));
            getDTO().setPartnerDetails(partner);

            IncomeEvidenceSummaryConvertor ieConvertor = new IncomeEvidenceSummaryConvertor();
            if (getOracleType().getIncomeEvidenceSummaryObject() != null) {
                ieConvertor.setDTOFromType(getOracleType().getIncomeEvidenceSummaryObject());
            }
            getDTO().setPassportSummaryEvidenceDTO(ieConvertor.getDTO());


            if (getOracleType().getStatusObject() != null) {
                AssessmentStatusConvertor convertor = new AssessmentStatusConvertor();
                convertor.setDTOFromType(getOracleType().getStatusObject());
                getDTO().setAssessementStatusDTO(convertor.getDTO());
            }

            if (getOracleType().getConfirmationObject() != null) {
                PassportConfirmationConvertor convertor = new PassportConfirmationConvertor();
                convertor.setDTOFromType(getOracleType().getConfirmationObject());
                getDTO().setPassportConfirmationDTO(convertor.getDTO());
            }

            if (getOracleType().getNewWorkReasonObject() != null) {
                NewWorkReasonConvertor convertor = new NewWorkReasonConvertor();
                convertor.setDTOFromType(getOracleType().getNewWorkReasonObject());
                getDTO().setNewWorkReason(convertor.getDTO());
            }

            if (getOracleType().getReviewTypeObject() != null) {
                ReviewTypeConvertor convertor = new ReviewTypeConvertor();
                convertor.setDTOFromType(getOracleType().getReviewTypeObject());
                getDTO().setReviewType(convertor.getDTO());
            }

            getDTO().setWhoDwpChecked(convertorHelper.toString(getOracleType().getWhoDwpChecked()));

        } catch (NullPointerException nex) {
            /*
             * This will happen if the dto object has not been set
             */
            throw new MAATApplicationException("PassportedConverter - the embedded dto is null");

        } catch (SQLException ex) {
            throw new MAATSystemException(ex);
        }
    }

    /**
     * This method will convert the native java object attributes to populate
     * the oracle java types of the embedded type class.
     *
     * @return
     */
    public void setTypeFromDTO(Object dto) throws MAATApplicationException, MAATSystemException {
        /*
         * update the oracle type object converting all of the dto attributes to oracleType attributes
         */
        try {
            setType(null);    // force new type to be instantiated
            setDTO(dto);
            ConvertorHelper convertorHelper = new ConvertorHelper();
            getOracleType().setTimeStamp(convertorHelper.toTimestamp(getDTO().getTimestamp()));

            getOracleType().setId(convertorHelper.toLong(getDTO().getPassportedId()));
            getOracleType().setUsn(convertorHelper.toLong(getDTO().getUsn()));
            getOracleType().setCmuId(convertorHelper.toLong(getDTO().getCmuId()));
            getOracleType().setPartnerBenefitClaimed(convertorHelper.toBoolean(getDTO().getBenefitClaimedByPartner()));
            getOracleType().setStatePensionCredit(
                    convertorHelper.toBoolean(getDTO().getBenefitGaurenteedStatePension()));
            getOracleType().setIncomeSupport(convertorHelper.toBoolean(getDTO().getBenefitIncomeSupport()));
            getOracleType().setEsa(convertorHelper.toBoolean(getDTO().getBenefitEmploymentSupport()));
            getOracleType().setUniversalCredit(convertorHelper.toBoolean(getDTO().getBenefitUniversalCredit()));
            getOracleType().setAssDate(convertorHelper.toDate(getDTO().getDate()));
            getOracleType().setPassportNote(convertorHelper.toString(getDTO().getNotes()));
            getOracleType().setResult(convertorHelper.toString(getDTO().getResult()));
            getOracleType().setDwpResult(convertorHelper.toString(getDTO().getDwpResult()));
            getOracleType().setUnder18HeardYouthCourt(convertorHelper.toBoolean(getDTO().getUnder18HeardYouthCourt()));
            getOracleType().setUnder18HeardMagsCourt(convertorHelper.toBoolean(getDTO().getUnder18HeardMagsCourt()));
            getOracleType().setUnder18FullEducation(convertorHelper.toBoolean(getDTO().getUnder18FullEducation()));
            getOracleType().setUnder16(convertorHelper.toBoolean(getDTO().getUnder16()));
            getOracleType().setBetween1617(convertorHelper.toBoolean(getDTO().getBetween1617()));
            getOracleType().setJobSeekers(convertorHelper.toBoolean(getDTO().getBenefitJobSeeker().getIsJobSeeker()));
            getOracleType().setLastSignOnDate(convertorHelper.toDate(getDTO().getBenefitJobSeeker().getLastSignedOn()));
            getOracleType().setPartnerDob(convertorHelper.toDate(getDTO().getPartnerDetails().getDateOfBirth()));
            getOracleType().setPartnerFirstName(convertorHelper.toString(getDTO().getPartnerDetails().getFirstName()));
            getOracleType().setPartnerSurname(convertorHelper.toString(getDTO().getPartnerDetails().getSurname()));
            getOracleType().setPartnerNiNo(
                    convertorHelper.toString(getDTO().getPartnerDetails().getNationaInsuranceNumber()));

            IncomeEvidenceSummaryConvertor ieConvertor = new IncomeEvidenceSummaryConvertor();
            if (getDTO().getPassportSummaryEvidenceDTO() != null) {
                ieConvertor.setTypeFromDTO(getDTO().getPassportSummaryEvidenceDTO());
            }
            getOracleType().setIncomeEvidenceSummaryObject(ieConvertor.getOracleType());


            AssessmentStatusConvertor assConvertor = new AssessmentStatusConvertor();
            if (getDTO().getAssessementStatusDTO() != null) {
                assConvertor.setTypeFromDTO(getDTO().getAssessementStatusDTO());
            }
            getOracleType().setStatusObject(assConvertor.getOracleType());

            PassportConfirmationConvertor pcConvertor = new PassportConfirmationConvertor();
            if (getDTO().getPassportConfirmationDTO() != null) {
                pcConvertor.setTypeFromDTO(getDTO().getPassportConfirmationDTO());
            }
            getOracleType().setConfirmationObject(pcConvertor.getOracleType());

            NewWorkReasonConvertor nwrConvertor = new NewWorkReasonConvertor();
            if (getDTO().getNewWorkReason() != null) {
                nwrConvertor.setTypeFromDTO(getDTO().getNewWorkReason());
            }
            getOracleType().setNewWorkReasonObject(nwrConvertor.getOracleType());

            ReviewTypeConvertor reviewTypeConvertor = new ReviewTypeConvertor();
            if (getDTO().getReviewType() != null) {
                reviewTypeConvertor.setTypeFromDTO(getDTO().getReviewType());
            }
            getOracleType().setReviewTypeObject(reviewTypeConvertor.getOracleType());

            getOracleType().setWhoDwpChecked(convertorHelper.toString(getDTO().getWhoDwpChecked()));
        } catch (NullPointerException nex) {
            /*
             * This will happen if the dto object has not been set
             */
            throw new MAATApplicationException("PassportedConvertor - the embedded dto is null");
        } catch (SQLException ex) {
            throw new MAATSystemException(ex);
        }
    }


}
