package dragau.o2o.customer.api.requests

import androidx.databinding.ObservableArrayList
import dragau.o2o.customer.models.enums.ParameterType
import dragau.o2o.customer.models.objects.BaseParameter
import dragau.o2o.customer.models.objects.Parameter
import kotlin.collections.ArrayList

class ProductRegisterRequest (
    var productId: String? = null,
    var productCategoryId: String? = null,
    var productParameters: ArrayList<Parameter>? = null,
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
            val params = ArrayList<Parameter>()
            val tempList = model.parameters?.filter { it.value != null
                    && it.type != ParameterType.BARCODE
                    &&  it.type != ParameterType.LIST }?.
                map {
                    Parameter(
                        it.id!!,
                        it.type,
                        it.name,
                        it.value,
                        it.Uom
                    )
                }
            if (tempList != null)
            {
                params.addAll(tempList)
            }

            /*if (model.parameters != null && model.parameters!!.count{ it.type == ParameterType.LIST && it.isRoot } > 0) {
                params.addAll(model.parameters!!.filter{ it.type == ParameterType.LIST && it.isRoot }.map{
                    Parameter(it.id, it.type, it.name, getLookupValue(model.parameters!!, it.value.toString()), it.Uom)
                })
            }

            else if (model.parameters != null && model.parameters!!.count{ it.type == ParameterType.LIST && !it.isRoot} > 0) {
                params.addAll(model.parameters!!.filter{ it.type == ParameterType.LIST && !it.isRoot }.map{
                    Parameter(it.id, it.type, it.name, it.selectedId, it.Uom)
                })
            }*/

            val request = ProductRegisterRequest(
                productId = model.productId,
                productCategoryId = model.productCategoryId,
                productParameters = params,
                manufacturer = model.produserName,
                name = model.title,
                barCode = model.productBarcode?.barcode,
                barcodeFormat = model.productBarcode?.barcodeFormat?.ordinal,
                description = model.describe
            )

            return request
        }

        private fun getLookupValue(parameters: ObservableArrayList<BaseParameter>, parentId: String): String?
        {
            val lookup = parameters.firstOrNull { it.id == parentId }
            if(lookup != null)
            {
                return getLookupValue(parameters, lookup.value.toString())
            }
            return parameters.firstOrNull { it.value == parentId }?.value.toString()
        }
    }


}