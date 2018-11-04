package ifrs.com.tcc2018.model;

import java.io.Serializable;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Carro extends RealmObject implements Serializable {

    @PrimaryKey
    private int id;
    private String nome;
    private double preco;
    private int numeroPortas;
    private RealmList<String> fotos;


    public int getNumeroPortas() {
        return numeroPortas;
    }

    public void setNumeroPortas(int numeroPortas) {
        this.numeroPortas = numeroPortas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public List<String> getFotos() {
        return fotos;
    }

    public void setFotos(RealmList<String> fotos) {
        this.fotos = fotos;
    }
}
