package co.dh.wings.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import co.dh.wings.databinding.ActivityLoginBinding
import co.dh.wings.network.ApiService
import co.dh.wings.network.Repository
import co.dh.wings.util.SharedPreferences
import co.dh.wings.view.home.MainActivity
import co.id.tesmandiritmdb.network.Resource

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val api by lazy { ApiService.getClient() }
    private lateinit var viewModel: LoginViewModel
    private lateinit var repository: Repository
    private lateinit var viewModelFactory: LoginViewModelFactory

    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        preferences = SharedPreferences(this)

        if (preferences.getString("isLogin").equals("true")){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        setupViewModel()
        listener()
        setupObserve()
    }

    private fun setupObserve() {
        viewModel.login.observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    Log.d("login", " :: Loading")
                }
                is Resource.Success -> {
                    Log.d("login", " :: response :: ${it.data!!}")
                    preferences.put("isLogin", "true")
                    preferences.put("name", it.data.user.user)
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
                is Resource.Error -> {
                    Log.d("login", " :: Error ")

                }
            }
        })
    }

    private fun listener() {
        binding.masuk.setOnClickListener {
            viewModel.fetchLogin(binding.username.text.toString(), binding.password.text.toString())
        }
    }

    private fun setupViewModel() {
        repository = Repository(api)
        viewModelFactory = LoginViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
    }
}