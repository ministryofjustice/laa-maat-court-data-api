package gov.uk.courtdata.link.validator;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import gov.uk.courtdata.validator.IValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.lang.String.format;

/**
 * <code>LinkExistsValidator</code> validate maatid has no link established.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class LinkExistsValidator implements IValidator<Void, Integer> {

    private final WqLinkRegisterRepository wqLinkRegisterRepository;


    /**
     * Validate
     *
     * @param maatId
     * @return
     * @throws ValidationException
     */
    @Override
    public Optional<Void> validate(Integer maatId) {

        final int linkCount = wqLinkRegisterRepository.getCountByMaatId(maatId);

        if (linkCount > 0)
            throw new ValidationException(format("%s is already linked to a case.", maatId));

        return Optional.empty();
    }
}
