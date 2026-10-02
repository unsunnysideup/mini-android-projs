package ait.hu.calculatorapp

import ait.hu.calculatorapp.databinding.ActivityMainBinding
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.math.BigDecimal
import    android.icu.number.NumberFormatter
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.Locale
import kotlin.math.pow


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val operators = listOf("×", "÷", "+", "–", "%", "^")

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.keypadSeven.setOnClickListener {
            if (binding.result.text.toString() == "0") {
                binding.result.text = "7"
            } else if (!binding.result.text.contains("^[a-zA-Z]+\$".toRegex())) {
                binding.result.append("7")
            }
        }

        binding.keypadEight.setOnClickListener {
            if (binding.result.text.toString() == "0") {
                binding.result.text = "8"
            } else if (!binding.result.text.contains("^[a-zA-Z]+\$".toRegex())) {
                binding.result.append("8")
            }
        }

        binding.keypadNine.setOnClickListener {
            if (binding.result.text.toString() == "0") {
                binding.result.text = "9"
            } else if (!binding.result.text.contains("^[a-zA-Z]+\$".toRegex())) {
                binding.result.append("9")
            }
        }

        binding.keypadFour.setOnClickListener {
            if (binding.result.text.toString() == "0") {
                binding.result.text = "4"
            } else if (!binding.result.text.contains("^[a-zA-Z]+\$".toRegex())) {
                binding.result.append("4")
            }
        }

        binding.keypadFive.setOnClickListener {
            if (binding.result.text.toString() == "0") {
                binding.result.text = "5"
            } else if (!binding.result.text.contains("^[a-zA-Z]+\$".toRegex())) {
                binding.result.append("5")
            }
        }

        binding.keypadSix.setOnClickListener {
            if (binding.result.text.toString() == "0") {
                binding.result.text = "6"
            } else if (!binding.result.text.contains("^[a-zA-Z]+\$".toRegex())) {
                binding.result.append("6")
            }
        }

        binding.keypadOne.setOnClickListener {
            if (binding.result.text.toString() == "0") {
                binding.result.text = "1"
            } else if (!binding.result.text.contains("^[a-zA-Z]+\$".toRegex())) {
                binding.result.append("1")
            }
        }

        binding.keypadTwo.setOnClickListener {
            if (binding.result.text.toString() == "0") {
                binding.result.text = "2"
            } else if (!binding.result.text.contains("^[a-zA-Z]+\$".toRegex())) {
                binding.result.append("2")
            }
        }

        binding.keypadThree.setOnClickListener {
            if (binding.result.text.toString() == "0") {
                binding.result.text = "3"
            } else if (!binding.result.text.contains("^[a-zA-Z]+\$".toRegex())) {
                binding.result.append("3")
            }
        }

        binding.zero.setOnClickListener {
            if (binding.result.text.toString() != "0" && !binding.result.text.contains("^[a-zA-Z]+\$".toRegex())) {
                binding.result.append("0")
            }
        }

        binding.backspace.setOnClickListener {
            if (!binding.result.text.contains("^[a-zA-Z]+\$".toRegex())) {
                binding.result.text = binding.result.text.dropLast(1)
            }
        }

        binding.multiply.setOnClickListener {
            if (operators.none { it in binding.result.text } && !binding.result.text.contains("^[a-zA-Z]+\$".toRegex())) {
                binding.result.append("×")
            }
        }

        binding.plus.setOnClickListener {
            if (operators.none { it in binding.result.text } && !binding.result.text.contains("^[a-zA-Z]+\$".toRegex())) {
                binding.result.append("+")
            }
        }

        binding.subtract.setOnClickListener {
            if (operators.none { it in binding.result.text } && !binding.result.text.contains("^[a-zA-Z]+\$".toRegex())) {
                binding.result.append("–")
            }
        }

        binding.divide.setOnClickListener {
            if (operators.none { it in binding.result.text } && !binding.result.text.contains("^[a-zA-Z]+\$".toRegex())) {
                binding.result.append("÷")
            }
        }

        binding.clear.setOnClickListener {
            binding.result.text = "0"
        }


        binding.power.setOnClickListener {
            if (operators.none { it in binding.result.text } && !binding.result.text.contains("^[a-zA-Z]+\$".toRegex())) {
                binding.result.append("^")
            }
        }

        binding.decimal.setOnClickListener {
            var equation = binding.result.text.toString()
            val n = equation.split("×", "÷", "+", "–", "^")

            if (!binding.result.text.contains("^[a-zA-Z]+\$".toRegex()) && !binding.result.text.contains("[×÷+–^]$".toRegex())) {
                if (n.size == 2 && !n[1].contains(".")) {
                    val rem = n[1]
                    equation = equation.removeSuffix(rem)
                    val full = "$equation$rem."
                    binding.result.text = full
                } else if (!n[0].contains(".")){
                    binding.result.append(".")
                }
            }
        }

        binding.negative.setOnClickListener {
            var equation = binding.result.text.toString()
            val n = equation.split("×", "÷", "+", "–", "^")

            if (!binding.result.text.contains("^[a-zA-Z]+\$".toRegex()) && !binding.result.text.contains("[×÷+–^]$".toRegex())) {
                if (n.size != 1) {
                    val rem = n[1]
                    equation = equation.removeSuffix(rem)
                    val full =
                        equation + ((rem.toBigDecimal()) * "-1".toBigDecimal()).toEngineeringString()
                    binding.result.text = full
                } else {
                    binding.result.text =
                        (n[0].toBigDecimal() * BigDecimal(-1)).toEngineeringString()
                }
            }
        }

        binding.equal.setOnClickListener {
            val equation = binding.result.text.toString()
            var result = 0.0.toBigDecimal()
            var divideZero = false

            if (operators.any { it in equation } && !equation.contains("[×÷+–^]$".toRegex())) {
                val n = equation.split("×", "÷", "+", "–", "^")

                if (equation.contains("×")) {
                    result = n[0].toBigDecimal() * n[1].toBigDecimal()
                } else if (equation.contains("+")) {
                    result = n[0].toBigDecimal() + n[1].toBigDecimal()
                } else if (equation.contains("–")) {
                    result = n[0].toBigDecimal() - n[1].toBigDecimal()
                } else if (equation.contains("÷")) {
                    try {
                        result = (n[0].toDouble() / n[1].toDouble()).toBigDecimal()
                    } catch (e: ArithmeticException) {
                        divideZero = true
                    }
                } else if (equation.contains("^")) {
                    result = (n[0].toDouble().pow(n[1].toDouble())).toBigDecimal()
                }

                if (divideZero) {
                    binding.result.text = getString(R.string.nan)
                } else {
                    binding.result.text = result.toEngineeringString()
                }
            }


        }
    }
}