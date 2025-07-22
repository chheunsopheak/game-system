package request.user

import org.jetbrains.annotations.NotNull

data class UserClientRegisterRequest(
    @NotNull("ref is required")
    var ref: String,
    var gender: String,
    var name: String,
    var dob: String?,
    var nid: String?,
    var passcode: String
)
