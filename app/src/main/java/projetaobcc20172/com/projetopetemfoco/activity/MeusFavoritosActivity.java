package projetaobcc20172.com.projetopetemfoco.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import projetaobcc20172.com.projetopetemfoco.R;
import projetaobcc20172.com.projetopetemfoco.adapter.FavoritoAdapter;
import projetaobcc20172.com.projetopetemfoco.config.ConfiguracaoFirebase;
import projetaobcc20172.com.projetopetemfoco.model.Favorito;

public class MeusFavoritosActivity extends Fragment {

    private ArrayList<Favorito> mFavoritos;
    private ArrayAdapter<Favorito> mAdapter;
    private ListView mListView;
    private String mIdUsuarioLogado;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.activity_meus_favoritos, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Meus Favoritos");

        //Recuperar id do usuário logado
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mIdUsuarioLogado = preferences.getString("id", "");

        mListView = getView().findViewById(R.id.lv_meus_favoritos);

        // Monta listview e mAdapter
        mFavoritos = new ArrayList<>();
        mAdapter = new FavoritoAdapter(getActivity(), mFavoritos);
        mListView.setAdapter(mAdapter);

        carregarFavoritos();

    }

    // Recuperar favoritos do Firebase
    private void carregarFavoritos(){

        Query query = ConfiguracaoFirebase.getFirebase().child("favoritos").orderByChild("idUsuario").equalTo(mIdUsuarioLogado);

        ValueEventListener mValueEventListenerFavoritos;
        mValueEventListenerFavoritos = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mFavoritos.clear();

                // Recupera favoritos
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Favorito favorito = dados.getValue(Favorito.class);
                    mFavoritos.add(favorito);

                }
                //Notificar o adaptador que exibe a lista de favoritos se houver alteração no banco
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Erro na leitura do banco de dados", Toast.LENGTH_SHORT).show();
            }
        };
        query.addValueEventListener(mValueEventListenerFavoritos);
    }
}






