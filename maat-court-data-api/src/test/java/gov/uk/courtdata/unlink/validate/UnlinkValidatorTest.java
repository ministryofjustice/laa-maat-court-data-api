package gov.uk.courtdata.unlink.validate;

import com.google.gson.Gson;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.error.MaatCourtDataException;
import gov.uk.courtdata.model.Unlink;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MAATCourtDataApplication.class)
public class UnlinkValidatorTest {
    @Autowired
    private UnlinkValidator unlinkValidator;
    @Autowired
    private Gson gson;
    @Autowired
    private TestModelDataBuilder testModelDataBuilder;

    private Unlink unlink;
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void buildUnlinkModel() {
        unlink = gson.fromJson(testModelDataBuilder.getUnLinkString(), Unlink.class);
    }

    @Test
    public void givenUnlinkModelIsEmpty_whenValidateRequestIsInvoked_thenValidationFailed() {
        exceptionRule.expect(MaatCourtDataException.class);
        exceptionRule.expectMessage("Unlink Request is empty");
        unlinkValidator.validateRequest(null);
    }

    @Test
    public void givenMAAtIdIsNull_whenValidateRequestIsInvoked_thenValidationFailed() {
        exceptionRule.expect(MaatCourtDataException.class);
        exceptionRule.expectMessage("There is No Valid MAAT ID found");
        unlink.setMaatId(null);
        unlinkValidator.validateRequest(unlink);
    }

    @Test
    public void givenUserIdlIsNull_whenValidateRequestIsInvoked_thenValidationFailed() {
        exceptionRule.expect(MaatCourtDataException.class);
        exceptionRule.expectMessage("There is No Valid User ID found");
        unlink.setUserId(null);
        unlinkValidator.validateRequest(unlink);
    }

    @Test
    public void givenReasonIdIsNull_whenValidateRequestIsInvoked_thenValidationFailed() {
        exceptionRule.expect(MaatCourtDataException.class);
        exceptionRule.expectMessage("There is No Valid Reason ID found");
        unlink.setReasonId(null);
        unlinkValidator.validateRequest(unlink);
    }

    @Test
    public void givenWQLinkRegisterIsNull_whenValidateWQLinkRegisterIsInvoked_thenValidationFailed() {
        exceptionRule.expect(MaatCourtDataException.class);
        exceptionRule.expectMessage("There is No link established for MAAT ID : 123");
        unlinkValidator.validateWQLinkRegister(null,123);
    }
    @Test
    public void givenWQLinkRegisterHasMoreThanOneEntry_whenValidateWQLinkRegisterIsInvoked_thenValidationFailed() {
        exceptionRule.expect(MaatCourtDataException.class);
        exceptionRule.expectMessage("There are multiple links found for  MAAT ID : 123");
        WqLinkRegisterEntity wqLinkRegisterEntity1 = WqLinkRegisterEntity.builder().build();
        WqLinkRegisterEntity wqLinkRegisterEntity2 = WqLinkRegisterEntity.builder().build();
        List<WqLinkRegisterEntity> linkRegisterEntities = new ArrayList<>();
        linkRegisterEntities.add(wqLinkRegisterEntity1);
        linkRegisterEntities.add(wqLinkRegisterEntity2);
        unlinkValidator.validateWQLinkRegister(linkRegisterEntities,123);
    }
}
