package ifrs.com.tcc2018.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ifrs.com.tcc2018.R;
import ifrs.com.tcc2018.model.Carro;
import ifrs.com.tcc2018.model.CarroWS;
import ifrs.com.tcc2018.view.MainActivity;

public class CarrosAdapter extends RecyclerView.Adapter<CarrosAdapter.ViewHolder> {

    /**
     * Recycler view
     */
    private final RecyclerView _recyclerView;
    private MainActivity mainActivity;
    /**
     * Arraylist com o retorno do Webservice dos possíveis questionários para um conjunto
     */
    private ArrayList<Carro> carros;
    private ArrayList<CarroWS> carrosOnline;

    /**
     * Construtor da classe
     * @param carros   Lista de Checklists
     * @param carroListOnline
     * @param recyclerView RecyclerView
     * @param distribuicaoActivity
     */
    public CarrosAdapter(ArrayList<Carro> carros, ArrayList<CarroWS> carroListOnline, RecyclerView recyclerView, MainActivity distribuicaoActivity) {
        this.carros = carros;
        this._recyclerView = recyclerView;
        this.mainActivity = distribuicaoActivity;
        this.carrosOnline = carroListOnline;
    }

    /**
     * Método implementando para a criação do ViewHolder da RecyclerView. Chamado uma vez.
     *
     * @param parent   Usado para reaalizar o inflate da View
     * @param viewType tipo da View
     */
    @Override
    public CarrosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.list_carros, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        //view.setOnClickListener(new MyOnClickListener());
        return viewHolder;
    }

    /**
     * Método implementando para a criação do ViewHolder da RecyclerView.
     * Chamado uma vez para cada item.
     *
     * @param holder   Holder que será usado na RecyclerView.
     * @param position Posição do item que está sendo gerado
     */
    @Override
    public void onBindViewHolder(CarrosAdapter.ViewHolder holder, int position) {

        try {

            if(carros!=null && carros.size() > 0 ){
                Carro doc = carros.get(position);

                holder.tvModelo.setText(doc.getNome());
                if(doc.getNumeroPortas() > 0){
                    holder.tvPreco.setText("R$ " + String.valueOf(doc.getPreco()) + " Número de portas: " + doc.getNumeroPortas());
                }
                else{
                    holder.tvPreco.setText("R$ " + String.valueOf(doc.getPreco()));
                }
            }
            else{
                CarroWS doc = carrosOnline.get(position);
                holder.tvModelo.setText(doc.getNome());
                if(doc.getNumeroPortas() > 0){
                    holder.tvPreco.setText("R$ " + String.valueOf(doc.getPreco()) + " Número de portas: " + doc.getNumeroPortas());
                }
                else{
                    holder.tvPreco.setText("R$ " + String.valueOf(doc.getPreco()));
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Método implementando para a criação do ViewHolder da RecyclerView. Retorno o tamanho da
     * lista de itens
     *
     * @return Inteiro com o tamanho da lista
     */
    @Override
    public int getItemCount() {
        if(carros!=null && carros.size()>0)
            return carros.size();
        else return carrosOnline.size();
    }

    /**
     * The type View holder.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvModelo;
        public TextView tvPreco;

        /**
         * Instantiates a new View holder.
         *
         * @param v the v
         */
        public ViewHolder(View v) {
            super(v);
            tvModelo = (TextView) v.findViewById(R.id.list_carros_modelo);
            tvPreco = (TextView) v.findViewById(R.id.list_carros_preco);
        }
    }

    /**
     * Classe para capturar click na recycler view
     */
    /*public class MyOnClickListener implements View.OnClickListener {
        /**
         * Metodo que processo o click
         *
         * @param v View que foi clicada
         */
        /*@Override
        public void onClick(View v) {
            int itemPosition = _recyclerView.indexOfChild(v);
            Log.e("Clicked and Position is", String.valueOf(itemPosition));
        }*/
    }

