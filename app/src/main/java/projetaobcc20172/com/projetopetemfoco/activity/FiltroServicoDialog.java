package projetaobcc20172.com.projetopetemfoco.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import projetaobcc20172.com.projetopetemfoco.R;
import projetaobcc20172.com.projetopetemfoco.adapter.OpPetGridAdapter;
import projetaobcc20172.com.projetopetemfoco.config.ConfiguracaoFirebase;
import projetaobcc20172.com.projetopetemfoco.config.ConfiguracoesBuscaServico;
import projetaobcc20172.com.projetopetemfoco.model.Endereco;
import projetaobcc20172.com.projetopetemfoco.utils.Enumerates;
import projetaobcc20172.com.projetopetemfoco.utils.Utils;

/**
 * Created by raul1 on 05/01/2018.
 * Classe que representa o filtro para a tela de busca por servico
 */

public class FiltroServicoDialog extends Activity implements View.OnClickListener {

    private OpPetGridAdapter mPetAdapter = null;
    private ArrayList<String> mOpcaosSelecionada;
    private HashMap<String,Integer> botoesClicados;//Hash que verifica se o botão está marcado ou não. 1 se estiver e 0 se não estiver
    private String opcaoTodos = "Todos";
    private ArrayList<String> tiposPets;
    private CheckBox cbProximoMinhaRedidencia;
    private CheckBox cbProx;
    private CheckBox cbAvaliacao;
    private CheckBox cbPreco;
    public Toast mToast;
    private int raio;
    private static String idUsuarioLogado;
    private boolean geoCoordControle = false;
    private ProgressBar mProgresso;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro_servicos);
        final GridView gridView = findViewById(R.id.gridOpPet);
        this.configurarSeekBar();
        Button btnSalvarFiltro = findViewById(R.id.btnSalvarFiltro);
        cbProx = findViewById(R.id.cbProx);
        cbProximoMinhaRedidencia = findViewById(R.id.cbProxMinhaResidencia);
        cbAvaliacao = findViewById(R.id.cbAva);
        cbPreco = findViewById(R.id.cbPreco);
        idUsuarioLogado = getPreferences("id", this);

        mProgresso = (ProgressBar) findViewById(R.id.pbProgressoFiltro);
        mProgresso.setVisibility(View.INVISIBLE);

        this.tiposPets = Utils.recuperaArrayR(this,R.array.tiposPetBusca);
        this.carregarFiltro();//Carrega as informações existentes no obj ConfiguracaoBuscaServico
        gridView.setAdapter(mPetAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView opcaoSelecionada = view.findViewById(R.id.tvGridTextPet);
                ImageView im = view.findViewById(R.id.gridImgPet);
                addOpcoes(opcaoSelecionada.getText().toString(),im,gridView);
            }
        });

        //Recuperar id do usuário logado
        idUsuarioLogado = getPreferences("id", this);

        this.cbProx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controleCheckBox(0);
            }
        });

        this.cbAvaliacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controleCheckBox(1);
            }
        });

        this.cbPreco.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                controleCheckBox(2);
            }
        });

        this.cbProximoMinhaRedidencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgresso.setVisibility(View.VISIBLE);
                idUsuarioLogado = getPreferences("id", FiltroServicoDialog.this);
                if(buscarEnderecoCoord(idUsuarioLogado)||geoCoordControle){
                    controleCheckBox(3);
                }else{
                    cbProximoMinhaRedidencia.setChecked(false);
                    mToast = Toast.makeText(FiltroServicoDialog.this, "Você deve antes adicionar um endereço", Toast.LENGTH_SHORT);
                    mToast.show();
                }
            }
        });

        btnSalvarFiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarOpcoes();
                Utils.mostrarMensagemCurta(FiltroServicoDialog.this, getString(R.string.filtro_atualizado_servico));
                finish();
            }
        });


      //  mPetAdapter.notifyDataSetChanged();
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();
        if(ConfiguracoesBuscaServico.getsFiltro().equals(Enumerates.Filtro.DISTANCIA)) {
            controleCheckBox(0);
        }
        else if(ConfiguracoesBuscaServico.getsFiltro().equals(Enumerates.Filtro.AVALICAO)){
            controleCheckBox(1);
        }
        else if(ConfiguracoesBuscaServico.getsFiltro().equals(Enumerates.Filtro.PRECO)){
            controleCheckBox(2);
        }
        else if(ConfiguracoesBuscaServico.getsFiltro().equals(Enumerates.Filtro.PROXIMO_MINHA_RESIDENCIA)){
            controleCheckBox(3);
        }
    }

    private void inicializarOpcoes(){
        mOpcaosSelecionada = ConfiguracoesBuscaServico.getsOpcaosPet();
    }

    //Método responsável por controlar os cliques nos ícones de tipos de pets
    private void addOpcoes(String selecionado,ImageView im,GridView grid){
        if(selecionado.equals(this.opcaoTodos)){
            mOpcaosSelecionada.clear();
            this.desmarcarTodos(grid);
            mOpcaosSelecionada.add(this.opcaoTodos);
            this.marcarImg(this.opcaoTodos,im);
        }
        else if(this.botoesClicados.get(this.opcaoTodos)==1){
            this.botoesClicados.put(this.opcaoTodos,0);
            mOpcaosSelecionada.remove(this.opcaoTodos);
            mOpcaosSelecionada.add(selecionado);
            this.desmarcarTodos(grid);
            this.marcarImg(selecionado,im);
        }
        //Desmarca um item marcado somente se a lista tiver mais de um item marcado
        //garante que sempre existirá pelo menos um item marcado para busca.
        else if(this.botoesClicados.get(selecionado)==1 && mOpcaosSelecionada.size()>1){
            mOpcaosSelecionada.remove(selecionado);
            this.desmarcarImg(selecionado,im);
        }//Se não está na lista add
        else if(this.botoesClicados.get(selecionado)==0){
            mOpcaosSelecionada.add(selecionado);
            this.marcarImg(selecionado,im);
        }

    }
    //coloca o marcado de "certo" na no botao
    private void marcarImg(String selecionado, ImageView im){
        if(selecionado.equals(tiposPets.get(0))){
            im.setImageResource(R.drawable.tipo_pet_todos_check);
        }
        else if(selecionado.equals(tiposPets.get(1))){
            im.setImageResource(R.drawable.tipo_pet_cachorro_check);
        }
        else if(selecionado.equals(tiposPets.get(2))){
            im.setImageResource(R.drawable.tipo_pet_gato_check);
        }
        this.botoesClicados.put(selecionado,1);
    }

    private void desmarcarImg(String selecionado, ImageView im){
        if(selecionado.equals(tiposPets.get(0)) ){
            im.setImageResource(R.drawable.tipo_pet_todos);
        }
        else if(selecionado.equals(tiposPets.get(1))){
            im.setImageResource(R.drawable.tipo_pet_cachorro);
        }
        else if(selecionado.equals(tiposPets.get(2))){
            im.setImageResource(R.drawable.tipo_pet_gato);
        }
        this.botoesClicados.put(selecionado,0);
    }

    private void desmarcarTodos(GridView grid){
        for(int i = 0;i<grid.getCount();i++){
            TextView tv = grid.getChildAt(i).findViewById(R.id.tvGridTextPet);
            ImageView iv = grid.getChildAt(i).findViewById(R.id.gridImgPet);
            this.desmarcarImg(tv.getText().toString(),iv);
            this.botoesClicados.put(tv.getText().toString(),0);
        }
    }

    private void inicializarHashBotoes(){
        this.botoesClicados = new HashMap<String,Integer>();
        for(String pet:this.tiposPets){
            this.botoesClicados.put(pet,0);
        }
        this.botoesClicados.put(this.opcaoTodos,1);
    }

    private void salvarOpcoes(){
        ConfiguracoesBuscaServico.setsOpcaosPet(this.mOpcaosSelecionada);
        if(this.cbAvaliacao.isChecked()){
            ConfiguracoesBuscaServico.setsFiltro(Enumerates.Filtro.AVALICAO);
        }
        else if(this.cbProx.isChecked()){
            ConfiguracoesBuscaServico.setsFiltro(Enumerates.Filtro.DISTANCIA);
        }
        else if(this.cbPreco.isChecked()){
            ConfiguracoesBuscaServico.setsFiltro(Enumerates.Filtro.PRECO);
        }
        else if(this.cbProximoMinhaRedidencia.isChecked()){
            ConfiguracoesBuscaServico.setsFiltro(Enumerates.Filtro.PROXIMO_MINHA_RESIDENCIA);
        }
        ConfiguracoesBuscaServico.getRaio().setRaioAtual((this.raio));
    }

    private void carregarFiltro(){
        this.inicializarOpcoes();
        this.inicializarHashBotoes();
        if(ConfiguracoesBuscaServico.getsEstado().equals(Enumerates.Estado.DEFAULT)) {
            mPetAdapter = new OpPetGridAdapter(this, tiposPets);
        }
        else{
            ArrayList <String> pets = new ArrayList<>();
            for(String pet:this.tiposPets){
                if(ConfiguracoesBuscaServico.getsOpcaosPet().contains(pet)){
                    if(pet.equalsIgnoreCase(this.opcaoTodos)){
                        pets.add(pet);
                    }
                    else{
                        pets.add(pet+"_check");
                    }
                    this.botoesClicados.put(pet,1);
                }
                else{
                    if(pet.equalsIgnoreCase(this.opcaoTodos)){
                        pets.add(pet+"_uncheck");
                        this.botoesClicados.put(pet,0);
                    }
                    else{
                        pets.add(pet);
                    }
                }
            }
            mPetAdapter = new OpPetGridAdapter(this, pets);
        }
    }

    @Override
    public void onClick(View v) {
        //Método com corpo vazio
    }


    private void configurarSeekBar(){
        SeekBar skRaio = findViewById(R.id.sbRaio);
        final TextView tvRaio = findViewById(R.id.tvRaio);
        this.raio = (ConfiguracoesBuscaServico.getRaio().getRaioAtual());
        skRaio.setMax(ConfiguracoesBuscaServico.getRaio().getRange());
        skRaio.setProgress(this.raio);
        tvRaio.setText(this.raio+ConfiguracoesBuscaServico.getRaio().getInicial()+" km");
        skRaio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int i1;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                if(!cbProx.isChecked()){
//                    ConfiguracoesBuscaServico.setsFiltro(Enumerates.Filtro.DISTANCIA);
//                    controleCheckBox(0);
//                }
                raio = (byte) i;
                i1 = i + ConfiguracoesBuscaServico.getRaio().getInicial();
                tvRaio.setText(i1 +" km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Método com corpo vazio
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Método com corpo vazio
            }
        });
    }

    private void controleCheckBox(int i){
        this.cbProx.setChecked(false);
        this.cbAvaliacao.setChecked(false);
        this.cbPreco.setChecked(false);
        this.cbProximoMinhaRedidencia.setChecked(false);
        if(i==0){
            this.cbProx.setChecked(true);
        }
        else if(i==1){
            this.cbAvaliacao.setChecked(true);
        }
        else if(i==2){
            this.cbPreco.setChecked(true);
        }
        else if(i==3){
            this.cbProximoMinhaRedidencia.setChecked(true);
        }
    }


    //Método que chama a activity para exibir informações do estabelecimento
    public boolean buscarEnderecoCoord(final String idUsuariologado) {
        //Buscar servicos do estabelecimento selecionado
        Query query = ConfiguracaoFirebase.getFirebase().child("enderecos").orderByChild("idUsuario").equalTo(idUsuariologado);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Endereco> enderecos = new ArrayList<>();
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Endereco end = dados.getValue(Endereco.class);
                    enderecos.add(end);
                    ConfiguracoesBuscaServico.geoLocalizacaoEndenreco[0] = end.getmLatitude();
                    ConfiguracoesBuscaServico.geoLocalizacaoEndenreco[1] = end.getmLongitude();
                    geoCoordControle = true;
                }
                mProgresso.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Método com corpo vazio
            }
        });
        return geoCoordControle;
    }

    //Método que recupera o id do usuario logado, para localizacao a geolocalização do endereço
    public static String getPreferences(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }
}
