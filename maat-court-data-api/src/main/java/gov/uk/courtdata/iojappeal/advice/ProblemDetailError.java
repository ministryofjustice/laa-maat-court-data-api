package gov.uk.courtdata.iojappeal.advice;

public enum ProblemDetailError {

    DB_ERROR("DB_ERROR", "Request violates a data constraint"),
    VALIDATION_FAILURE("VALIDATION_FAILURE", "Validation failure"),
    OBJECT_NOT_FOUND("OBJECT_NOT_FOUND", "Resource not found"),
    APPLICATION_ERROR("APPLICATION_ERROR", "Unexpected error"),
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

