package response.store

data class MyStoreResponse(
    val userId: String,
    val merchantId: String,
    val merchantName: String,
    val storeId: String,
    val storeName: String,
    val contactNumber: String? = null,
    val storeLogo: String? = null,
    val location: String? = null,
    val totalTicket: Int
) {
    companion object {
        fun from(data: MyStoreResponse): MyStoreResponse {
            return MyStoreResponse(
                userId = data.userId,
                merchantId = data.merchantId,
                merchantName = data.merchantName,
                storeId = data.storeId,
                storeName = data.storeName,
                contactNumber = data.contactNumber,
                storeLogo = data.storeLogo,
                location = data.location,
                totalTicket = data.totalTicket
            )
        }
    }
}
