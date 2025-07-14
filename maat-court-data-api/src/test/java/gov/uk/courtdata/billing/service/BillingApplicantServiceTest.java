package gov.uk.courtdata.billing.service;

import gov.uk.courtdata.billing.entity.BillingApplicantEntity;
import gov.uk.courtdata.billing.repository.BillingApplicantRepository;
import gov.uk.courtdata.billing.request.UpdateRepOrderBillingRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BillingApplicantServiceTest {

    @Mock
    private BillingApplicantRepository billingApplicantRepository;

    private BillingApplicantService billingApplicantService;

    @BeforeEach
    void setUp() {
        billingApplicantService = new BillingApplicantService(billingApplicantRepository);
    }

    @Test
    void givenNoInput_whenFindAllApplicantsForBillingInvoked_thenGetBillingApplicants() {
        List<BillingApplicantEntity> applicants = new ArrayList<>();
        BillingApplicantEntity applicant = new BillingApplicantEntity();
        applicants.add(applicant);
        when(billingApplicantRepository.findAllApplicantsForBilling()).thenReturn(applicants);

        List<BillingApplicantEntity> applicantEntities = billingApplicantService.findAllApplicantsForBilling();

        Assertions.assertTrue(!applicantEntities.isEmpty());
        verify(billingApplicantRepository, atLeastOnce()).findAllApplicantsForBilling();
    }

    @Test
    void shouldResetCclfFlagSuccessfully() {
        UpdateRepOrderBillingRequest request = new UpdateRepOrderBillingRequest();
        request.setUserModified("test_user");
        request.setRepOrderIds(List.of(1, 2, 3));

        billingApplicantService.resetApplicantBilling(request);

        verify(billingApplicantRepository).resetApplicantBilling(request.getRepOrderIds(), request.getUserModified());
    }

}