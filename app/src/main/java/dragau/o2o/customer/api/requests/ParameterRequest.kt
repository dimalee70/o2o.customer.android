package dragau.o2o.customer.api.requests

import dragau.o2o.customer.models.enums.ParameterType

data class ParameterRequest
(
    val id: String?,
    val type: ParameterType,
    val name: String,
    val value: Any?,
    val Uom: String?
)