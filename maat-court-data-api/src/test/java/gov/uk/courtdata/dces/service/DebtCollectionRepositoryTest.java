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
    private static final byte[] fdcMerge1StatementHash = new byte[]{10, -61, -47, -37, -104, -81, 75, -96, -79, 106, -14, -99, 92, -27, 49, 125, -23, 64, -50, -6, 69, -35, -7, -70, -22, 44, -44, 29, 94, 43, 20, 86};
    private static final byte[] fdcMerge2StatementHash = new byte[]{-72, -126, -68, -81, -35, -126, -54, 86, 54, 112, -74, 43, 21, 81, -107, -46, -17, -24, 37, -35, -95, 34, -61, -90, -52, 109, 25, 46, 126, 30, -55, -88};

    @Captor
    ArgumentCaptor<String> mergeSQLCaptor;

    @Test
    void verifyMergeStatement1_IsExpected() {
        when(jdbcTemplate.batchUpdate(mergeSQLCaptor.capture()))
                .thenReturn(new int[]{1});
        debtCollectionRepository.globalUpdatePart1();

        assertNotNull(mergeSQLCaptor);
        Assertions.assertArrayEquals(fdcMerge1StatementHash, getCaptorHash());
    }

    @Test
    void verifyMergeStatement2_IsExpected() {
        when(jdbcTemplate.batchUpdate(mergeSQLCaptor.capture()))
                .thenReturn(new int[]{1});
        debtCollectionRepository.globalUpdatePart2();


        assertNotNull(mergeSQLCaptor);
        Assertions.assertArrayEquals(fdcMerge2StatementHash,getCaptorHash());
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