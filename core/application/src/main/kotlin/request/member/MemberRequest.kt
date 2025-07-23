package request.member

data class MemberRequest(
    val name: String,
    val phone: String,
    val ticket: Int,
    val rewardType: String
)
