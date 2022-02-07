package com.example.billbook

import android.graphics.drawable.Drawable
import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.temporal.TemporalAmount

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [bills.newInstance] factory method to
 * create an instance of this fragment.
 */
class bills : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    data class data(
        val date: String? = "",
        val amount: String? = "",
    )

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val date: TextView = view.findViewById(R.id.date)
        val amount: TextView = view.findViewById(R.id.amount)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bills, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navcon = findNavController()
        val back:ImageView=view.findViewById(R.id.back1)
        back.setOnClickListener {
            navcon.popBackStack()
        }
        var action:NavDirections
        var date1:String
        val db = Firebase.firestore
         val id = FirebaseAuth.getInstance().currentUser?.uid
        val qu = db.collection("data").document("$id").collection("date")
        val g = FirestoreRecyclerOptions.Builder<data>().setQuery(qu, data::class.java)
            .setLifecycleOwner(this).build()
        val adapt = object : FirestoreRecyclerAdapter<data, ViewHolder>(g) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val layout =
                    LayoutInflater.from(parent.context).inflate(R.layout.card, parent, false)
                return ViewHolder(layout)
            }
            override fun onBindViewHolder(
                holder: ViewHolder,
                position: Int,
                model: data
            ) {
                holder.date.text = model.date
                holder.amount.text = model.amount
                holder.itemView.setOnClickListener {
                    date1= model.date.toString()
                    action = billsDirections.actionBillsToBill1(date1)
                    navcon.navigate(action)
                }

            }

        }
        val recycle = view.findViewById<RecyclerView>(R.id.recycle)
        recycle.adapter = adapt
        recycle.layoutManager= LinearLayoutManager(context)


    }


    }



