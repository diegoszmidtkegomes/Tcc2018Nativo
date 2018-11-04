package ifrs.com.tcc2018.repositorio;

import java.util.ArrayList;
import java.util.List;

import ifrs.com.tcc2018.model.Carro;
import ifrs.com.tcc2018.model.CarroWS;
import io.realm.Realm;
import io.realm.RealmList;

public class CarroRepositorio {

    public void inserir(List<CarroWS> carros){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        for (CarroWS carro: carros
             ) {
            Carro c = new Carro();
            try {
                c.setId(realm.where(Carro.class).max("id").intValue() + 1);
            } catch (Exception ex) {
                c.setId(1);
            }
            c.setNome(carro.getNome());
            c.setPreco(carro.getPreco());
            c.setNumeroPortas(carro.getNumeroPortas());
            RealmList<String> listaFotos = new RealmList<>();
            for (String f :
                    carro.getFotos()) {
                listaFotos.add(f);
            }
            c.setFotos(listaFotos);
            realm.copyToRealmOrUpdate(c);
        }
        realm.commitTransaction();
    }

    public List<Carro> buscaCarros() {
        Realm realm = Realm.getDefaultInstance();
        return new ArrayList<>(realm.where(Carro.class).findAll());
    }

    public Carro selectById(int id) {
        Realm realm = Realm.getDefaultInstance();
        Carro doc = realm.where(Carro.class).equalTo("id", id).findFirst();
        return doc;
    }

    public void deleteAll() {
        try {
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.delete(Carro.class);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
