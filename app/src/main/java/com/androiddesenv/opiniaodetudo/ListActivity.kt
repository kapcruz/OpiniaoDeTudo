package com.androiddesenv.opiniaodetudo

import android.content.Context
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.androiddesenv.opiniaodetudo.model.Review
import com.androiddesenv.opiniaodetudo.model.repository.ReviewRepository

class ListActivity : AppCompatActivity() {

    private lateinit var reviews : MutableList<Review>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_review_layout)

        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val listView = findViewById<ListView>(R.id.list_recyclerview)
        initList(listView)
        configureOnLongClick(listView)
    }

    override fun onRestart() {
        super.onRestart()
        object : AsyncTask<Unit, Void, Unit>() {
            override fun doInBackground(vararg params: Unit?) {
                this@ListActivity.reviews = ReviewRepository(this@ListActivity.applicationContext).listAll().toMutableList()
            }
            override fun onPostExecute(result: Unit?) {
                val listView = findViewById<ListView>(R.id.list_recyclerview)
                val adapter = listView.adapter as ArrayAdapter<Review>
                adapter.notifyDataSetChanged()
            }
        }.execute()
    }

    private fun initList(listView: ListView){
        object : AsyncTask<Void, Void, ArrayAdapter<Review>>(){
            override fun doInBackground(vararg params: Void?): ArrayAdapter<Review> {
                val repo = ReviewRepository(this@ListActivity.applicationContext)
                reviews = repo.listAll().toMutableList()

                val adapter = object : ArrayAdapter<Review>(this@ListActivity, -1, reviews ){
                    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                        val itemView = layoutInflater.inflate(R.layout.review_list_item_layout, null)
                        val item = reviews[position]

                        val textViewName = itemView.findViewById<TextView>(R.id.item_name)
                        val textViewReview = itemView.findViewById<TextView>(R.id.item_review)

                        textViewName.text = item.name
                        textViewReview.text = item.review

                        return itemView
                    }
                }
                return adapter
            }

            override fun onPostExecute(adapter: ArrayAdapter<Review>) {
                listView.adapter = adapter
            }

        }.execute()
    }

    private fun configureOnLongClick(listView: ListView?) {
        listView?.setOnItemLongClickListener { parent, view, position, id ->
            val popupMenu = PopupMenu(this@ListActivity, view)
            popupMenu.inflate(R.menu.list_review_item_menu)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.item_list_delete -> this@ListActivity.delete(reviews[position])
                    R.id.item_list_edit -> this@ListActivity.openItemForEdition(reviews[position])
                }
                true
            }
            popupMenu.show()
            true
        }
    }

    private fun delete(item: Review){
        object: AsyncTask<Unit, Void, Unit>(){
            override fun doInBackground(vararg params: Unit?) {
                ReviewRepository(this@ListActivity.applicationContext).delete(item)
                reviews.remove(item)
            }
            override fun onPostExecute(result: Unit?) {
                val listView = findViewById<ListView>(R.id.list_recyclerview)
                val adapter = listView.adapter as ArrayAdapter<Review>
                adapter.notifyDataSetChanged()
            }
        }.execute()

    }

    private fun openItemForEdition(item: Review) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("item", item)
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

}