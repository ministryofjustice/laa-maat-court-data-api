package gov.uk.courtdata.unlink.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.entity.UnlinkEntity;
import gov.uk.courtdata.entity.WqCoreEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.model.Unlink;
import gov.uk.courtdata.model.UnlinkModel;
import gov.uk.courtdata.repository.IdentifierRepository;
import gov.uk.courtdata.repository.RepOrderCPDataRepository;
import gov.uk.courtdata.repository.UnlinkReasonRepository;
import gov.uk.courtdata.repository.WqCoreRepository;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UnLinkImplTest {

    @InjectMocks
    private UnLinkImpl unLink;

    @Spy
    private WqLinkRegisterRepository wqLinkRegisterRepository;

    @Captor
    private ArgumentCaptor<WqLinkRegisterEntity> wqLinkRegisterEntityArgumentCaptor;

    @Spy
    private WqCoreRepository wqCoreRepository;

    @Captor
    private ArgumentCaptor<WqCoreEntity> wqCoreEntityArgumentCaptor;

    @Spy
    private UnlinkReasonRepository unlinkReasonRepository;

    @Captor
    private ArgumentCaptor<UnlinkEntity> unlinkEntityArgumentCaptor;

    @Mock
    private IdentifierRepository identifierRepository;

    @Mock
    private RepOrderCPDataRepository repOrderCPDataRepository;

    @Test
    void givenCaseDetail_whenExecuteIsInvoked_thenCaseInUnlinked() {
        UnlinkModel unlinkModel = getUnlinkModel();
        unLink.execute(unlinkModel);

        verify(wqLinkRegisterRepository).save(any());
        verify(identifierRepository).getTxnID();
        verify(unlinkReasonRepository).save(any());
        verify(repOrderCPDataRepository).save(any());
    }

    @Test
    void givenCaseDetail_whenExecuteIsInvoked_thenSaveWqCoreEntity() {
        UnlinkModel unlinkModel = getUnlinkModel();

        unLink.execute(unlinkModel);

        verify(wqCoreRepository).save(wqCoreEntityArgumentCaptor.capture());
        assertThat(wqCoreEntityArgumentCaptor.getValue().getCaseId()).isEqualTo(5566);
        assertThat(wqCoreEntityArgumentCaptor.getValue().getWqType()).isEqualTo(24);
        assertThat(wqCoreEntityArgumentCaptor.getValue().getCreatedUserId()).isEqualTo("1234");
        assertThat(wqCoreEntityArgumentCaptor.getValue().getMaatUpdateStatus()).isNull();
    }

    @Test
    void givenCaseDetail_whenExecuteIsInvoked_thenSaveWqLinkRegisterEntity() {

        UnlinkModel unlinkModel = getUnlinkModel();

        unLink.execute(unlinkModel);

        verify(wqLinkRegisterRepository).save(wqLinkRegisterEntityArgumentCaptor.capture());
        assertThat(wqLinkRegisterEntityArgumentCaptor.getValue().getCaseId()).isEqualTo(5566);
        assertThat(wqLinkRegisterEntityArgumentCaptor.getValue().getCaseUrn()).isEqualTo("case565");
        assertThat(wqLinkRegisterEntityArgumentCaptor.getValue().getRemovedUserId())
                .isEqualTo("1234");
        assertThat(wqLinkRegisterEntityArgumentCaptor.getValue().getLibraId()).isEqualTo("libraid1");
        assertThat(wqLinkRegisterEntityArgumentCaptor.getValue().getMaatId()).isEqualTo(43534543);
        assertThat(wqLinkRegisterEntityArgumentCaptor.getValue().getCjsAreaCode())
                .isEqualTo("LFD3");
    }

    @Test
    void givenCaseDetail_whenExecuteIsInvoked_thenSaveUnlinkEntity() {

        UnlinkModel unlinkModel = getUnlinkModel();

        unLink.execute(unlinkModel);

        verify(unlinkReasonRepository).save(unlinkEntityArgumentCaptor.capture());
        assertThat(unlinkEntityArgumentCaptor.getValue().getTxId()).isZero();
        assertThat(unlinkEntityArgumentCaptor.getValue().getCaseId()).isEqualTo(5566);
        assertThat(unlinkEntityArgumentCaptor.getValue().getReasonId()).isEqualTo(8877);
        assertThat(unlinkEntityArgumentCaptor.getValue().getOtherReason()).isEqualTo("some reason text");
    }

    private UnlinkModel getUnlinkModel() {
        Unlink unlink = Unlink.builder()
                .userId("1234")
                .maatId(5555666)
                .otherReasonText("some reason text")
                .reasonId(8877)
                .build();

        WqLinkRegisterEntity wqLinkRegisterEntity = WqLinkRegisterEntity.builder()
                .caseId(5566)
                .caseUrn("case565")
                .libraId("libraid1")
                .maatId(43534543)
                .cjsAreaCode("LFD3")
                .removedDate(LocalDateTime.now())
                .build();

        RepOrderCPDataEntity repOrderCPDataEntity =
                RepOrderCPDataEntity.builder().caseUrn("case565").build();

        return UnlinkModel.builder()
                .unlink(unlink)
                .wqLinkRegisterEntity(wqLinkRegisterEntity)
                .repOrderCPDataEntity(repOrderCPDataEntity)
                .txId(1111)
                .build();
    }
}
