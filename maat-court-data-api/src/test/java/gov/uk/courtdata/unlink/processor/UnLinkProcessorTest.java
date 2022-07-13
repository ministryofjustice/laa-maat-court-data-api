package gov.uk.courtdata.unlink.processor;

import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.model.Unlink;
import gov.uk.courtdata.model.UnlinkModel;
import gov.uk.courtdata.repository.RepOrderCPDataRepository;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import gov.uk.courtdata.unlink.impl.UnLinkImpl;
import gov.uk.courtdata.unlink.validator.UnLinkValidationProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnLinkProcessorTest {

    @InjectMocks
    private UnLinkProcessor unLinkProcessor;

    @Mock
    private WqLinkRegisterRepository wqLinkRegisterRepository;

    @Mock
    private RepOrderCPDataRepository repOrderCPDataRepository;

    @Mock
    private UnLinkValidationProcessor unlinkValidator;

    @Mock
    private UnLinkImpl unlinkImpl;

    @Test
    public void process() {

        //given unlink...
        Unlink unlink = getUnlink();
        UnlinkModel unlinkModel = UnlinkModel.builder().unlink(unlink).build();

        List<WqLinkRegisterEntity> wqLinkRegisterEntityList =
                Collections.singletonList(WqLinkRegisterEntity.builder().caseUrn("casedfd").build());

        when(wqLinkRegisterRepository.findBymaatId(anyInt()))
                .thenReturn(wqLinkRegisterEntityList);

        Optional<RepOrderCPDataEntity> repOrderCPDataEntity =
                Optional.of(RepOrderCPDataEntity.builder()
                        .caseUrn("caseurn")
                        .build());

        when(repOrderCPDataRepository.findByrepOrderId(anyInt()))
                .thenReturn(repOrderCPDataEntity);

        UnlinkModel unlinkResponse = unLinkProcessor.process(unlink);

        verify(unlinkValidator).validate(any());
        verify(unlinkValidator).validateWQLinkRegister(any(),any());
        verify(unlinkImpl).execute(any());

        assertThat(unlinkResponse.getTxId()).isNull();

        assertThat(unlinkResponse.getUnlink().getMaatId()).isEqualTo(5555666);
        assertThat(unlinkResponse.getUnlink().getUserId()).isEqualTo("1234");
        assertThat(unlinkResponse.getUnlink().getReasonId()).isEqualTo(8877);

        assertThat(unlinkResponse.getRepOrderCPDataEntity().getCaseUrn()).isEqualTo("caseurn");

        assertThat(unlinkResponse.getWqLinkRegisterEntity().getMaatId()).isNull();
        assertThat(unlinkResponse.getWqLinkRegisterEntity().getCaseUrn()).isEqualTo("casedfd");
    }

    private Unlink getUnlink() {

        return Unlink.builder()
                .userId("1234")
                .maatId(5555666)
                .otherReasonText("some reason text")
                .reasonId(8877)
                .build();
    }
}