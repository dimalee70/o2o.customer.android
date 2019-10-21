package dragau.o2o.customer.models.objects

import com.google.gson.annotations.SerializedName

data class TokenResult(

    @field:SerializedName("systemUserId")
    val systemUserId: String? = null,

    @field:SerializedName("token")
    val token: String? = null,

    @field:SerializedName("expireDate")
    val expireDate: String? = null
) {
    override fun toString(): String {
        return "TokenResult(code=$token, expireDate=$expireDate)"
    }
}