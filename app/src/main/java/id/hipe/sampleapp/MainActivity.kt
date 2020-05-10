package id.hipe.sampleapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import id.hipe.customkeyboard.R
import java.io.File
import java.io.FileInputStream
import java.io.ObjectInputStream
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var tvKeyboard: TextView
    var count_of_words:HashMap<String, Int> = HashMap()
    fun readmap() {
        try
        {
            val fileInputStream = FileInputStream(File(Environment.getExternalStorageDirectory(), "Word_count.txt"))
            val objectInputStream = ObjectInputStream(fileInputStream)
            val myNewlyReadInMap = objectInputStream.readObject() as HashMap<String,Int>
            objectInputStream.close()

            count_of_words = myNewlyReadInMap
//            for (name in myNewlyReadInMap.keySet())
//            {
//                val key = name.toString()
//                val value = myNewlyReadInMap.get(name).toString()
//                //System.out.println(key + " " + value);
//                Log.d("MAPMAPread", "HashMap " + key + " " + value)
//            }
            for ((key, value) in count_of_words) {
                Log.d("MAPMAP","$key = $value")
            }
            Log.d("MAPMAP", "Map read from storage")
        }
        catch (e:Exception) {
            Log.d("MAPMAP", "Error occured while reading " + e.toString())
            e.printStackTrace()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvKeyboard = findViewById(R.id.tvKeyboard)

        startActivityForResult(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS), 99)

        if (isInputEnabled()) {
            (getSystemService(
                Context.INPUT_METHOD_SERVICE) as InputMethodManager).showInputMethodPicker()
        } else {
            Toast.makeText(this@MainActivity, "Please enable keyboard first", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 99)
        {
            readmap()
            val builder=StringBuilder()
            for ((key, value) in count_of_words)
            {
                builder.append("$key = $value\n ")
                Log.d("MAPMAPdisp","$key = $value")
            }
            tvKeyboard.text = "Hipe keyboard now enabled"
            //tvKeyboard.text = "Hipe keyboard currently disabled"
            tvKeyboard.text=builder
        }
    }

    private fun isInputEnabled(): Boolean {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val mInputMethodProperties = imm.enabledInputMethodList

        val N = mInputMethodProperties.size
        var isInputEnabled = false

        for (i in 0 until N) {

            val imi = mInputMethodProperties[i]
            Log.d("INPUT ID", imi.id.toString())
            if (imi.id.contains(packageName ?: "")) {
                isInputEnabled = true
            }
        }

        return isInputEnabled
    }

}
