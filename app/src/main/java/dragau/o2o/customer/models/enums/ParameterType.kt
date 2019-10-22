package dragau.o2o.customer.models.enums

import com.google.gson.annotations.SerializedName

enum class ParameterType(val value: Int)
{
    @SerializedName("0")
    STRING(0),
    @SerializedName("1")
    INT(1),
    @SerializedName("2")
    DATE(2),
    @SerializedName("3")
    BOOL(3),
    @SerializedName("4")
    DECIMAL(4),
    @SerializedName("5")
    BARCODE(5),
    FOOTER(-1)
}