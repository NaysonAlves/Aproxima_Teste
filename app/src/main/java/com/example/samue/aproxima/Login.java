package com.example.samue.aproxima;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends AppCompatActivity {

    private EditText editLoginEmail, editlLoginSenha;
    private Button btnLoginLogar, btnLoginNovo;
    //private LoginButton btnLoginFacebook;
    private CallbackManager mCallbackManager;

    private FirebaseAuth auth;
    private FirebaseUser user;
    GoogleSignInClient mGoogleSignInClient;
    SignInButton signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inicializaComponentes();
        eventoClicks();
        inicializarFirebaseCallback();
        //clickButton();
        // Configure Google Sign In

        auth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        //-----------
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        //https://www.youtube.com/watch?v=SfV6sumayW0
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            user = auth.getCurrentUser();
                            Intent i = new Intent(Login.this, InformacoesUsuario.class);
                            startActivity(i);
                            finish();
                            alert("Usuário logado com sucesso");

                        } else {
                            // If sign in fails, display a message to the user.
                            alert("Usuário não conseguiu logar");
                        }

                        // ...
                    }
                });
    }
    //--------

//    private void clickButton() {
//        btnLoginFacebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                firebaseLogin(loginResult.getAccessToken());
//            }
//
//            @Override
//            public void onCancel() {
//                alert("Operação cancelada");
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                alert("Erro no login com Facebook");
//            }
//        });
//    }

//    private void firebaseLogin(AccessToken accessToken) {
//        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
//
//        auth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()){
//                            Intent i = new Intent(Login.this, MainActivity.class);
//                            startActivity(i);
//                        }else{
//                            alert("Erro de autenticação com Firebase");
//                        }
//                    }
//                });
//    }

    private void inicializarFirebaseCallback() {
        auth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
    }

    private void eventoClicks() {
        btnLoginNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Cadastro.class);
                startActivity(i);
            }
        });
        btnLoginLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editLoginEmail.getText().toString().trim();
                String senha = editlLoginSenha.getText().toString().trim();
                login(email,senha);
            }
        });
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);
            }
        });
    }

    private void login(String email, String senha) {
        auth.signInWithEmailAndPassword(email,senha)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent i = new Intent(Login.this, InformacoesUsuario.class);
                            startActivity(i);
                        }else{
                            alert("E-mail ou senha errados");
                        }
                    }
                });
    }

    private void alert(String s) {
        Toast.makeText(Login.this,s,Toast.LENGTH_SHORT).show();
    }

    private void inicializaComponentes() {
        editLoginEmail = (EditText) findViewById(R.id.editLoginEmail);
        editlLoginSenha = (EditText) findViewById(R.id.editlLoginSenha);
        btnLoginLogar = (Button) findViewById(R.id.btnLoginLogar);
        btnLoginNovo = (Button) findViewById(R.id.btnLoginNovo);
        //btnLoginFacebook = (LoginButton) findViewById(R.id.btnLoginFacebook);
        //btnLoginFacebook.setReadPermissions("email","public_profile");
        signInButton = (SignInButton) findViewById(R.id.btnLoginGoogle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }

        mCallbackManager.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
        user = Conexao.getFirebaseUser();
        //FirebaseUser currentUser = auth.getCurrentUser();
    }
}

