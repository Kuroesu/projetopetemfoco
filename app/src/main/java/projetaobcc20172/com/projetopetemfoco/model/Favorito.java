package projetaobcc20172.com.projetopetemfoco.model;

import java.io.Serializable;


/**
 * Created by LuizAlberes on 12/01/2018.
 */

public class Favorito implements Serializable {

    private String mIdFavorito;
    private String mIdUsuario;
    private String mIdFornecedor;
    private String mTelefone;
    private String mNome;
    private String mConfirma;
    private String mCpfCnpj;

    public Favorito() {
    }

    public Favorito(String idFavorito, String idFornecedor, String nome, String telefone, String confirma, String cpfCnpj) {
        this.mIdFavorito = idFavorito;
        this.mIdFornecedor = idFornecedor;
        this.mTelefone = telefone;
        this.mNome = nome;
        this.mConfirma = confirma;
        this.mCpfCnpj = cpfCnpj;
    }

    public String getIdUsuario() {
        return mIdUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.mIdUsuario = idUsuario;
    }

    public String getIdFavorito() {
        return mIdFavorito;
    }

    public void setIdFavorito(String idFavorito) {
        this.mIdFavorito = idFavorito;
    }

    public String getIdFornecedor() {
        return mIdFornecedor;
    }

    public void setIdFornecedor(String idFornecedor) {
        this.mIdFornecedor = idFornecedor;
    }

    public String getTelefone() {
        return mTelefone;
    }

    public void setTelefone(String telefone) {
        this.mTelefone = telefone;
    }

    public String getNome() {
        return mNome;
    }

    public void setNome(String nome) {
        this.mNome = nome;
    }

    public String getConfirma() {
        return mConfirma;
    }

    public void setConfirma(String confirma) {
        this.mConfirma = confirma;
    }

    public String getCpfCnpj() {return mCpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {this.mCpfCnpj = cpfCnpj;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof Favorito)
        {
            Favorito c = (Favorito) o;
            if ( this.getIdFornecedor()==c.getIdFornecedor() ) //whatever here
                return true;
        }
        return false;
    }


}
