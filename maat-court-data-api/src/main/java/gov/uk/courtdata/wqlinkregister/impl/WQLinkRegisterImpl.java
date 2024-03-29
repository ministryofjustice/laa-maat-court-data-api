package gov.uk.courtdata.wqlinkregister.impl;

import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class WQLinkRegisterImpl {

    private final WqLinkRegisterRepository wqLinkRegisterRepository;


    public List<WqLinkRegisterEntity> findByMaatId(Integer maatID) {
        return  wqLinkRegisterRepository.findBymaatId(maatID);
    }


}
