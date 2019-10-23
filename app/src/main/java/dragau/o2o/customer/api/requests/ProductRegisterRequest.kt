package dragau.o2o.customer.api.requests

import com.google.zxing.BarcodeFormat
import dragau.o2o.customer.models.enums.ParameterType
import dragau.o2o.customer.models.objects.BaseParameter
import dragau.o2o.customer.models.objects.ProductImage
import java.util.*

class ProductRegisterRequest (
    var productId: String? = null,
    var productCategoryId: String? = null,
    var productParameters: List<ParameterRequest>? = null,
    var manufacturer: String? = null,
    var name: String? = null,
    var barCode: String? = null,
    val barcodeFormat: Int? = null,
    var description: String? = null
)
{
    companion object {
        fun from(model: ProductRegisterViewModel): ProductRegisterRequest
        {
            val request = ProductRegisterRequest(
                productId = model.productId,
                productCategoryId = model.productCategoryId,
                productParameters = model.parameters?.filter { it.value != null && it.type != ParameterType.BARCODE }?.map { ParameterRequest(it.id, it.type, it.name, it.value, it.Uom) },
                manufacturer = model.produserName,
                name = model.title,
                barCode = model.productBarcode?.barcode,
                barcodeFormat = model.productBarcode?.barcodeFormat?.ordinal,
                description = model.describe
            )

            return request
        }
    }
}