package constant

object ApplicationMessage {

    // ✅ General Success
    const val SUCCESS = "Operation completed successfully."
    const val SAVED = "Saved successfully."
    const val UPDATED = "Updated successfully."
    const val DELETED = "Deleted successfully."
    const val FETCHED = "Data retrieved successfully."
    const val EXISTING = "Record already exists."
    const val FAILED = "Operation failed."
    const val NOT_FOUND = "Record not found."
    const val SOMETHING_WENT_WRONG = "Something went wrong. Please try again later."

    // ✅ Authentication
    const val LOGIN_SUCCESS = "Login successful."
    const val LOGIN_FAILED = "Login failed. Please check your credentials."
    const val LOGOUT_SUCCESS = "Logged out successfully."
    const val LOGOUT_FAILED = "Failed to log out."

    const val REGISTER_SUCCESS = "Registration successful."
    const val INVALID_CREDENTIALS = "Invalid username or password."
    const val UNAUTHORIZED = "Unauthorized access. Please log in."
    const val FORBIDDEN = "You don't have permission to perform this action."
    const val ACCOUNT_DISABLED = "Account is disabled. Contact support."
    const val TOKEN_EXPIRED = "Your session has expired. Please log in again."
    const val TOKEN_INVALID = "Invalid authentication token."
    const val INVALID_PASSWORD = "Invalid password."
    const val PASSWORD_NOT_MATCH = "Current Password does not match with Old Password."
    const val PASSWORD_CHANGED = "Password changed successfully."
    const val PASSWORD_RESET = "Password reset successfully."
    const val INVALID_REFRESH_TOKEN = "Invalid refresh token, please log in again."

    // ✅ Validation & Input
    const val REQUIRED_FIELD_MISSING = "Required field is missing."
    const val INVALID_INPUT = "Invalid input format."
    const val PASSWORD_TOO_WEAK = "Password does not meet strength requirements."
    const val EMAIL_INVALID = "Email format is invalid."
    const val PHONE_INVALID = "Phone number format is invalid."
    const val USERNAME_TAKEN = "Username is already taken."
    const val EMAIL_TAKEN = "Email is already registered."

    // ✅ Resource-related
    const val USER_NOT_FOUND = "User not found."
    const val RESOURCE_NOT_FOUND = "Requested resource not found."
    const val DUPLICATE_ENTRY = "Duplicate record exists."
    const val RECORD_NOT_FOUND = "No matching record found."
    const val USER_NOT_ACTIVE = "User is not active."
    const val USER_LOCKED = "User account is locked."

    // ✅ Server & System
    const val INTERNAL_ERROR = "Internal server error. Please try again later."
    const val SERVICE_UNAVAILABLE = "Service temporarily unavailable."
    const val OPERATION_FAILED = "Operation failed. Please try again."

    // ✅ Email & Verification
    const val EMAIL_SENT = "Email has been sent."
    const val EMAIL_FAILED = "Failed to send email."
    const val VERIFICATION_SUCCESS = "Verification successful."
    const val VERIFICATION_FAILED = "Verification failed or expired."

    // ✅ File Operations
    const val FILE_UPLOADED = "File uploaded successfully."
    const val FILE_DOWNLOAD_READY = "File is ready for download."
    const val FILE_NOT_FOUND = "File not found."
    const val FILE_UPLOAD_FAILED = "Failed to upload file."

    // ✅ Pagination & Data
    const val NO_DATA_FOUND = "No data available."
    const val PAGE_OUT_OF_RANGE = "Page number is out of range."

    // ✅ Permission & Access
    const val NO_PERMISSION = "You do not have permission to access this resource."

}