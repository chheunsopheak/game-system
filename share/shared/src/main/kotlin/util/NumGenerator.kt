package util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random

object NumGenerator {
    fun generateRandomNumber(length: Int): String {
        val numbers = ('0'..'9')
        return (1..length)
            .map { numbers.random() }
            .joinToString("")
    }

    fun generateNo(text: String): String {
        val date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        val randomSuffix = Random.nextInt(1000, 9999)
        return "$text$date$randomSuffix"
    }

    fun generateCode(text: String): String {
        val randomNumber = Random.nextInt(0, 10000)
        val formattedNumber = randomNumber.toString().padStart(4, '0')
        return "$text$formattedNumber"
    }

    fun generateEmail(prefix: String = "user", domain: String = "example.com"): String {
        val uniquePart = generateRandomNumber(6)
        return "$prefix$uniquePart@$domain"
    }
}