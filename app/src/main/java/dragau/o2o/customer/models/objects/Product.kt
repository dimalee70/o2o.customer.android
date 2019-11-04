package dragau.o2o.customer.models.objects

import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

data class Product(
    @field:SerializedName("productId")
    var productId: String? = null,

    @field:SerializedName("productCategoryId")
    var productCategoryId: String? = null,

    @field:SerializedName("productImageBase64")
    var productImageBase64: String? = null,

    @field:SerializedName("name")
    var name: String? = null,

    @field:SerializedName("barCode")
    var barCode: String? = null,

    @field:SerializedName("manufacturer")
    var manufacturer: String? = null,

    @field:SerializedName("description")
    var description: String? = null,

    @field:SerializedName("productThumbnails")
    var productThumbnails: Stack<ProductImage>? = null,

    @field:SerializedName("productParameters")
    var productParameters: ArrayList<Parameter>? = null,

    @field:SerializedName("barcodeFormat")
    var barcodeFormat: Int? = null
    ){

    override fun equals(other: Any?): Boolean {
        return this.productId.equals((other as Product).productId)
    }

    override fun hashCode(): Int {
        return this.productId.hashCode()
    }
}