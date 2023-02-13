package co.dh.wings.network.response

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Transactions(
    @ColumnInfo(name = "currency") val currency: String,
    @ColumnInfo(name = "document_code") val document_code: String,
    @PrimaryKey(autoGenerate = true)
    val document_number: Int,
    @ColumnInfo(name = "price") val price: String,
    @ColumnInfo(name = "product_code") val product_code: String,
    @ColumnInfo(name = "quantity") val quantity: Int,
    @ColumnInfo(name = "sub_total") val sub_total: String,
    @ColumnInfo(name = "unit") val unit: String,
    @ColumnInfo(name = "name") val name: String
)
