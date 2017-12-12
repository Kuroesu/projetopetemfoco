package projetaobcc20172.com.projetopetemfoco;

import android.content.Context;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;


import projetaobcc20172.com.projetopetemfoco.excecoes.ValidacaoException;
import projetaobcc20172.com.projetopetemfoco.model.Fornecedor;
import projetaobcc20172.com.projetopetemfoco.utils.VerificadorDeObjetos;

/**
 * Created by raul on 12/12/17.
 */
@RunWith(Parameterized.class)
public class UnitTestCadastroFornecedor {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private Context mMockContext;

    @Parameterized.Parameter
    public Fornecedor fornecedorTest;

    @Parameterized.Parameters
    public static Iterable<? extends Object> data() {
        String nome = "Teste";
        String email="teste@gmail.com";
        String cpfCnpj = "123456";
        String telefone="996085321";
        String senha="123456";
        String horarios = "17:30";

        return Arrays.asList(
                new Fornecedor(nome,email,cpfCnpj,telefone,senha,senha,""),
                new Fornecedor(nome,email,cpfCnpj,telefone,senha,"",""),
                new Fornecedor(nome,email,cpfCnpj,telefone,"","",""),
                new Fornecedor(nome,email,cpfCnpj,"","","",""),
                new Fornecedor(nome,email,"","","","",""),
                new Fornecedor(nome,"","","","","",""),
                new Fornecedor("",email,cpfCnpj,telefone,senha,senha,horarios),
                new Fornecedor(nome,"",cpfCnpj,telefone,senha,senha,horarios),
                new Fornecedor(nome,email,"",telefone,senha,senha,horarios),
                new Fornecedor(nome,email,cpfCnpj,"",senha,senha,horarios));
    }

    @Test(expected=ValidacaoException.class)
    public void testCampoObgUsuario() throws ValidacaoException {
        VerificadorDeObjetos.vDadosFornecedor(fornecedorTest,mMockContext);
    }
}
