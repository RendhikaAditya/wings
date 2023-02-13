package co.dh.wings.network

import co.dh.wings.network.response.BaseResponse
import co.dh.wings.network.response.LoginResponse
import co.dh.wings.network.response.ProductResponse
import retrofit2.Response
import retrofit2.http.*

interface EndPoin {


    @GET("get_product.php")
    suspend fun product(): Response<ProductResponse>

    @FormUrlEncoded
    @POST("login.php")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("do_transaction.php")
    suspend fun TransactionHeader(
        @Field("document_code") document_code: String,
        @Field("document_number") document_number: String,
        @Field("user") user: String,
        @Field("total") total: Int
    ): Response<BaseResponse>

    @FormUrlEncoded
    @POST("do_transaction_detail.php")
    suspend fun TransactionDetail(
        @Field("document_code") document_code: String,
        @Field("document_number") document_number: String,
        @Field("product_code") product_code: String,
        @Field("price") price: String,
        @Field("quantity") quantity: String,
        @Field("unit") unit: String,
        @Field("sub_total") sub_total: String,
        @Field("currency") currency: String,
        ): Response<BaseResponse>

}