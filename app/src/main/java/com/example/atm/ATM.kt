package com.example.atm

class ATM {
    private val availableDenominations = mutableMapOf(
        2000 to 0,
        500 to 0,
        200 to 0,
        100 to 0
    )


    fun deposit(denomination: Int, numNotes: Int): String {
        val validDenominations = setOf(100, 200, 500, 2000)

        if (denomination !in validDenominations) {
            return "Invalid denomination. Allowed denominations: ${validDenominations.joinToString(", ")}"
        }

        if (numNotes <= 0) {
            return "Invalid number of notes. Please enter a positive number."
        }

        val amount = denomination * numNotes

        if (amount % denomination != 0) {
            return "Invalid deposit amount. Amount should be a multiple of $denomination."
        }

        availableDenominations[denomination] =
            availableDenominations.getOrDefault(denomination, 0) + numNotes
        return "Deposit successful. Denomination $denomination updated."
    }


    fun withdraw(amount: Int): Boolean {
        if (amount <= 0) {
            return false
        }

        val sortedDenominations = availableDenominations.keys.sortedDescending()
        var remainingAmount = amount

        val withdrawalDenominations = mutableMapOf<Int, Int>()

        for (denomination in sortedDenominations) {
            val availableNotes = availableDenominations[denomination] ?: 0
            val numNotesToWithdraw = minOf(remainingAmount / denomination, availableNotes)

            if (numNotesToWithdraw > 0) {
                withdrawalDenominations[denomination] = numNotesToWithdraw
                remainingAmount -= denomination * numNotesToWithdraw
            }

            if (remainingAmount == 0) {
                break
            }
        }

        if (remainingAmount > 0) {
            return false  // Insufficient denominations for requested withdrawal
        }

        for ((denomination, numNotes) in withdrawalDenominations) {
            availableDenominations[denomination] =
                availableDenominations.getOrDefault(denomination, 0) - numNotes
        }

        return true  // Withdrawal successful
    }

    fun getAvailableDenominationCount(denomination: Int): Int {
        return availableDenominations.getOrDefault(denomination, 0)
    }

    fun getTotalAvailableValue(): Int {
        return availableDenominations.entries.sumBy { (denomination, count) -> denomination * count }
    }

}
