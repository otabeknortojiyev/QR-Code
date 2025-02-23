package uz.gita.otabek.hashcode

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editText1 = findViewById<EditText>(R.id.et1)
        val editText2 = findViewById<EditText>(R.id.et2)
        val squaresView = findViewById<SquaresView>(R.id.sv)

        findViewById<TextView>(R.id.generate).setOnClickListener {
            if (editText1.text.toString() != "" && editText2.text.toString() != "") {
                squaresView.matrixTemp = null
                val number1 = editText1.text.toString().toIntOrNull() ?: 1
                val number2 = editText2.text.toString().toIntOrNull() ?: 1

                val result = number1 * number2
                squaresView.heightCount = number1
                squaresView.weightCount = number2
                squaresView.squareCount = result
            } else Toast.makeText(this@MainActivity, "Write height and weight", Toast.LENGTH_SHORT).show()
        }
    }
}