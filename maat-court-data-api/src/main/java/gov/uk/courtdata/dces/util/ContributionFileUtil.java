package gov.uk.courtdata.dces.util;

import gov.uk.courtdata.dces.request.CreateContributionFileRequest;
import gov.uk.courtdata.dces.request.CreateFdcFileRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@UtilityClass
public class ContributionFileUtil {

    private static final String CONTRIBUTIONS_PREFIX = "CONTRIBUTIONS_";
    private static final String FDC_PREFIX = "FDC_";



    public void assessFilename(CreateFdcFileRequest fdcFileRequest){
        fdcFileRequest.setXmlFileName(getOrDefaultFileName(fdcFileRequest.getXmlFileName(), FDC_PREFIX));
    }
    public void assessFilename(CreateContributionFileRequest contributionFileRequest){
        contributionFileRequest.setXmlFileName(getOrDefaultFileName(contributionFileRequest.getXmlFileName(), CONTRIBUTIONS_PREFIX));
    }

    private String getOrDefaultFileName(String filename, String typePrefix){
        if(!StringUtils.isEmpty(filename)){
            return filename;
        }
        else{
            return generateFilename(typePrefix);
        }
    }

    @NotNull
    private static String generateFilename(String typePrefix) {
        final LocalDateTime date = LocalDateTime.now();
        final String filename = date.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        final StringBuilder stringBuilder = new StringBuilder(typePrefix);
        stringBuilder.append(filename);
        stringBuilder.append(".xml");
        log.info("Contribution file name {}", stringBuilder);
        return stringBuilder.toString();
    }


}
