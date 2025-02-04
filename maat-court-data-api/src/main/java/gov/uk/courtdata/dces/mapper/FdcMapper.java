package gov.uk.courtdata.dces.mapper;

import gov.uk.courtdata.entity.FdcContributionsEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class FdcMapper implements RowMapper<FdcContributionsEntity> {
    @Override
    public @NotNull FdcContributionsEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        FdcContributionsEntity fdcEntity = new FdcContributionsEntity();
        fdcEntity.setId(rs.getInt("ID"));
        fdcEntity.setMaatId(rs.getInt("REP_ID"));
        fdcEntity.setFinalCost(rs.getBigDecimal("FINAL_COST"));
        fdcEntity.setLgfsCost(rs.getBigDecimal("LGFS_COST"));
        fdcEntity.setAgfsCost(rs.getBigDecimal("AGFS_COST"));

        Date dateCalculated = rs.getDate("DATE_CALCULATED");
        if(Objects.nonNull(dateCalculated)){
            fdcEntity.setDateCalculated(dateCalculated.toLocalDate());
        }
        Date sentenceOrderDate = rs.getDate("SENTENCE_ORDER_DATE");
        if(Objects.nonNull(sentenceOrderDate)){
            fdcEntity.setSentenceOrderDate(sentenceOrderDate.toLocalDate());
        }
        return fdcEntity;
    }

}
