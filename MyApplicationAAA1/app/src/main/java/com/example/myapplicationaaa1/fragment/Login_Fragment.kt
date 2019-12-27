package com.example.myapplicationaaa1.fragment


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplicationaaa1.R
import com.example.myapplicationaaa1.activity.RegisterActivity
import com.example.myapplicationaaa1.model.UserModel
import com.example.myapplicationaaa1.utils.UserDao
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_login.*


class Login_Fragment : Fragment() {

    override fun onResume() {
        super.onResume()
        UpdateUI()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_login, container, false)

        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val progressBar: ProgressBar =progressBar2
//
//        if(progressBar!=null)
        progressBar.visibility = View.GONE
        registerButton.visibility = View.VISIBLE
        LogIn.visibility = View.VISIBLE

        LogIn.setOnClickListener()
        {
            LoginUser()
        }
        registerButton.setOnClickListener {
            if (FirebaseAuth.getInstance().currentUser == null)
                startActivity(Intent(requireContext(), RegisterActivity::class.java))
            UpdateUI()

        }
        google_button.setOnClickListener {
            configureGoogleSignIn()
            signIn()
        }

        UpdateUI()
    }

    private fun UpdateUI() {
        if (FirebaseAuth.getInstance().currentUser == null) {
            registerButton.visibility = View.VISIBLE
            LogIn.visibility = View.VISIBLE
        } else {
            registerButton.visibility = View.GONE
            LogIn.visibility = View.GONE

            //Change fragment
            val newFragment = Profile_Fragment()
            val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainer, newFragment)
            fragmentTransaction?.commit()
        }

    }

    private fun LoginUser() {

        val password = passwordEditText.text.toString()
        val email = emailLoginEditText.text.toString()

        if (email.isEmpty()) {
            Toast.makeText(context, "Please Enter Email", Toast.LENGTH_LONG).show()
            return
        } else if (password.isEmpty()) {
            Toast.makeText(context, "Please Enter Password", Toast.LENGTH_LONG).show()
            return
        } else { //Progress bar
            progressBar.visibility = View.VISIBLE

            //Login aND INIT
            if (FirebaseAuth.getInstance().currentUser == null) {//no hay usera logeado
                //check en la base de datos
                val auth = FirebaseAuth.getInstance()
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        //SHARED_PREFERENCES
                        FirebaseAuth.getInstance().currentUser?.uid?.let { userID ->
                            UserDao().get(userId = userID, successListener = {
                                if (it != null) {
                                    this.getActivity()?.let { it2 ->
                                        it2.getSharedPreferences(
                                            "userProfile",
                                            Context.MODE_PRIVATE
                                        ).edit()
                                            .putString("username", it.userName).apply()
                                        it2.getSharedPreferences(
                                            "userProfile",
                                            Context.MODE_PRIVATE
                                        ).edit()
                                            .putString("password", it.password).apply()
                                        it2.getSharedPreferences(
                                            "userProfile",
                                            Context.MODE_PRIVATE
                                        ).edit()
                                            .putString("email", it.email).apply()
                                        it2.getSharedPreferences(
                                            "userProfile",
                                            Context.MODE_PRIVATE
                                        ).edit()
                                            .putString("image_url", it.url).apply()
                                        progressBar.visibility = View.GONE

                                        UpdateUI()
                                    }
                                }

                            }, failureListener = {

                            })
                        }
                    }.addOnFailureListener {
                        Toast.makeText(context, "Error with sing in", Toast.LENGTH_LONG).show()
                        progressBar.visibility = View.GONE

                    }


            }


        }

    }


    private fun getAllUsersTest() {
        UserDao().getAll(
            successListener = { users ->
                //adapter.users=users

            },
            failureListener = {
                Toast.makeText(
                    context,
                    "Los datos de usuario/contraseñoa son incorrectos",
                    Toast.LENGTH_LONG
                ).show()

                Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        )
    }

    private fun getUser(userId: String) {
        UserDao().get(userId = userId,
            successListener = {

                Toast.makeText(
                    context,
                    "Los datos de usuario/contraseñoa son correctos",
                    Toast.LENGTH_LONG
                ).show()

            },
            failureListener = {
                Toast.makeText(
                    context,
                    "Los datos de usuario/contraseñoa son incorrectos",
                    Toast.LENGTH_LONG
                ).show()


            })
    }


    val RC_SIGN_IN: Int = 1
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions
    //****************************

    private fun configureGoogleSignIn() {
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        activity?.let {
            mGoogleSignInClient = GoogleSignIn.getClient(it, mGoogleSignInOptions)

        }
    }

    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.let { firebaseAuthWithGoogle(it) }
            } catch (e: ApiException) {
                Toast.makeText(context, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        progressBar.visibility = View.VISIBLE

        lateinit var firebaseAuth: FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Google sign Complete :)", Toast.LENGTH_LONG).show()

                val acct = GoogleSignIn.getLastSignedInAccount(activity!!)

                val auth = FirebaseAuth.getInstance()
                val user = auth.currentUser
                if (acct != null) {
                    //Aqui???Register in FiraStore
                    val dataBase = FirebaseFirestore.getInstance();
                    val newUser = UserModel(
                        userID = "",
                        //stroe this into a constant
                        userName = acct.displayName,
                        url = acct.photoUrl.toString(),
                        password = "Sign In With Google",
                        email = acct.email
                    )
                    if (user !== null) {
                        dataBase
                            .collection("Users")
                            .document(user.uid)
                            .set(newUser)
                            .addOnSuccessListener { documentReference ->
                                activity?.let { it1 ->
                                    FirebaseAnalytics.getInstance(it1)
                                        .logEvent("Register_Complete", null)
                                }


                            }.addOnFailureListener { e ->
                                Toast.makeText(
                                    context!!,
                                    "Error, try again later",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                                progressBar.visibility = View.GONE

                            }
                        //SHARED_PREFERENCES
                        FirebaseAuth.getInstance().currentUser?.uid?.let { userID ->
                            UserDao().get(userId = userID, successListener = {
                                if (it != null) {
                                    this.getActivity()?.let { it2 ->
                                        it2.getSharedPreferences(
                                            "userProfile",
                                            Context.MODE_PRIVATE
                                        ).edit()
                                            .putString("username", it.userName).apply()
                                        it2.getSharedPreferences(
                                            "userProfile",
                                            Context.MODE_PRIVATE
                                        ).edit()
                                            .putString("password", it.password).apply()
                                        it2.getSharedPreferences(
                                            "userProfile",
                                            Context.MODE_PRIVATE
                                        ).edit()
                                            .putString("email", it.email).apply()
                                        it2.getSharedPreferences(
                                            "userProfile",
                                            Context.MODE_PRIVATE
                                        ).edit()
                                            .putString("image_url", it.url).apply()
                                        progressBar.visibility = View.GONE

                                        UpdateUI()
                                    }
                                }

                            }, failureListener = {

                            })
                        }
                    }
                }


            } else {
                Toast.makeText(context, "Google sign in failed:(", Toast.LENGTH_LONG).show()
                progressBar.visibility = View.GONE

            }
        }


    }
}
