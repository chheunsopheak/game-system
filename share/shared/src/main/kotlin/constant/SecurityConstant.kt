package constant

object SecurityConstant {
    const val JWT_SECRET: String = "YS1zdHJpbmctc2VjcmV0LWF0LWxlYXN0LTI1Ni1iaXRzLWxvbmc="
    const val JWT_EXPIRATION_MS: Long = 24 * 60 * 60 * 1000
    const val AUTH_HEADER_NAME: String = "Authorization"
    const val TOKEN_PREFIX: String = "Bearer "
}
