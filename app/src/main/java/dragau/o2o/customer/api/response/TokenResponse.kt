package dragau.o2o.customer.api.response

import dragau.o2o.customer.models.objects.TokenResult


class TokenResponse: BaseResponse() {
    override val resultObject: TokenResult? = null

    override fun toString(): String {
        return "BaseResponse(messageList=$messageList, result=$result, resultObject=$resultObject)"
    }

}