package projetaobcc20172.com.projetopetemfoco.utils;

/**
 * Created by raul1 on 18/01/2018.
 */

public class Enumerates {

    public enum Filtro {
        AVALICAO,
        DISTANCIA,
        PROXIMO_MINHA_RESIDENCIA,
        PRECO;
    }

    public enum TipoFornecedor {
        AUTONOMO,
        TODOS,
        ESTABELECIMENTO;
    }

    public enum Estado{
        DEFAULT,
        BUSCANDO;
    }

    public enum Promocao{
        PADRAO,
        PROXIMO;
    }

}
