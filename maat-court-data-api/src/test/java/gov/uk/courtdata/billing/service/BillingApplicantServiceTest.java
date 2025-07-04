package gov.uk.courtdata.billing.service;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import gov.uk.courtdata.billing.repository.BillingApplicantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    void givenNoInput_whenPopulateTableInvoked_thenInsertIsSuccessful() {
        billingApplicantService.findAllApplicantsForBilling();
        verify(billingApplicantRepository, atLeastOnce()).findAllApplicantsForBilling();
    }


}