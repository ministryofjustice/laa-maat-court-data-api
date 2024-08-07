package gov.uk.courtdata.offence.helper;

import gov.uk.courtdata.entity.OffenceEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.offence.model.OffenceSummary;
import gov.uk.courtdata.offence.model.Plea;
import gov.uk.courtdata.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OffenceHelperTest {

    @InjectMocks
    private OffenceHelper offenceHelper;

    @Mock
    private OffenceRepository offenceRepository;
    @Mock
    private WQResultRepository wqResultRepository;
    @Mock
    private ResultRepository resultRepository;
    @Mock
    private XLATResultRepository xlatResultRepository;
    @Mock
    private WQOffenceRepository wqOffenceRepository;

    @Mock
    private WqLinkRegisterRepository wqLinkRegisterRepository;

    @Test
    public void testWhenOffenceResultIsCommittal_thenReturnTrue() {

        when(offenceRepository.findByCaseId(anyInt())).thenReturn(getOffenceEntity());
        when(wqLinkRegisterRepository.findBymaatId(anyInt())).thenReturn(List.of(WqLinkRegisterEntity.builder().maatId(123).caseId(456).build()));
        when(xlatResultRepository.findResultsByWQType(anyInt(), anyInt())).thenReturn(List.of(4057, 4558, 4559, 4560, 4561, 4562, 4564, 4567, 4593, 1290));

        when(resultRepository.findResultCodeByCaseIdAndAsnSeq(anyInt(), anyString())).thenReturn(List.of(4057, 4558));

        when(wqResultRepository.findResultCodeByCaseIdAndAsnSeq(anyInt(), anyString())).thenReturn(List.of(4057, 4558));

        List<OffenceSummary> offenceSummaryList = offenceHelper.getTrialOffences(getOffenceSummary(), 12121);

        verify(offenceRepository).findByCaseId(anyInt());
        verify(xlatResultRepository,atLeast(2)).findResultsByWQType(anyInt(), anyInt());
        assertEquals(1, offenceSummaryList.size());
        assertEquals(UUID.fromString("e2540d98-995f-43f2-97e4-f712b8a5d6a6"), offenceSummaryList.get(0).getOffenceId());

    }

    @Test
    public void testWhenOffenceResultIsNotCommittal_thenReturnFalse() {

        when(offenceRepository.findByCaseId(anyInt())).thenReturn(getOffenceEntity());
        when(wqLinkRegisterRepository.findBymaatId(anyInt())).thenReturn(List.of(WqLinkRegisterEntity.builder().maatId(123).caseId(456).build()));
        when(xlatResultRepository.findResultsByWQType(anyInt(), anyInt())).thenReturn(List.of(4057, 4558, 4559, 4560, 4561, 4562, 4564, 4567, 4593, 1290));

        when(resultRepository.findResultCodeByCaseIdAndAsnSeq(anyInt(), anyString())).thenReturn(List.of(453, 454));

        when(wqResultRepository.findResultCodeByCaseIdAndAsnSeq(anyInt(), anyString())).thenReturn(List.of(6665, 6666));


        List<OffenceSummary> offenceSummaryList = offenceHelper.getTrialOffences(getOffenceSummary(), 12121);

        verify(offenceRepository).findByCaseId(anyInt());
        verify(xlatResultRepository,atLeast(2)).findResultsByWQType(anyInt(), anyInt());
        assertEquals(0, offenceSummaryList.size());

    }

    private List<OffenceEntity> getOffenceEntity() {
        return
                List.of(
                        OffenceEntity.builder()
                                .offenceId("e2540d98-995f-43f2-97e4-f712b8a5d6a6")
                                .asnSeq("001")
                                .caseId(12121)
                                .applicationFlag(1)
                                .build(),
                        OffenceEntity.builder()
                                .offenceId("908ad01e-5a38-4158-957a-0c1d1a783862")
                                .asnSeq("002")
                                .caseId(12121)
                                .build());
    }

    private List<OffenceSummary> getOffenceSummary() {

        return List.of(
                OffenceSummary.builder()
                        .offenceCode("1212")
                        .offenceId(UUID.fromString("e2540d98-995f-43f2-97e4-f712b8a5d6a6"))
                        .plea(Plea.builder().value("NOT_GUILTY").pleaDate("2021-11-12").build())
                        .build()
        );
    }
}