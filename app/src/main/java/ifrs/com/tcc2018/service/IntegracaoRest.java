package ifrs.com.tcc2018.service;

import android.util.Log;

import java.util.List;

import ifrs.com.tcc2018.model.CarroWS;
import ifrs.com.tcc2018.model.iAsyncResponseObj;
import ifrs.com.tcc2018.model.Carro;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IntegracaoRest {

    private APIInterface apiInterface;


    public void buscaCarrosWS100Itens(final iAsyncResponseObj delegate){
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<List<CarroWS>> call = apiInterface.getListCarros100Itens();
        call.enqueue(new Callback<List<CarroWS>>() {
            @Override
            public void onResponse(Call<List<CarroWS>> call, Response<List<CarroWS>> response) {

                Log.d("TAG",response.code()+"");
                List<CarroWS> carros = response.body();

                /*if(carros!=null){

                    for (CarroWS carro: carros
                            ) {
                        Log.e("carro", carro.getNome());
                        Log.e("carro", carro.getPreco() + "");
                    }
                }*/
                delegate.processoEncerrado(carros);
            }
            @Override
            public void onFailure(Call<List<CarroWS>> call, Throwable t) {
                call.cancel();
                Log.e("diego", t.getLocalizedMessage());
            }
        });
    }

    public void buscaCarrosWS1000Itens(final iAsyncResponseObj delegate){
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<List<CarroWS>> call = apiInterface.getListCarros1000Itens();
        call.enqueue(new Callback<List<CarroWS>>() {
            @Override
            public void onResponse(Call<List<CarroWS>> call, Response<List<CarroWS>> response) {

                Log.d("TAG",response.code()+"");
                List<CarroWS> carros = response.body();

                if(carros!=null){

                    for (CarroWS carro: carros
                            ) {
                        Log.e("carro", carro.getNome());
                        Log.e("carro", carro.getPreco() + "");
                    }
                }
                delegate.processoEncerrado(carros);
            }
            @Override
            public void onFailure(Call<List<CarroWS>> call, Throwable t) {
                call.cancel();
                Log.e("diego", t.getLocalizedMessage());
            }
        });
    }
}
