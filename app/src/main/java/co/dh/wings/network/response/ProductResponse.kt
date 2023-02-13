package co.dh.wings.network.response

import java.io.Serializable


data class ProductResponse(
    val `data`: List<Data>,
    val message: String,
    val status: Int
) {
    class Data : Serializable {

        val currency: String? = null
        val dimension: String? = null
        val discount: Int = 0
        val price: String? = null
        val product_code: String? = null
        val product_name: String? = null
        val unit: String? = null
    }
}

