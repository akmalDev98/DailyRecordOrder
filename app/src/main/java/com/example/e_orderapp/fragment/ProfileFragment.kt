package com.example.e_orderapp.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.e_orderapp.R
import com.example.e_orderapp.`interface`.Communicator
import com.example.e_orderapp.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_profile.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val mData = FirebaseDatabase.getInstance().reference.child("User")
    private lateinit var communicator : Communicator
    private lateinit var bundle: Bundle
    private lateinit var currentUser : String
//
    override fun onAttach(context: Context) {
        super.onAttach(context)
        communicator = context as Communicator

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

        val fragmentView =  inflater.inflate(R.layout.fragment_profile, container, false)
        val args = arguments
        if (args != null) {
            currentUser  = args.getString("userID")!!
        }


        mData.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val currProfile = User()
                for (i in snapshot.children){
                   if(i.child("userId").getValue().toString() == currentUser){
                       currProfile.userEmail = i.child("userEmail").getValue().toString()
                       currProfile.userPass = i.child("userPass").getValue().toString()
                       currProfile.userName = i.child("userName").getValue().toString()
                       currProfile.businessName = i.child("businessName").getValue().toString()
                   }
               }
                fragmentView.profile_email_tv.text = currProfile.userEmail
                fragmentView.profile_password_tv.text = currProfile.userPass
                fragmentView.profile_username_tv.text = currProfile.userName
                fragmentView.profile_businessname_tv.text = currProfile.businessName
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })



        fragmentView.profile_okay_button.setOnClickListener {
            fragmentManager!!.popBackStack()
        }
        return fragmentView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}