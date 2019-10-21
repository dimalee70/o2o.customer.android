package dragau.o2o.customer.models.objects

import com.google.gson.annotations.SerializedName

data class ProductCategories(
    @field:SerializedName("productCategoryId")
    var productCategoryId: String? = null,

    @field:SerializedName("productName")
    var productName: String?,

    @field:SerializedName("stateCode")
    var stateCode: Int? = null,

    @field:SerializedName("statusCode")
    var statusCode: Int? = null
){
    override fun toString(): String {
        return productName!!
    }
}