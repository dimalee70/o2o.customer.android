package dragau.o2o.customer.api

import com.google.gson.JsonObject
import dragau.o2o.customer.api.response.CreateResponse
import dragau.o2o.customer.api.response.ProductCategoriesResponce
import dragau.o2o.customer.api.response.ProductResponce
import io.reactivex.Observable
import dragau.o2o.customer.api.response.TokenResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiManager {

    @POST("v1/auth/gettoken")
    fun getToken(@Body body: JsonObject): Observable<TokenResponse>


    @GET("v1/product/getproductcategories")
    fun getProductategories(): Observable<ProductCategoriesResponce>

    @GET("v1/product/getproductbybarcode")
    fun getProductByBarcode(@Query("barcode") barcode: String): Observable<ProductResponce>

    @POST("v1/product/create")
    fun createProduct(@Body body: JsonObject): Observable<CreateResponse>

    @POST("v1/product/updateproduct")
    fun updateProduct(@Body body: JsonObject): Observable<CreateResponse>

    @POST("v1/product/uploadphoto")
    fun uploadProductPhoto(@Body body: JsonObject): Observable<CreateResponse>

    @POST("v1/product/updatephoto")
    fun updatePhoto(@Body body: JsonObject): Observable<CreateResponse>
}