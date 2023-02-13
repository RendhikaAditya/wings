package co.dh.wings.view.detail

import android.annotation.SuppressLint
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import co.dh.wings.R
import co.dh.wings.databinding.ActivityDetailBinding
import co.dh.wings.network.response.ProductResponse
import co.dh.wings.network.response.Transactions
import co.dh.wings.util.helper
import co.dh.wings.view.home.TransactionsViewModel

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    var helpers = helper()
    private lateinit var transactionsViewModel: TransactionsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupListener()
        setupIntent()

    }

    private fun setupIntent() {
        val helper = helper()
        var data : ProductResponse.Data = intent.getSerializableExtra("data") as ProductResponse.Data
        binding.harga.setText(helper.formatRupiah(data.price!!.toDouble()))
        binding.namaProduk.setText(data.product_name)
        binding.priceUnit.setText(data.unit)
        binding.dimensi.setText(data.dimension)
    }

    private fun setupListener() {
        binding.btnBuy.setOnClickListener {
            showDIalog(intent.getSerializableExtra("data") as ProductResponse.Data)
        }
    }

    private fun setupViewModel() {
        transactionsViewModel = ViewModelProvider(this).get(TransactionsViewModel::class.java)

    }

    @SuppressLint("SuspiciousIndentation")
    private fun showDIalog(result: ProductResponse.Data) {
        val dialog = Dialog(this@DetailActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_add)

        val jmlEt = dialog.findViewById<TextView>(R.id.jmlItem)
        val closeBtn = dialog.findViewById<ImageView>(R.id.btnClose)
        val tambahBtn = dialog.findViewById<TextView>(R.id.btnTambah)
        val kurangBtn = dialog.findViewById<TextView>(R.id.btnKurang)
        val submitButton = dialog.findViewById<TextView>(R.id.submit_button)

        jmlEt.setText("1")

        var jml = 1
        tambahBtn.setOnClickListener {
            jml = jml + 1
            jmlEt.setText("$jml")
        }

        kurangBtn.setOnClickListener {
            if (jml > 0) {
                jml = jml - 1
                jmlEt.setText("$jml")
            }
        }



        submitButton.setOnClickListener {
            if (jml < 1) {
                Toast.makeText(this, "Jumlah minimal harus 1", Toast.LENGTH_SHORT).show()
            } else {
                val transaction = Transactions(
                    currency = result.currency.toString(),
                    document_code = "TRX",
                    document_number = 0,
                    price = helpers.calculateDiscount(
                        result.price!!.toDouble(),
                        result.discount.toDouble()
                    ).toString(),
                    product_code = result.product_code.toString(),
                    quantity = jml,
                    sub_total = (helpers.calculateDiscount(
                        result.price.toDouble(),
                        result.discount.toDouble()
                    )*jml).toString(),
                    unit = result.unit.toString(),
                    name = result.product_name.toString()
                )
                val exist = transactionsViewModel.checkTransactionExists(transaction)
                if (exist) {

                    val qty = transactionsViewModel.getQty(result.product_code.toString())

                    transactionsViewModel.updateTransactionByProductCode(
                        jml + qty!!.toInt(),
                        result.product_code.toString()
                    )
                } else {
                    transactionsViewModel.insert(transaction)
                }




                dialog.dismiss()
                finish()
            }
        }

        closeBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

}