package gov.uk.courtdata.dces.mapper;

import gov.uk.courtdata.entity.FdcContributionsEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.enums.FdcContributionsStatus;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;

/***
 * Mapper to map the basic needed fields for FDC processing.
 * Note that this will only map the SentenceOrderDate, and Id fields on the RepOrderEntity.
 */
public class FdcMapper implements RowMapper<FdcContributionsEntity> {
    @Override
    public @NotNull FdcContributionsEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        FdcContributionsEntity fdcEntity = new FdcContributionsEntity();
        fdcEntity.setId(rs.getInt("ID"));
        fdcEntity.setContFileId(rs.getInt("CONT_FILE_ID"));
        fdcEntity.setFinalCost(rs.getBigDecimal("FINAL_COST"));
        fdcEntity.setLgfsCost(rs.getBigDecimal("LGFS_COST"));
        fdcEntity.setAgfsCost(rs.getBigDecimal("AGFS_COST"));
        fdcEntity.setStatus(FdcContributionsStatus.valueOf(rs.getString("STATUS")));
        fdcEntity.setDateCalculated(getDateField(rs, "DATE_CALCULATED"));
        fdcEntity.setUserModified(rs.getString("USER_MODIFIED"));
        fdcEntity.setDateModified(getDateField(rs, "DATE_MODIFIED"));
        // create basic RepOrderEntity
        RepOrderEntity repOrderEntity = new RepOrderEntity();
        repOrderEntity.setId(rs.getInt("REP_ID"));
        repOrderEntity.setSentenceOrderDate( getDateField(rs, "SENTENCE_ORDER_DATE"));

        fdcEntity.setRepOrderEntity(repOrderEntity);
        return fdcEntity;
    }

    private LocalDate getDateField(ResultSet rs, String fieldName) throws SQLException {
        Date date = rs.getDate(fieldName);
        return Objects.nonNull(date)? date.toLocalDate() : null;
    }

}
