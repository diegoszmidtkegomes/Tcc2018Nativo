package ifrs.com.tcc2018.service;

import java.util.List;

import ifrs.com.tcc2018.model.Carro;
import ifrs.com.tcc2018.model.CarroWS;
import retrofit2.Call;
import retrofit2.http.GET;

public interface APIInterface {

    //@GET("5b855f9a3000009b247292b5")
    @GET("http://demo5138309.mockable.io/")
    //@GET("http://www.mocky.io/v2/5b9910203200008d0013fb9a")
    Call<List<CarroWS>> getListCarros1000Itens();

    //json sem o numero de portas
    //@GET("http://www.mocky.io/v2/5b906e6d2e0000af28a89eb8")
    @GET("http://www.mocky.io/v2/5b9910203200008d0013fb9a")
    Call<List<CarroWS>> getListCarros100Itens();
}
