package dragau.o2o.customer.api

import com.google.gson.JsonObject
import dragau.o2o.customer.api.requests.ProductRegisterRequest
import dragau.o2o.customer.api.response.*
import io.reactivex.Observable
import io.reactivex.Single
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
    fun createProduct(@Body body: ProductRegisterRequest): Observable<CreateResponse>

    @POST("v1/product/updateproduct")
    fun updateProduct(@Body body: ProductRegisterRequest): Observable<CreateResponse>

    @POST("v1/product/uploadphoto")
    fun uploadProductPhoto(@Body body: JsonObject): Observable<CreateResponse>

    @POST("v1/product/updatephoto")
    fun updatePhoto(@Body body: JsonObject): Observable<CreateResponse>

    @GET("v1/product/getproductbycontact")
    fun getProductsByContact(@Query("ContactId")contactId: String, @Query("index") index: Int): Single<ProductResponceContact>

    @GET("v1/sync/getparameters")
    fun getParameters(): Observable<ParametersResponse>

    @GET("v1/product/getproductthumbnails")
    fun getPhoto(@Query("ProductId") productId: String): Observable<String>

    @GET("v1/sync/GetLookups")
    fun getCategories(): Observable<LookupCustomResponse>

    @GET("/api/v1/product/get")
    fun getProduct(@Query("productId") productId: String): Observable<ProductResponce>
}