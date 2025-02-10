package gov.uk.courtdata.dces.mapper;

import gov.uk.courtdata.entity.FdcContributionsEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.enums.FdcContributionsStatus;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        fdcEntity.setFinalCost(rs.getBigDecimal("FINAL_COST"));
        fdcEntity.setLgfsCost(rs.getBigDecimal("LGFS_COST"));
        fdcEntity.setAgfsCost(rs.getBigDecimal("AGFS_COST"));
        fdcEntity.setStatus(FdcContributionsStatus.valueOf(rs.getString("STATUS")));

        Date dateCalculated = rs.getDate("DATE_CALCULATED");
        if(Objects.nonNull(dateCalculated)){
            fdcEntity.setDateCalculated(dateCalculated.toLocalDate());
        }
        // create basic RepOrderEntity
        RepOrderEntity repOrderEntity = new RepOrderEntity();
        repOrderEntity.setId(rs.getInt("REP_ID"));
        Date sentenceOrderDate = rs.getDate("SENTENCE_ORDER_DATE");
        if(Objects.nonNull(sentenceOrderDate)){
            repOrderEntity.setSentenceOrderDate(sentenceOrderDate.toLocalDate());
        }

        fdcEntity.setRepOrderEntity(repOrderEntity);
        return fdcEntity;
    }

}
