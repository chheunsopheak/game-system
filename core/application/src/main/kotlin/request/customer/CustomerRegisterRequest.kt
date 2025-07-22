package request.customer

data class CustomerRegisterRequest(
    var ref: String,
    var phone: String,
    var gender: String,
    var name: String,
    var dob: String?,
    var nid: String?,
    var passcode: String
)
