package dragau.o2o.customer.api.response

import dragau.o2o.customer.api.response.BaseResponse
import dragau.o2o.customer.models.objects.Product


class ProductResponce: BaseResponse() {
    override val resultObject: Product? = null
}