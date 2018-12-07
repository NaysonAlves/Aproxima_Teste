package com.example.samue.aproxima;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.samue.aproxima.modelo.Pessoa;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InformacoesUsuario extends AppCompatActivity {

    EditText editNome, editSobrenome, editDataNascimento, editApelido;
    RadioButton rdbOpcao;
    RadioGroup rdgSexo;
    Button btnGravar;
    ListView listV_dados;
    String sexoUsuario;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacoes_usuario);
        inicializaComponentes();
        inicializarFirebase();
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(InformacoesUsuario.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        //firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }

    private void inicializaComponentes() {
        editNome = (EditText)findViewById(R.id.editInfUsuarioNome);
        editSobrenome = (EditText) findViewById(R.id.editInfUsuarioSobrenome);
        editDataNascimento = (EditText)findViewById(R.id.editInfUsuarioDataNasc);
        editApelido = (EditText)findViewById(R.id.editInfUsuarioApelido);
        rdgSexo = (RadioGroup)findViewById(R.id.radioGroupSexo);
        btnGravar = (Button)findViewById(R.id.btnInfUsuarioGravar);

        rdgSexo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rdbOpcao = rdgSexo.findViewById(checkedId);
                switch (checkedId){
                    case R.id.radioBtnMasculino:
                        sexoUsuario = rdbOpcao.getText().toString();
                        break;
                    case R.id.radioBtnFeminino:
                        sexoUsuario = rdbOpcao.getText().toString();
                        break;
                    case R.id.radioBtnOutro:
                        sexoUsuario = rdbOpcao.getText().toString();
                        break;
                    default:
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_novo){
            Pessoa pessoa = new Pessoa();
            pessoa.setUid(UUID.randomUUID().toString());
            pessoa.setNome(editNome.getText().toString());
            pessoa.setSobrenome(editSobrenome.getText().toString());
            pessoa.setDataNascimento(editDataNascimento.getText().toString());
            pessoa.setSexo(sexoUsuario);
            pessoa.setApelido(editApelido.getText().toString());
            databaseReference.child("Pessoa").child(pessoa.getUid()).setValue(pessoa);
            limparCampos();
        }
        return true;
    }

    private void limparCampos() {
        editNome.setText("");
        editSobrenome.setText("");
        editDataNascimento.setText("");
        editApelido.setText("");
    }
}

