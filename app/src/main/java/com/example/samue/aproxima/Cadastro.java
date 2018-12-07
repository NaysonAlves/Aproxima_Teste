package com.example.samue.aproxima;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Cadastro extends AppCompatActivity {

    private EditText editCadastroEmail, editCadastroSenha;
    private Button btnCadastroRegistrar, btnCadastroVoltar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        inicializaComponentes();
        eventoClicks();
    }

    private void eventoClicks() {
        btnCadastroVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnCadastroRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editCadastroEmail.getText().toString().trim();
                String senha = editCadastroSenha.getText().toString().trim();
                criaUser(email, senha);
            }
        });
    }

    private void criaUser(String email, String senha) {
        auth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(Cadastro.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            alert("Usuário cadastrado com sucesso");
                            Intent i = new Intent(Cadastro.this,Login.class);
                            startActivity(i);
                            finish();
                        }else{
                            alert("Erro de Cadsatro");
                        }
                    }
                });
    }

    private void alert(String msg){
        Toast.makeText(Cadastro.this, msg,Toast.LENGTH_SHORT).show();
    }

    private void inicializaComponentes() {
        editCadastroEmail = (EditText) findViewById(R.id.editCadastroEmail);
        editCadastroSenha = (EditText) findViewById(R.id.editCadastroSenha);
        btnCadastroRegistrar = (Button) findViewById(R.id.btnCadastroRegistrar);
        btnCadastroVoltar = (Button) findViewById(R.id.btnCadastroVoltar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
    }
}
