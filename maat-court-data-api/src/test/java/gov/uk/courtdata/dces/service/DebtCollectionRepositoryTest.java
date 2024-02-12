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
    private static final byte[] fdcMerge1StatementHash = new byte[]{63, 93, 48, -38, -1, 2, -116, 125, -6, 100, 103, 97, 30, 43, -54, 7, -86, -126, 50, -126, -93, 40, -127, -112, 107, 44, -40, 82, -81, -93, -94, -35};
    private static final byte[] fdcMerge2StatementHash = new byte[]{-110, -117, -90, -20, 15, 82, 0, -66, 98, 121, 11, 1, -105, -98, -1, 23, 126, 83, 21, 120, 70, 24, 74, -44, -69, 107, 69, 125, 50, 39, 107, -71};

    @Captor
    ArgumentCaptor<String> mergeSQLCaptor;

    @Test
    void verifyMergeStatement1_IsExpected() {
        when(jdbcTemplate.batchUpdate(mergeSQLCaptor.capture()))
                .thenReturn(new int[]{1});
        debtCollectionRepository.globalUpdatePart1();

        assertNotNull(mergeSQLCaptor);
        Assertions.assertArrayEquals(fdcMerge1StatementHash, getCaptorHash(), "A change has been detected in the debtCollectionRepository.globalUpdatePart1() please verify changes are correct. And update this test.");
    }

    @Test
    void verifyMergeStatement2_IsExpected() {
        when(jdbcTemplate.batchUpdate(mergeSQLCaptor.capture()))
                .thenReturn(new int[]{1});
        debtCollectionRepository.globalUpdatePart2();


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