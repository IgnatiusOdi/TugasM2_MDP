package com.example.tugas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts

class LoginActivity : AppCompatActivity() {
    lateinit var etUsername: EditText
    lateinit var etPassword: EditText
    lateinit var btLogin: Button
    lateinit var btToRegister: Button

    var users: ArrayList<User> = ArrayList()
    var indexUser: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etUsername = findViewById(R.id.etUsernameLogin)
        etPassword = findViewById(R.id.etPasswordLogin)
        btLogin = findViewById(R.id.btLogin)
        btToRegister = findViewById(R.id.btToRegister)

        users.add(User("a", "a", "a", 1))

        val goToFarming = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                if (data != null) {
                    val user: User = data.getParcelableExtra("user")!!
                    users[indexUser] = user
                }
            }
        }

        btLogin.setOnClickListener {
            indexUser = loginCheck()
            if (indexUser != -1) {
                val intent = Intent(this, FarmingActivity::class.java)
                intent.putExtra("user", users[indexUser])
                goToFarming.launch(intent)
            }
        }

        val goToRegister = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                if (data != null) {
                    users = data.getParcelableArrayListExtra("users")!!
                }
            }
        }

        btToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.putParcelableArrayListExtra("users", users)
            goToRegister.launch(intent)
        }
    }

    private fun loginCheck(): Int {
        if (etUsername.text.isBlank() || etPassword.text.isBlank()) {
            // FIELD KOSONG
            Toast.makeText(this@LoginActivity, "Field tidak boleh kosong!", Toast.LENGTH_SHORT).show()
        } else {
            // CEK USER
            for(user in users) {
                if (user.username == etUsername.text.toString()) {
                    // CEK PASSWORD
                    if (user.password == etPassword.text.toString()) {
                        return users.indexOf(user)
                    } else {
                        Toast.makeText(this@LoginActivity, "Password salah!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            // USER TIDAK DITEMUKAN
            Toast.makeText(this@LoginActivity, "User tidak ditemukan!", Toast.LENGTH_SHORT).show()
        }
        return -1
    }
}