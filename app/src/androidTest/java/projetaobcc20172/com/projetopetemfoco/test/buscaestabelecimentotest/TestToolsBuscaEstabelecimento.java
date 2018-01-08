package projetaobcc20172.com.projetopetemfoco.test.buscaestabelecimentotest;

import android.app.Activity;

import projetaobcc20172.com.projetopetemfoco.R;
import projetaobcc20172.com.projetopetemfoco.test.TestTools;

/**
 * Created by raul1 on 06/01/2018.
 */

public class TestToolsBuscaEstabelecimento {

    protected static void digitarBuscar(String estabelecimento){
        TestTools.digitarCampo(R.id.svBusca,estabelecimento);
    }

    protected static void realizarPesquisa(){
        TestTools.pressionarBuscarTeclado();
    }

    protected static void avaliarListaEstabelecimentos(Activity act, String estabelecimento){
        TestTools.checarListViewComTextView(act,R.id.lvBuscaEsta,R.id.tvNomeForn,estabelecimento,15);
    }

    protected static void checarTamanhoList(int qtEsperada){
        TestTools.checarTamanhoList(TestTools.activityAtual(),R.id.lvBuscaEsta,qtEsperada);
    }
}