package gov.uk.courtdata.config;

import com.nimbusds.oauth2.sdk.auth.verifier.Hint;
import gov.uk.courtdata.exception.ValidationException;
import io.sentry.SentryEvent;
import io.sentry.SentryOptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

/**
 * This class provides a Sentry filter for ValidationExceptions
 */
@Component
public class SentryValidationExceptionFilter implements SentryOptions.BeforeSendCallback {

    @Override
    public @Nullable SentryEvent execute(@NotNull SentryEvent event, @Nullable Object hint) {
        return event.getThrowable() instanceof ValidationException ? null : event;
    }
}

