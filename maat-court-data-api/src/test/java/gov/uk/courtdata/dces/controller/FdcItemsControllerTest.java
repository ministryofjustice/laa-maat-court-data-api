package gov.uk.courtdata.dces.controller;

import gov.uk.courtdata.dces.service.FdcContributionsService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class FdcItemsControllerTest {

    @Mock
    private FdcContributionsService fdcContributionsService;

    @InjectMocks
    private FdcItemsController fdcItemsController;

    @BeforeEach
    void setUp() {

    }


}