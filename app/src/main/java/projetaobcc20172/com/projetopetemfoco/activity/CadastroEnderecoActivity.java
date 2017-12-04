package projetaobcc20172.com.projetopetemfoco.activity;
/**
 * Created by Alexsandro on 03/12/17.
 */

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import projetaobcc20172.com.projetopetemfoco.R;
import projetaobcc20172.com.projetopetemfoco.excecoes.CampoEnderecoObrAusenteException;
import projetaobcc20172.com.projetopetemfoco.helper.Base64Custom;
import projetaobcc20172.com.projetopetemfoco.helper.Preferencias;
import projetaobcc20172.com.projetopetemfoco.model.Endereco;

public class CadastroEnderecoActivity extends AppCompatActivity {

    private EditText logradouro, numero, complemento, bairro, cidade, cep;
    private Spinner uf;
    private Button botaoCadastrarEndereco;
    private Endereco endereco;

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE) //permite que essa variavel seja vista pela classe de teste
    private Toast mToast;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_endereco);

        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.tb_endereco);

        logradouro = findViewById(R.id.editText_endereco_logradouro);
        numero = findViewById(R.id.editText_endereco_numero);
        complemento = findViewById(R.id.editText_endereco_complemento);
        bairro = findViewById(R.id.editText_endereco_bairro);
        cidade = findViewById(R.id.editText_endereco_cidade);
        cep = findViewById(R.id.editText_endereco_cep);
        uf = findViewById(R.id.spinner_endereco_uf);
        String[] array_spinner = {"ACRE", "ALAGOAS", "AMAPA", "AMAZONAS", "BAHIA", "CEARA", "DISTRITO FEDERAL", "ESPIRITO SANTO",
                "GOIAS", "MARANHAO", "MATO GROSSO", "MATO GROSSO DO SUL", "MINAS GERAIS", "PARA", "PARAIBA", "PARANA", "PERNAMBUCO", "PIAUI",
                "RIO DE JANEIRO", "RIO GRANDE DO NORTE", "RIO GRANDE DO SUL", "RONDONIA", "RORAIMA", "SANTA CATARINA",
                "SAO PAULO", "SERGIPE", "TOCANTINS"};
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, array_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        uf.setAdapter(adapter);

        botaoCadastrarEndereco = findViewById(R.id.botao_endereco_cadastrar);
        botaoCadastrarEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endereco = new Endereco();
                endereco.setLogradouro(logradouro.getText().toString());
                endereco.setNumero(numero.getText().toString());
                endereco.setComplemento(complemento.getText().toString());
                endereco.setBairro(bairro.getText().toString());
                endereco.setCidade(cidade.getText().toString());
                endereco.setCep(cep.getText().toString());
                endereco.setUf(uf.getAdapter().toString());
                cadastrarEndereco();
            }
        });

        // Configura toolbar
        toolbar.setTitle("Cadastro de Endereço");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left_white);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void verificarCamposObrigatorios() throws CampoEnderecoObrAusenteException {
        if (logradouro.getText().toString().isEmpty()
                ||
                numero.getText().toString().isEmpty()
                ||
                complemento.getText().toString().isEmpty()
                ||
                bairro.getText().toString().isEmpty()
                ||
                cidade.getText().toString().isEmpty()
                ||
                cep.getText().toString().isEmpty()
                ) {
            throw new CampoEnderecoObrAusenteException();
        }
    }

    private void cadastrarEndereco() {
        try {
            this.verificarCamposObrigatorios();
            endereco.salvar();
        } catch (CampoEnderecoObrAusenteException e) {
            mToast = mToast.makeText(CadastroEnderecoActivity.this, R.string.erro_cadastro_endereco_campos_obrigatorios_Toast, Toast.LENGTH_SHORT);
            mToast.show();
        } catch (Exception e) {
            mToast = mToast.makeText(CadastroEnderecoActivity.this, R.string.erro_cadastro_endereco_campos_obrigatorios_Toast, Toast.LENGTH_SHORT);
            mToast.show();
        }
    }

    public void abrirCadastro() {
        Intent intent = new Intent(CadastroEnderecoActivity.this, CadastroUsuarioActivity.class);
        startActivity(intent);
        finish();
    }
}
