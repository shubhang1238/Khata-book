package com.example.billbook

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [login.newInstance] factory method to
 * create an instance of this fragment.
 */
class login : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var auth:FirebaseAuth

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
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val auth=Firebase.auth
        val navcon= findNavController()
        val register:TextView= view.findViewById(R.id.register)
        register.setOnClickListener{
            navcon.navigate(R.id.action_login_to_register)
        }
        val logi = view.findViewById<TextInputEditText>(R.id.email1)
        val passwor = view.findViewById<TextInputEditText>(R.id.password)
        val button =view.findViewById<Button>(R.id.login)

        button.setOnClickListener {
            val login = logi.text.toString().trim()
            val password = passwor.text.toString().trim()
            if ((login.isNotEmpty() and password.isNotEmpty()) and Patterns.EMAIL_ADDRESS.matcher(login).matches()) {
                auth.signInWithEmailAndPassword(login,password)
                    .addOnCompleteListener { task ->
                        val user: FirebaseUser? = Firebase.auth.currentUser
                        if (task.isSuccessful) {
                            if (user!!.isEmailVerified) {
                                Log.d("TAG", "user signed in With Email:success")
                                navcon.navigate(R.id.action_login_to_home2)
                            } else {
                                Toast.makeText(
                                    context, "verify your email to use app",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Firebase.auth.signOut()
                            }
                        }
                    }
            }else{
                snackbar("please check your email and password")
            }

        }
}

    override fun onStart() {
        super.onStart()
        val navcon= findNavController()
        auth = Firebase.auth
        val currentuser=auth.currentUser
        if(currentuser!=null){
            navcon.navigate(R.id.action_login_to_home2)
        }
    }
    fun snackbar(string:CharSequence) {
        Snackbar.make(requireContext(),requireView(),string, Snackbar.LENGTH_SHORT).show()
    }
}