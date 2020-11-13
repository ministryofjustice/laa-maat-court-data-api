package gov.uk.courtdata.unlink.processor;

import com.google.gson.Gson;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.model.Unlink;
import gov.uk.courtdata.model.UnlinkModel;
import gov.uk.courtdata.repository.RepOrderCPDataRepository;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import gov.uk.courtdata.unlink.impl.UnLinkImpl;
import gov.uk.courtdata.unlink.validator.UnLinkValidationProcessor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UnLinkProcessorTest {

    @InjectMocks
    private UnLinkProcessor unLinkProcessor;

    @Mock
    private WqLinkRegisterRepository wqLinkRegisterRepository;

    @Mock
    private RepOrderCPDataRepository repOrderCPDataRepository;

    @Mock
    private UnLinkValidationProcessor unLinkValidationProcessor;

    @Mock
    private UnLinkImpl unlinkImpl;

    @Mock
    private TestModelDataBuilder testModelDataBuilder;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
      //  testModelDataBuilder = new TestModelDataBuilder(new TestEntityDataBuilder(), new Gson());
    }

    @Test
    public void process() {

        //given unlink...
        Unlink unlink = getUnlink();
        UnlinkModel unlinkModel = UnlinkModel.builder().unlink(unlink).build();

        List<WqLinkRegisterEntity> wqLinkRegisterEntityList =
                Arrays.asList(WqLinkRegisterEntity.builder().caseUrn("casedfd").build());

        //when(wqLinkRegisterRepository.findBymaatId(anyInt())).thenReturn(wqLinkRegisterEntityList);

        Optional<RepOrderCPDataEntity> repOrderCPDataEntity =
                Optional.of(RepOrderCPDataEntity.builder()
                        .caseUrn("caseurn")
                        .build());

       // when(repOrderCPDataRepository.findByrepOrderId(111)).thenReturn(repOrderCPDataEntity);

        //when
        //UnlinkModel response = unLinkProcessor.process(unlink);

        //then
        //verify(unLinkValidationProcessor).validate(any());
        //verify(repOrderCPDataRepository).findByrepOrderId(any());
//        verify(offenceCodeRefDataProcessor, times(1)).processOffenceCode("Code");
        //assertThat(caseInfoCaptor.getValue().getTxId()).isEqualTo(courtDataDTO.getTxId());

    }



    private UnlinkModel getUnlinkModel () {

        WqLinkRegisterEntity wqLinkRegisterEntity =
                WqLinkRegisterEntity.builder()
                        .caseId(5566)
                        .caseUrn("case565")
                        .build();

        RepOrderCPDataEntity repOrderCPDataEntity =
                RepOrderCPDataEntity.builder()
                        .caseUrn("case565")
                        .build();

        UnlinkModel unlinkModel = UnlinkModel.builder()
                .unlink(getUnlink())
                .wqLinkRegisterEntity(wqLinkRegisterEntity)
                .repOrderCPDataEntity(repOrderCPDataEntity)
                .build();

        return unlinkModel;

    }
    private Unlink getUnlink () {

        return Unlink.builder()
                .userId("1234")
                .maatId(5555666)
                .otherReasonText("some reason text")
                .reasonId(8877)
                .build();


    }
}