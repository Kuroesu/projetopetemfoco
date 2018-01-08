package projetaobcc20172.com.projetopetemfoco.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import projetaobcc20172.com.projetopetemfoco.R;
import projetaobcc20172.com.projetopetemfoco.adapter.PetAdapter;
import projetaobcc20172.com.projetopetemfoco.config.ConfiguracaoFirebase;
import projetaobcc20172.com.projetopetemfoco.model.Pet;

public class PetsActivity extends AppCompatActivity {

    private ArrayList<Pet> mPets;
    private ArrayAdapter<Pet> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pets);

        //Recuperar id do usuário logado
        String idUsuarioLogado;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        idUsuarioLogado = preferences.getString("id", "");

        ImageButton mCadastrarPet; //Botão de cadastrar o pet

        Toolbar toolbar;
        toolbar = findViewById(R.id.tb_main);

        // Configura toolbar
        toolbar.setTitle(R.string.pets);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left_white);
        setSupportActionBar(toolbar);

        mCadastrarPet = findViewById(R.id.btnCadastrarPet);
        ListView mListView;
        mListView = findViewById(R.id.lv_pets);

        // Monta listview e mAdapter
        mPets = new ArrayList<>();
        mAdapter = new PetAdapter(PetsActivity.this, mPets);
        mListView.setAdapter(mAdapter);

        // Recuperar pets do Firebase
        DatabaseReference mFirebase;
        mFirebase = ConfiguracaoFirebase.getFirebase().child("usuarios").child(idUsuarioLogado).child("pets");

        ValueEventListener mValueEventListenerPet;
        mValueEventListenerPet = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPets.clear();

                // Recupera pets
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Pet pet = dados.getValue(Pet.class);
                    mPets.add(pet);
                }
                //Notificar o adaptador que exibe a lista de pets se houver alteração no banco
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(PetsActivity.this, "Erro na leitura do banco de dados", Toast.LENGTH_SHORT).show();
            }
        };

        //Ação do botão de cadastrar o pet, que abre a tela para o seu cadastro
        mCadastrarPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PetsActivity.this, CadastroPetActivity.class);
                startActivity(intent);
            }
        });
        mFirebase.addValueEventListener(mValueEventListenerPet);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
