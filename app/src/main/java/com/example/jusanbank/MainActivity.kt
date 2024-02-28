package com.example.jusanbank

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat

private const val TAG = "MainActivity"
private const val INITIAL_PERCENT = 23

class MainActivity : AppCompatActivity() {

    private lateinit var etDeposit: EditText
    private lateinit var tvComissionAmount: TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var seekBarPercent: SeekBar
    private lateinit var tvPercentLabel: TextView
    private lateinit var tvPercentDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initButtons()
        initCalculator()
    }
    private fun initCalculator() {

        etDeposit = findViewById(R.id.etBaseAmount)

        etDeposit.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "after text changed $s")
                calculate()
            }

        })

        tvPercentLabel = findViewById(R.id.tvPercentLabel)
        tvPercentDescription = findViewById(R.id.tvPercentDescription)
        seekBarPercent = findViewById(R.id.seekBarPercent)
        seekBarPercent.progress = INITIAL_PERCENT
        tvPercentLabel.text = "$INITIAL_PERCENT%"
        updatePercentDescription(INITIAL_PERCENT)
        seekBarPercent.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")
                tvPercentLabel.text = "$progress%"
                calculate()
                updatePercentDescription(progress)
              }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        tvComissionAmount = findViewById(R.id.tvComissionAmount)

        tvTotalAmount = findViewById(R.id.tvTotalAmount)

    }

    private fun updatePercentDescription(percent: Int) {
        val description = when(percent) {
            in 0..9 -> getString(R.string.pension)
            in 10..14 -> getString(R.string.optimal)
            in 15..19 -> getString(R.string.comfort)
            in 20..24 -> getString(R.string.businessman)
            else -> getString(R.string.maximum)
        }
        tvPercentDescription.text = description

        val color = ArgbEvaluator().evaluate(
            percent.toFloat() / seekBarPercent.max,
            ContextCompat.getColor(this, R.color.color_worst_percent),
            ContextCompat.getColor(this, R.color.color_best_percent)
        ) as Int
        tvPercentDescription.setTextColor(color)
    }

    private fun calculate() {
        if(etDeposit.text.isEmpty()) {
            tvComissionAmount.text = ""
            tvTotalAmount.text = ""
            return
        }
        val baseAmount = etDeposit.text.toString().toDouble()
        val percent = seekBarPercent.progress

        val comission = baseAmount * percent / 100
        val totalAmount = baseAmount + comission

        tvComissionAmount.text = "%.2f".format(comission)
        tvTotalAmount.text = "%.2f".format(totalAmount)
    }

    private fun initButtons() {

        val b0: Button = findViewById(R.id.button0)
        val b1: Button = findViewById(R.id.button1)
        val b2: Button = findViewById(R.id.button2)
        val b3: Button = findViewById(R.id.button3)
        val b4: Button = findViewById(R.id.button4)
        val b5: Button = findViewById(R.id.button5)
        val b6: Button = findViewById(R.id.button6)
        val b7: Button = findViewById(R.id.button7)
        val b8: Button = findViewById(R.id.button8)
        val b9: Button = findViewById(R.id.button9)

        b0.setOnClickListener ( ::onNumButtonClick )
        b1.setOnClickListener ( ::onNumButtonClick )
        b2.setOnClickListener ( ::onNumButtonClick )
        b3.setOnClickListener ( ::onNumButtonClick )
        b4.setOnClickListener ( ::onNumButtonClick )
        b5.setOnClickListener ( ::onNumButtonClick )
        b6.setOnClickListener ( ::onNumButtonClick )
        b7.setOnClickListener ( ::onNumButtonClick )
        b8.setOnClickListener ( ::onNumButtonClick )
        b9.setOnClickListener (::onNumButtonClick )

        val back: Button = findViewById(R.id.buttonBack)
        back.setOnClickListener {
            val text= etDeposit.text.dropLast(1)
            etDeposit.setText(text, TextView.BufferType.EDITABLE)

        }

        val ok: Button = findViewById(R.id.buttonOk)
        ok.setOnClickListener {
            Toast.makeText(this, tvTotalAmount.text, Toast.LENGTH_LONG).show()
        }
    }

    private fun onNumButtonClick(view: View){

        if (view !is Button) return

        val text = view.text.toString()

        etDeposit.setText(etDeposit.text.toString()+text, TextView.BufferType.EDITABLE)

    }
}