package gov.uk.courtdata.advice;

public enum ProblemDetailError {

    DB_ERROR("DB_ERROR", "Request violates a data constraint"),
    VALIDATION_FAILURE("VALIDATION_FAILURE", "Validation failure"),
    OBJECT_NOT_FOUND("OBJECT_NOT_FOUND", "Resource not found"),
    APPLICATION_ERROR("APPLICATION_ERROR", "Unexpected error"),
    METHOD_NOT_ALLOWED("METHOD_NOT_ALLOWED", "HTTP method not allowed"),
    UNSUPPORTED_MEDIA_TYPE("UNSUPPORTED_MEDIA_TYPE", "Unsupported media type"),
    BAD_REQUEST("BAD_REQUEST", "Invalid request");

    private final String code;
    private final String defaultDetail;

    ProblemDetailError(String code, String defaultDetail) {
        this.code = code;
        this.defaultDetail = defaultDetail;
    }

    public String code() {
        return code;
    }

    public String defaultDetail() {
        return defaultDetail;
    }
}

