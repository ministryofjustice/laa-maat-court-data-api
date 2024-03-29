package gov.uk.courtdata.wqlinkregister.service;

import gov.uk.courtdata.dto.WQLinkRegisterDTO;
import gov.uk.courtdata.wqlinkregister.impl.WQLinkRegisterImpl;
import gov.uk.courtdata.wqlinkregister.mapper.WQLinkRegisterMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WQLinkRegisterService {

    private final WQLinkRegisterImpl wqLinkRegisterImpl;
    private final WQLinkRegisterMapper wqLinkRegisterMapper;

    @Transactional(readOnly = true)
    public List<WQLinkRegisterDTO> findByMaatId(Integer maatID) {
        log.info("WQ Link Register - Find by maat id  - Start");
        return wqLinkRegisterMapper.wQLinkRegisterToWQLinkRegisterDTO(wqLinkRegisterImpl.findByMaatId(maatID));
    }
}
