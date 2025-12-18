package net.skillgain.exception.model

enum class ProblemType(
    val titleKey: String,
    val messageKey: String
) {
    INTERNAL_SERVER_ERROR(
        "exception.generic.internal_server_error.title",
        "exception.generic.internal_server_error.detail"
    ),
    VALIDATION_ERROR(
        "exception.request.validation_error.title",
        "exception.request.validation_error.detail"
    ),
    CONSTRAINT_VIOLATION(
        "exception.request.constraint_violation.title",
        "exception.request.constraint_violation.detail"
    ),
    INVALID_REQUEST_BODY(
        "exception.request.invalid_request_body.title",
        "exception.request.invalid_request_body.detail"
    ),
    MISSING_PARAMETER(
        "exception.request.missing_parameter.title",
        "exception.request.missing_parameter.detail"
    ),
    TYPE_MISMATCH(
        "exception.request.type_mismatch.title",
        "exception.request.type_mismatch.detail"
    ),
    METHOD_NOT_ALLOWED(
        "exception.request.method_not_allowed.title",
        "exception.request.method_not_allowed.detail"
    ),
    UNSUPPORTED_MEDIA_TYPE(
        "exception.request.unsupported_media_type.title",
        "exception.request.unsupported_media_type.detail"
    ),

    ACCESS_DENIED(
        "exception.request.access_denied.title",
        "exception.request.access_denied.detail"
    ),
    BAD_REQUEST(
        "exception.request.bad_request.title",
        "exception.request.bad_request.detail"
    )
}
