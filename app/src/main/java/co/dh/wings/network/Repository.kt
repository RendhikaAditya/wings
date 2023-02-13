package co.dh.wings.network

import co.dh.wings.network.EndPoin

class Repository(
    private val api: EndPoin
) {

    suspend fun fetchLogin(user: String, password: String) = api.login(user, password)

    suspend fun fetchTransaction(
        document_code: String,
        document_number: String,
        user: String,
        total: Int
    ) = api.TransactionHeader(document_code, document_number, user, total)

    suspend fun fetchTransactionDetail(
        document_code: String,
        document_number: String,
        product_code: String,
        price: String,
        quantity: String,
        unit: String,
        sub_total: String,
        currency:String
    )= api.TransactionDetail(document_code, document_number, product_code, price, quantity, unit, sub_total, currency)

    suspend fun fetchProduct() = api.product()
}