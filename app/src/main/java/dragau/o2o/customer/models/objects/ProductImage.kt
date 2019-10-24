package dragau.o2o.customer.models.objects

import com.google.gson.annotations.SerializedName

data class ProductImage(
    @field:SerializedName("annotationId")
    var annotationId: String? = null,
    @field:SerializedName("body")
    var body: String? = null
)
{
}