package com.example.billbook

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.rpc.context.AttributeContext
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [home.newInstance] factory method to
 * create an instance of this fragment.
 */
class home : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private  lateinit var Imageuri:Uri

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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = Firebase.firestore
        val name = FirebaseAuth.getInstance().currentUser!!.uid
        var a = ""
        var b = ""
        db.collection("data").document(name).collection("user").document("name").get().addOnSuccessListener { doc ->
            a = doc.data?.get("email").toString()
            b  = doc.data?.get("name").toString()

        }
        val select:Button = view.findViewById(R.id.select)
        val image:ImageView = view.findViewById(R.id.image)
        val dat =view.findViewById<TextView>(R.id.date)
        val upload = view.findViewById<Button>(R.id.upload)
        val amont = view.findViewById<TextView>(R.id.amount)
        val draw = view.findViewById<DrawerLayout>(R.id.draw)
        val nav = findNavController()
        val navview = view.findViewById<NavigationView>(R.id.navview)
        val toolbar:Toolbar = view.findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            val gmail = view.findViewById<TextView>(R.id.gmail)
            val Name = view.findViewById<TextView>(R.id.Name)
            gmail.text = a
            Name.text = b
            draw.openDrawer(navview)
        }
        navview.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.bills -> {
                    nav.navigate(R.id.action_home2_to_bills)
                    draw.close()
                }
                R.id.signout -> {
                    Firebase.auth.signOut()
                    nav.navigate(R.id.action_home2_to_login)
                }
            }
            return@setNavigationItemSelectedListener true
        }
        val progressDialog =  ProgressDialog(context)
        progressDialog.setMessage(" Uploading image")
        progressDialog.setCancelable(false)
        select.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT

            startActivityForResult(intent,100)
        }
        upload.setOnClickListener {
            if (dat.text.toString().isNotEmpty() and amont.text.toString().isNotEmpty() and (image.drawable != null)){
                progressDialog.show()
                val date = dat.text
                val amount = amont.text
                val data = hashMapOf(
                    "date" to "$date",
                    "amount" to "$amount"
                )
                db.collection("data").document("$name").collection("date").document("$date").set(data)
                val storage = FirebaseStorage.getInstance().getReference("image/${name}/${date}")
                storage.putFile(Imageuri).addOnCompleteListener {
                    image.setImageURI(null)
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                    if (progressDialog.isShowing) progressDialog.dismiss()
                }.addOnFailureListener {
                    if (progressDialog.isShowing) progressDialog.dismiss()
                    Toast.makeText(context, "Reupload", Toast.LENGTH_SHORT).show()

                }


            }
            else{
                Toast.makeText(context,"please select an image and enter date and amount",Toast.LENGTH_SHORT).show()
            }
        }


    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == AppCompatActivity.RESULT_OK) {
            Imageuri = data?.data!!
            view?.findViewById<ImageView>(R.id.image)?.setImageURI(Imageuri)


        }
    }

//    override fun onStop() {
//        super.onStop()
////         val nav =view?.findViewById<NavigationView>(R.id.navview)
//        val dra = view?.findViewById<DrawerLayout>(R.id.draw)
//        dra?.closeDrawers()
//    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}