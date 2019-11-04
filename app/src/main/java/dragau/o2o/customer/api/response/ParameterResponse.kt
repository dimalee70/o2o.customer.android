package dragau.o2o.customer.api.response

import dragau.o2o.customer.models.objects.Parameter

class ParameterResponse: BaseResponse() {
    override val resultObject: ArrayList<Parameter>? = null
}