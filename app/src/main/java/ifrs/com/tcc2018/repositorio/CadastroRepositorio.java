package ifrs.com.tcc2018.repositorio;

import ifrs.com.tcc2018.model.Cadastro;
import io.realm.Realm;

public class CadastroRepositorio {

    public Cadastro inserir(String nome, String endereco, String email, String data, String modelo, double preco){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Cadastro c = new Cadastro();
        try {
            c.setId(realm.where(Cadastro.class).max("id").intValue() + 1);
        } catch (Exception ex) {
            c.setId(1);
        }
        c.setData(data);
        c.setEmail(email);
        c.setNome(modelo);
        c.setPreco(preco);
        c.setEndereco(endereco);
        c.setNome(nome);
        realm.copyToRealmOrUpdate(c);
        realm.commitTransaction();
        return c;
    }
}
