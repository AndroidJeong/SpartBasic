package com.example.spartabasic

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.spartabasic.databinding.ActivityMainBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var job: Job? = null
    private  lateinit var binding: ActivityMainBinding

    private val TAG = "MainActivity"
    private var counter = 1
    private var buttonCheck = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate")
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        /*  savedInstanceState?.let {
              counter = it.getInt("counter")
          }*/

        setupButton()
        setRandomValueBetweenOneToHundred()
        //setJobAndLaunch()

    }

    override fun onRestart() {
        super.onRestart()
        Log.i(TAG, "onRestart")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume")
        // onRestoreInstanceState -> onResume 순서라서
        if(buttonCheck){
            setJobAndLaunch()
        } else {
            binding.spartaTextView.text = counter.toString()
        }

    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause")
        job?.cancel()
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop")
        job?.cancel()

    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.i(TAG, "onRestoreInstanceState")
        counter = savedInstanceState.getInt("counter")
        buttonCheck = savedInstanceState.getBoolean("buttonCheck")
        binding.textViewRandom.text = savedInstanceState.getString("randomValue")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState")
        outState.putInt("counter", counter)
        outState.putBoolean("buttonCheck", buttonCheck)
        outState.putString("randomValue", binding.textViewRandom.text.toString())
    }

    private fun setupButton() {
        binding.clickButton.setOnClickListener {
            checkAnswerAndShowToast()
            job?.cancel()
            buttonCheck = false
        }
    }

    private fun setRandomValueBetweenOneToHundred() {
        val randomValue = (1..100).random()
        binding.textViewRandom.text = randomValue.toString()
    }


    private fun setJobAndLaunch() {
        job?.cancel() // job is uninitialized exception
        job = lifecycleScope.launch {
            while (counter <= 100) {
                if (isActive) {
                    //증감연산자 변경
                    binding.spartaTextView.text = (++counter).toString()
                    delay(500) // 1초 = 1000
                }
            }
        }



    }

    private fun checkAnswerAndShowToast() {
        val spartaText = binding.spartaTextView.text.toString()
        val randomText = binding.textViewRandom.text.toString()
        if (spartaText == randomText) {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT).show()
        }
    }
}