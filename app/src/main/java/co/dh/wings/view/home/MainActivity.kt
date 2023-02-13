package co.dh.wings.view.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import co.dh.wings.R
import co.dh.wings.databinding.ActivityMainBinding
import co.dh.wings.network.ApiService
import co.dh.wings.network.response.ProductResponse
import co.dh.wings.network.Repository
import co.dh.wings.network.response.Transactions
import co.dh.wings.util.helper
import co.dh.wings.view.checkout.ChackoutActivity
import co.dh.wings.view.detail.DetailActivity
import co.id.tesmandiritmdb.network.Resource
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private val api by lazy { ApiService.getClient() }
    private lateinit var viewModel: MainViewModel
    private lateinit var repository: Repository
    private lateinit var viewModelFactory: MainViewModelFactory
    private lateinit var adapter: ProductAdapter

    private lateinit var transactionsViewModel: TransactionsViewModel
    var helpers = helper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setupViewModel()
        setupObserve()
        setupData()
        setupListener()


    }

    private fun setupListener() {

        binding.btnChackout.setOnClickListener {
            startActivity(Intent(this@MainActivity, ChackoutActivity::class.java))
        }
    }

    private fun setupData() {
        adapter = ProductAdapter(
            arrayListOf(),
            object : ProductAdapter.OnAdapterListener {
                override fun onClick(result: ProductResponse.Data) {
                    showDIalog(result)
                }

                override fun onClickDetail(result: ProductResponse.Data) {
                    startActivity(Intent(this@MainActivity, DetailActivity::class.java)
                        .putExtra("data", result))
                }
            }
        )
        adapter.notifyDataSetChanged()
        binding.rvProduct.adapter = adapter
    }

    private fun setupObserve() {

        viewModel.fetchProduct()
        viewModel.product.observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    Log.d("product", " :: Loading")
                }
                is Resource.Success -> {
                    Log.d("product", " :: response :: ${it.data!!}")
                    adapter.setData(it.data.data)

                }
                is Resource.Error -> {
                    Log.d("product", " :: Error ")

                }
            }
        })
    }

    private fun setupViewModel() {
        repository = Repository(api)
        viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        transactionsViewModel = ViewModelProvider(this).get(TransactionsViewModel::class.java)
    }

    @SuppressLint("SuspiciousIndentation")
    private fun showDIalog(result: ProductResponse.Data) {
        val dialog = Dialog(this@MainActivity)
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
            }
        }

        closeBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

}