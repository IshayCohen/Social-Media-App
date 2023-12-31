package com.example.socialking


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Explode
import android.view.Window
import android.widget.Toast
import com.example.socialking.databinding.ActivityLoggedInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


lateinit var loggedInBinding: ActivityLoggedInBinding
var discoveredName: String="placeholder"

class LoggedInActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {

        with(window){
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            exitTransition = Explode()
        }

        super.onCreate(savedInstanceState)




        loggedInBinding = ActivityLoggedInBinding.inflate(layoutInflater)
        val view = loggedInBinding.root
        setContentView(view)




        val intent = intent
        val emailReceived= intent.getStringExtra("email").toString()
        //discoveredName="null"
        collectNameByEmail(emailReceived)







        //this following segment is for signing out of existing user
        loggedInBinding.SignOutButtonLoggedInAct.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@LoggedInActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        loggedInBinding.addPostButton.setOnClickListener{

            val intent = Intent(this@LoggedInActivity, AddPostActivity::class.java)
            //collectNameByEmail(emailReceived)
            if(discoveredName.equals("null"))
                discoveredName= intent.getStringExtra("name").toString()


            intent.putExtra("email",emailReceived)
            intent.putExtra("name", discoveredName)
            startActivity(intent)

        }

        loggedInBinding.PostWallButtonLoggedInAct.setOnClickListener{

            val intent = Intent(this@LoggedInActivity,PostWallActivity::class.java)
            intent.putExtra("email",emailReceived)
            intent.putExtra("name", discoveredName)
            startActivity(intent)

        }
        loggedInBinding.chatButton.setOnClickListener{
            val intent = Intent(this@LoggedInActivity,ChatComposeActivity::class.java)


            if(discoveredName.equals("null"))
                discoveredName= intent.getStringExtra("name").toString()


            intent.putExtra("name", discoveredName)
            intent.putExtra("email",emailReceived)
           // intent.putExtra("flag", flag)
            startActivity(intent)
            finish()

        }
        loggedInBinding.searchButtonLoggedIn.setOnClickListener{

            val intent = Intent(this@LoggedInActivity,SearchActivity::class.java)
            startActivity(intent)

        }



    }


    fun collectNameByEmail(email: String){
        val modifiedEmail: String = email.replace(".", "")
        database = FirebaseDatabase.getInstance().getReference("users")
        database.child(modifiedEmail).get().addOnSuccessListener {
            if (it.exists()) {
                discoveredName= it.child("name").value.toString()
            } else {
                //Toast.makeText(this, "User Doesn't Exist", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener{
            Toast.makeText(this, "Failed to read data", Toast.LENGTH_SHORT).show()

        }

    }

}
