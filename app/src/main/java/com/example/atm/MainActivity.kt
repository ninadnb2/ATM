package com.example.atm


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private val atm = ATM()
    private lateinit var availableNotes2000: TextView
    private lateinit var availableNotes500: TextView
    private lateinit var availableNotes200: TextView
    private lateinit var availableNotes100: TextView
    private lateinit var textTotal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        availableNotes2000 = findViewById(R.id.availableNotes2000)
        availableNotes500 = findViewById(R.id.availableNotes500)
        availableNotes200 = findViewById(R.id.availableNotes200)
        availableNotes100 = findViewById(R.id.availableNotes100)
        textTotal = findViewById(R.id.TextTotal)

        val depositButton: Button = findViewById(R.id.depositButton)
        val withdrawButton: Button = findViewById(R.id.withdrawButton)
        val exitButton: Button = findViewById(R.id.exitButton)

        depositButton.setOnClickListener {
            val denominationsToDeposit = mapOf(
                2000 to findViewById<EditText>(R.id.editText2000).text.toString().toIntOrNull(),
                500 to findViewById<EditText>(R.id.editText500).text.toString().toIntOrNull(),
                200 to findViewById<EditText>(R.id.editText200).text.toString().toIntOrNull(),
                100 to findViewById<EditText>(R.id.editText100).text.toString().toIntOrNull()
            )

            var anyDepositsMade = false

            for ((denomination, numNotes) in denominationsToDeposit) {
                if (numNotes != null && numNotes > 0) {
                    val depositAmount = denomination * numNotes
                    atm.deposit(denomination, numNotes)
                    findViewById<EditText>(getEditTextIdForDenomination(denomination)).text.clear()
                    anyDepositsMade = true
                }
            }

            if (anyDepositsMade) {
                updateAvailableDenominations()
            } else {
                Toast.makeText(this, "Please enter the number of notes to deposit", Toast.LENGTH_SHORT).show()
            }
        }







        /*  withdrawButton.setOnClickListener {
              val withdrawEditText: EditText = findViewById(R.id.editTextWithdraw)
              val withdrawalAmountStr = withdrawEditText.text.toString()

              if (withdrawalAmountStr.isNotBlank()) {
                  val withdrawalAmount = withdrawalAmountStr.toInt()

                  val withdrawalSuccessful = atm.withdrawSeparateDenominations(withdrawalAmount)

                  if (withdrawalSuccessful) {
                      updateAvailableDenominations()
                      withdrawEditText.text.clear()
                  } else {
                      // Show an error message or handle withdrawal failure
                      Toast.makeText(this, "Withdrawal failed: Insufficient denominations available.", Toast.LENGTH_SHORT).show()
                  }
              } else {
                  // Show an error message or handle the case when no withdrawal amount is entered
                  Toast.makeText(this, "Please enter a withdrawal amount", Toast.LENGTH_SHORT).show()
              }
          }*/
        withdrawButton.setOnClickListener {
            val withdrawalEditText: EditText = findViewById(R.id.editTextWithdraw)
            val withdrawalText = withdrawalEditText.text.toString()

            if (withdrawalText.isNotEmpty()) {
                val withdrawalAmount = withdrawalText.toInt()

                if (atm.withdraw(withdrawalAmount)) {
                    // If withdrawal is successful, update the available denominations and total value
                    updateAvailableDenominations()
                    updateTotalAvailableValue()
                    withdrawalEditText.text.clear()
                } else {
                    // Handle withdrawal failure due to insufficient denominations
                    Toast.makeText(this, "Withdrawal failed: Insufficient denominations available.", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Handle the case when no withdrawal amount is entered
                Toast.makeText(this, "Please enter a withdrawal amount", Toast.LENGTH_SHORT).show()
            }
        }





















        exitButton.setOnClickListener {
            finish()
        }

        updateAvailableDenominations()
    }

    private fun updateAvailableDenominations() {
        availableNotes2000.text = atm.getAvailableDenominationCount(2000).toString()
        availableNotes500.text = atm.getAvailableDenominationCount(500).toString()
        availableNotes200.text = atm.getAvailableDenominationCount(200).toString()
        availableNotes100.text = atm.getAvailableDenominationCount(100).toString()

        val totalValue = atm.getTotalAvailableValue()
        textTotal.text = totalValue.toString()
    }



    private fun updateTotalAvailableValue() {
        val totalValue = atm.getTotalAvailableValue()
        textTotal.text = totalValue.toString()
    }



    private fun getEditTextIdForDenomination(denomination: Int): Int {
        return when (denomination) {
            2000 -> R.id.editText2000
            500 -> R.id.editText500
            200 -> R.id.editText200
            100 -> R.id.editText100
            else -> throw IllegalArgumentException("Unknown denomination: $denomination")
        }
    }

}
