package dragau.o2o.customer.api.response

import dragau.o2o.customer.api.requests.ParameterRequest
import dragau.o2o.customer.models.objects.BaseParameter

class ParametersResponse: BaseResponse() {
    override val resultObject: ArrayList<ParameterRequest>? = null
}