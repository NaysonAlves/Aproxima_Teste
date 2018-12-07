package com.example.samue.aproxima;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.samue.aproxima.modelo.Pessoa;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    EditText editNome, editEmail;
    ListView listV_dados;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private List<Pessoa> pessoaList = new ArrayList<Pessoa>();
    private ArrayAdapter<Pessoa> pessoaArrayAdapter;

    Pessoa pessoaSelecionada;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicializaComponentes();
        inicializarFirebase();
        eventoDatabse();
        itemSelecionado();


    }

    private void itemSelecionado() {
       listV_dados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               pessoaSelecionada = (Pessoa)parent.getItemAtPosition(position);
               editNome.setText(pessoaSelecionada.getNome());
               //editEmail.setText(pessoaSelecionada.getEmail());
           }
       });
    }

    private void eventoDatabse() {
        databaseReference.child("Pessoa").addValueEventListener(new ValueEventListener() {
            //Muda todos os valores que estão na ramificação Pessoa
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pessoaList.clear();
                for(DataSnapshot objSnapshot:dataSnapshot.getChildren()){
                    Pessoa p = objSnapshot.getValue(Pessoa.class);
                    pessoaList.add(p);
                }
                pessoaArrayAdapter = new ArrayAdapter<Pessoa>(MainActivity.this,
                        android.R.layout.simple_list_item_1,pessoaList);
                listV_dados.setAdapter(pessoaArrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(MainActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }

    private void inicializaComponentes() {
        editEmail = (EditText)findViewById(R.id.editEmail);
        editNome = (EditText)findViewById(R.id.editNome);
        listV_dados = (ListView)findViewById(R.id.listV_dados);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*Grava os valores no Firebase, criando a ramificações
    * Sendo que a ramificação pai e Pessoa, e o "Chave Principal" é UID*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_novo){
            Pessoa p = new Pessoa();
            p.setUid(UUID.randomUUID().toString());
            p.setNome(editNome.getText().toString());
            //p.setEmail(editEmail.getText().toString());
            databaseReference.child("Pessoa").child(p.getUid()).setValue(p);
            limparCampos();
        } else if(id == R.id.menu_atualiza){
            Pessoa p = new Pessoa();
            p.setUid(pessoaSelecionada.getUid());
            p.setNome(editNome.getText().toString().trim());
           // p.setEmail(editEmail.getText().toString().trim());
            databaseReference.child("Pessoa").child(p.getUid()).setValue(p);
            limparCampos();
        }else if(id == R.id.menu_deleta){
            Pessoa p = new Pessoa();
            p.setUid(pessoaSelecionada.getUid());
            databaseReference.child("Pessoa").child(p.getUid()).removeValue();
            limparCampos();
        }
        return true;
    }

    private void limparCampos() {
        editNome.setText("");
        editEmail.setText("");
    }
}
