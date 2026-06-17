package br.com.tauan.agendamento.shared.presentation.docs;

/**
 * Exemplos de payloads de erro usados na documentação OpenAPI (@ExampleObject).
 * Os valores refletem o que o {@code GlobalExceptionHandler} efetivamente retorna.
 */
public final class ApiExamples {

    private ApiExamples() {
    }

    public static final String VALIDATION_ERROR = """
            {
              "success": false,
              "error": {
                "message": "Validation failed",
                "status": 400,
                "code": "VALIDATION_ERROR",
                "fieldErrors": [
                  { "field": "email", "message": "must not be blank" }
                ]
              }
            }""";

    public static final String UNAUTHENTICATED = """
            {
              "success": false,
              "error": {
                "message": "User is not authenticated",
                "status": 401,
                "code": "UNAUTHENTICATED_USER",
                "fieldErrors": []
              }
            }""";

    public static final String INVALID_CREDENTIALS = """
            {
              "success": false,
              "error": {
                "message": "Invalid credentials",
                "status": 401,
                "code": "UNAUTHORIZED",
                "fieldErrors": []
              }
            }""";

    public static final String FORBIDDEN = """
            {
              "success": false,
              "error": {
                "message": "You do not have permission to perform this action",
                "status": 403,
                "code": "FORBIDDEN",
                "fieldErrors": []
              }
            }""";

    public static final String USER_NOT_FOUND = """
            {
              "success": false,
              "error": {
                "message": "User not found",
                "status": 404,
                "code": "USER_NOT_FOUND",
                "fieldErrors": []
              }
            }""";

    public static final String EMAIL_ALREADY_EXISTS = """
            {
              "success": false,
              "error": {
                "message": "Email already exists",
                "status": 409,
                "code": "EMAIL_ALREADY_EXISTS",
                "fieldErrors": []
              }
            }""";

    public static final String CALENDAR_NOT_FOUND = """
            {
              "success": false,
              "error": {
                "message": "Calendar not found",
                "status": 404,
                "code": "CALENDAR_NOT_FOUND",
                "fieldErrors": []
              }
            }""";

    public static final String CALENDAR_ALREADY_EXISTS = """
            {
              "success": false,
              "error": {
                "message": "Calendar already exists",
                "status": 409,
                "code": "CALENDAR_ALREADY_EXISTS",
                "fieldErrors": []
              }
            }""";

    public static final String TIME_SLOT_NOT_FOUND = """
            {
              "success": false,
              "error": {
                "message": "Time slot not found",
                "status": 404,
                "code": "TIME_SLOT_NOT_FOUND",
                "fieldErrors": []
              }
            }""";

    public static final String TIME_SLOT_CONFLICT = """
            {
              "success": false,
              "error": {
                "message": "Time slot overlaps with an existing time slot",
                "status": 409,
                "code": "TIME_SLOT_CONFLICT",
                "fieldErrors": []
              }
            }""";

    public static final String RESERVATION_NOT_FOUND = """
            {
              "success": false,
              "error": {
                "message": "Reservation not found",
                "status": 404,
                "code": "RESERVATION_NOT_FOUND",
                "fieldErrors": []
              }
            }""";

    public static final String RESERVATION_CONFLICT = """
            {
              "success": false,
              "error": {
                "message": "Time slot is already reserved for this date",
                "status": 409,
                "code": "RESERVATION_CONFLICT",
                "fieldErrors": []
              }
            }""";

    public static final String RESERVATION_ALREADY_CANCELLED = """
            {
              "success": false,
              "error": {
                "message": "Reservation already cancelled",
                "status": 409,
                "code": "RESERVATION_ALREADY_CANCELLED",
                "fieldErrors": []
              }
            }""";
}
