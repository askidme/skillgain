package net.skillgain.exception.model

enum class ProblemType(
    val type: String,
    val title: String,
    val messageKey: String
) {
    INTERNAL_SERVER_ERROR(
        "internal-server-error",
        "Internal Server Error",
        "exception.generic.internal_server_error"
    ),
    VALIDATION_ERROR(
        "validation-error",
        "Validation Error",
        "exception.request.validation_error"
    ),
    CONSTRAINT_VIOLATION(
        "constraint-violation",
        "Constraint Violation",
        "exception.request.constraint_violation"
    ),
    INVALID_REQUEST_BODY(
        "invalid-request-body",
        "Invalid Request Body",
        "exception.request.invalid_request_body"
    ),
    MISSING_PARAMETER(
        "missing-parameter",
        "Missing Parameter",
        "exception.request.missing_parameter"
    ),
    TYPE_MISMATCH(
        "type-mismatch",
        "Type Mismatch",
        "exception.request.type_mismatch"
    ),
    METHOD_NOT_ALLOWED(
        "method-not-allowed",
        "Method Not Allowed",
        messageKey = "exception.request.method_not_allowed"
    ),
    UNSUPPORTED_MEDIA_TYPE(
        "unsupported-media-type",
        "Unsupported Media Type",
        "exception.request.unsupported_media_type"
    ),

    ACCESS_DENIED(
        "access-denied",
        "Access Denied",
        "exception.request.access_denied"
    ),
    BAD_REQUEST_GEN(
        "bad-request",
        "Bad Request",
        "exception.request.bad_request"
    )
}
