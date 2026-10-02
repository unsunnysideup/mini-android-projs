package ait.hu.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ait.hu.tipcalculator.ui.theme.TipCalculatorTheme
import ait.hu.tipcalculator.ui.theme.Typography
import android.R
import android.R.attr.onClick
import android.R.attr.text
import android.widget.EditText
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.substring
import org.w3c.dom.Text

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TipCalculatorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TipCalculate(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun TipCalculate(modifier: Modifier = Modifier) {

    LazyColumn(
        modifier = modifier
    ) {
        stickyHeader {
            Text(
                text = "Tip Calculator",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xff7e3285))
                    .padding(15.dp)
            )

        }

        item {
            Text(
                text = "Tip Calculator",
                color = Color.Gray,
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 100.dp, bottom = 50.dp)

            )

            Text(
                text = "Enter your bill amount:",
                color = Color.Gray,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp)

            )

            var number by remember { mutableStateOf("") }

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp)) {

                OutlinedTextField(
                    value = number,
                    onValueChange = {number = it},
                    placeholder = { Text(text = "0.00",
                        fontSize = 20.sp) },
                    prefix = {Text(text = "$",
                        color = Color(0xff7e3285))},
                    textStyle = TextStyle(
                        fontSize = 20.sp,
                        color = Color(0xff7e3285)
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xffA71FB5),
                        cursorColor = Color(0xffA71FB5)

                    ),
                    modifier = Modifier
                        .fillMaxWidth()

                )

            }

            Text(
                text = "Select a tip percentage:",
                textAlign = TextAlign.Center,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp)

            )

            val radioOptions = listOf("10%", "15%", "20%")
            val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }

            Column(
                modifier.selectableGroup()) {
                radioOptions.forEach { text ->
                    Row( modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (text == selectedOption),
                            onClick = { onOptionSelected(text) },
                            role = Role.RadioButton
                        )
                        .padding(vertical = 5.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        RadioButton(
                            selected = (text == selectedOption),
                            onClick = null // null recommended for accessibility with screen readers
                        )
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }

            var result by remember { mutableStateOf("Total Payment: $0.00") }

           Row(modifier = Modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {
                Button(
                    onClick = {

                        try {
                            val res = (((number.toDouble() * (1 + (selectedOption.substring(0, 2)
                                .toDouble()) / 100)) * 100).toInt() / 100.0)
                            result = "Total Payment: $$res"
                        } catch (e: NumberFormatException) {
                            result = if (number == "") {
                                "Total Payment: $0.00"

                            } else {
                                "Inappropriate Input"
                            }
                        }


                    },
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xff7e3285)),
                    modifier = Modifier.padding(horizontal = 10.dp)
                ) {
                    Text("CALCULATE", fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = {
                        number = ""
                        result = "Total Payment: $0.00"
                    },
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                ) {
                    Text("CLEAR", fontWeight = FontWeight.SemiBold)
                }
            }

            Text(text = result,
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xff7e3285),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(50.dp))
        }








}
}

