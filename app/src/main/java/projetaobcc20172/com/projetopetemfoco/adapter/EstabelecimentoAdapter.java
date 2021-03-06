package projetaobcc20172.com.projetopetemfoco.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import projetaobcc20172.com.projetopetemfoco.R;
import projetaobcc20172.com.projetopetemfoco.model.Fornecedor;


//Classe que monta uma View para exibir os pets cadastrados do usuário
public class EstabelecimentoAdapter extends ArrayAdapter<Fornecedor> {

    private ArrayList<Fornecedor> mForncedores;
    private Context mContext;
    private Map<String, Integer> mImagens = new HashMap<String, Integer>();

    public EstabelecimentoAdapter(Context c, ArrayList<Fornecedor> objects) {
        super(c, 0, objects);
        this.mContext = c;
        this.mForncedores = objects;
        mImagens.put("Autônomo",R.drawable.ic_action_autonomo);
        mImagens.put("Estabelecimento",R.drawable.ic_action_estabelecimentos);
    }

    @NonNull
    @Override
    public View getView(final int position, final View convertView, @NonNull final ViewGroup parent) {

        View view = null;
        // Verifica se a lista está preenchida
        if (mForncedores != null) {

            // inicializar objeto para montagem da view
            final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // Monta view a partir do xml
            assert inflater != null;
            view = inflater.inflate(R.layout.lista_estabelecimentos_busca, parent, false);

            // recupera elemento para exibição
            ImageView img = view.findViewById(R.id.ivFornecedorImg);
            TextView nome = view.findViewById(R.id.tvNomeForn);
            TextView bairro = view.findViewById(R.id.tvBairroForn);
            TextView cidade = view.findViewById(R.id.tvCidadeForn);
            TextView uf = view.findViewById(R.id.tvUfForn);
            TextView nota = view.findViewById(R.id.tvNota);
            TextView distancia = view.findViewById(R.id.tvTextDist);

            final Fornecedor fornecedor = mForncedores.get(position);
            img.setImageResource(mImagens.get(fornecedor.getTipo()));
            nome.setText(fornecedor.getNome());
            bairro.setText(fornecedor.getEndereco().getBairro());
            cidade.setText(fornecedor.getEndereco().getLocalidade());
            uf.setText(" - " + fornecedor.getEndereco().getUf());
            distancia.setText("Distância: "+fornecedor.getDistancia()+" km");
            nota.setText(String.format("%.2f", fornecedor.getNota()));
        }
        return view;
    }
}
