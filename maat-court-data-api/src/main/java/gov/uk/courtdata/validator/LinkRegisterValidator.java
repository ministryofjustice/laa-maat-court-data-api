package gov.uk.courtdata.validator;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.lang.String.format;

@Component
@XRayEnabled
@RequiredArgsConstructor
public class LinkRegisterValidator implements IValidator<Void, Integer> {

    private final WqLinkRegisterRepository wqLinkRegisterRepository;

    @Override
    public Optional<Void> validate(final Integer maatId) {

        final int linkCount = wqLinkRegisterRepository.getCountByMaatId(maatId);

        if (linkCount == 0) {
            throw new ValidationException(format("MAAT Id : %s not linked.", maatId));
        } else if (linkCount > 1) {
            throw new ValidationException(format("Multiple Links found for  MAAT Id : %s", maatId));
        }
        return Optional.empty();
    }
}