package com.androiddesenv.opiniaodetudo

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.room.util.StringUtil
import com.androiddesenv.opiniaodetudo.model.Review
import com.androiddesenv.opiniaodetudo.model.repository.ReviewRepository

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val mainContainer = findViewById<ConstraintLayout>(R.id.main_container)
        val buttonSave = findViewById<Button>(R.id.button_save);
        val textViewName = findViewById<TextView>(R.id.input_nome);
        val textViewReview = findViewById<TextView>(R.id.input_review);

        val reviewToEdit = (intent?.getSerializableExtra("item") as Review?)?.also { review ->
            textViewName.text = review.name
            textViewReview.text = review.review
        }


        mainContainer.setOnTouchListener { v, event ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0 )
        }

        buttonSave.setOnClickListener {
            val name = textViewName.text
            val review = textViewReview.text
            if(name.toString().isEmpty() || name.toString() == null){
                Toast.makeText(this@MainActivity.applicationContext, "Preencha o campo Nome do produto/filme/coisa!!!", Toast.LENGTH_LONG).show()
            }else{
                object: AsyncTask<Void, Void, Unit>() {
                    override fun doInBackground(vararg params: Void?) {
                        val repository = ReviewRepository(this@MainActivity.applicationContext)
                        if(reviewToEdit == null){
                            repository.save(name.toString().trim(), review.toString().trim())
                            textViewName.setText("")
                            textViewReview.setText("")
                            startActivity(Intent(this@MainActivity, ListActivity::class.java))
                        }else{
                            repository.update(reviewToEdit.id, name.toString(), review.toString())
                            finish()
                        }
                    }
                }.execute()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.menu_list_reviews){
            startActivity(Intent(this, ListActivity::class.java))
            return true
        }

        return false
    }
}