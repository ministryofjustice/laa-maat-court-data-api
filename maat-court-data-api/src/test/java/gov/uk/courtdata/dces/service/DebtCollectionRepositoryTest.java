package gov.uk.courtdata.dces.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DebtCollectionRepositoryTest {

    @InjectMocks
    private DebtCollectionRepository debtCollectionRepository;

    @Mock
    JdbcTemplate jdbcTemplate;
    private static final byte[] fdcMerge1StatementHash = new byte[]{21, 27, 94, 127, -78, -69, 79, -63, -108, 121, -65, -125, 105, 9, 107, -37, 34, 120, -101, 10, 71, -92, -120, -77, -93, 82, -121, 66, -32, -127, -50, -81};
    private static final byte[] fdcMerge2StatementHash = new byte[]{109, 66, -116, -54, 29, -72, -61, -56, -1, 99, 70, -17, 15, -67, -3, -73, 64, -1, -81, 70, 14, 21, 24, -60, 110, 53, -50, -67, -91, -94, -53, 71};

    @Captor
    ArgumentCaptor<String> mergeSQLCaptor;

    @Test
    void verifyMergeStatement1_IsExpected() {
        when(jdbcTemplate.batchUpdate(mergeSQLCaptor.capture()))
                .thenReturn(new int[]{1});
        debtCollectionRepository.globalUpdatePart1("%s");

        assertNotNull(mergeSQLCaptor);
        Assertions.assertArrayEquals(fdcMerge1StatementHash, getCaptorHash(), "A change has been detected in the debtCollectionRepository.globalUpdatePart1() please verify changes are correct. And update this test.");
    }

    @Test
    void verifyMergeStatement2_IsExpected() {
        when(jdbcTemplate.batchUpdate(mergeSQLCaptor.capture()))
                .thenReturn(new int[]{1});
        debtCollectionRepository.globalUpdatePart2("%s");


        assertNotNull(mergeSQLCaptor);
        Assertions.assertArrayEquals(fdcMerge2StatementHash,getCaptorHash(),"A change has been detected in the debtCollectionRepository.globalUpdatePart1() please verify changes are correct. And update this test.");
    }

    byte[] getCaptorHash(){
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return messageDigest.digest(mergeSQLCaptor.getValue().getBytes(StandardCharsets.UTF_8));
    }
}