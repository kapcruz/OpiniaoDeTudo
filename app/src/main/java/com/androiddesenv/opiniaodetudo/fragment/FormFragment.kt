package com.androiddesenv.opiniaodetudo.fragment

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.androiddesenv.opiniaodetudo.ListActivity
import com.androiddesenv.opiniaodetudo.R
import com.androiddesenv.opiniaodetudo.model.Review
import com.androiddesenv.opiniaodetudo.model.repository.ReviewRepository


class FormFragment : Fragment() {

    private lateinit var mainView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mainView = inflater.inflate(R.layout.new_review_form_layout, null)

        val buttonSave = mainView.findViewById<Button>(R.id.button_save)
        val textViewName = mainView.findViewById<TextView>(R.id.input_nome)
        val textViewReview = mainView.findViewById<TextView>(R.id.input_review)


        val reviewToEdit = (activity!!.intent?.getSerializableExtra("item") as Review?)?.also { review ->
            textViewName.text = review.name
            textViewReview.text = review.review
        }

        buttonSave.setOnClickListener {
            val name = textViewName.text
            val review = textViewReview.text
            if(name.toString().isEmpty() || name.toString() == null){
                Toast.makeText(activity!!.applicationContext, "Preencha o campo Nome do produto/filme/coisa!!!", Toast.LENGTH_LONG).show()
            }else{
                object: AsyncTask<Void, Void, Unit>() {
                    override fun doInBackground(vararg params: Void?) {
                        val repository = ReviewRepository(activity!!.applicationContext)
                        if(reviewToEdit == null){
                            repository.save(name.toString().trim(), review.toString().trim())
                            textViewName.setText("")
                            textViewReview.setText("")
                            startActivity(Intent(activity!!, ListActivity::class.java))
                        }else{
                            repository.update(reviewToEdit.id, name.toString(), review.toString())
                            activity!!.finish()
                        }
                    }
                }.execute()
            }
        }

        return mainView
    }
}