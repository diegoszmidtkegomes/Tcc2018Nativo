package ifrs.com.tcc2018.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.Serializable;
import java.util.List;


import ifrs.com.tcc2018.R;
import ifrs.com.tcc2018.model.Imagem;
import ifrs.com.tcc2018.view.ImagemActivity;

/**
 * Adapter para lista de imagens capturadas
 *
 * @author diego.gomes
 * @version 1.0.0
 */
public class CapturarImagemAdapter extends BaseAdapter {

    /**
     * Lista de _imagens recebida por parâmetro que serão exibidas
     */
    private final List<Imagem> _imagens;
    /**
     * Contexto recebido por parâmetro
     */
    private final Context _context;
    /**
     * Escala calculada em tempo de execução para cálculo do tamanho e proporção da imagem
     */
    private float _escala;
    /**
     * Largura calculada em tempo de execução para cálculo do tamanho e proporção da imagem
     */
    private int _largura;
    /**
     * Altura calculada em tempo de execução para cálculo do tamanho e proporção da imagem
     */
    private int _altura;
    /**
     * Arquivo gerado com captura da imagem
     */
    private File arquivo;

    /**
     * Construtor da classe
     *
     * @param _imagens the imagens
     * @param _context the context
     */
    public CapturarImagemAdapter(List<Imagem> _imagens, Context _context) {
        this._imagens = _imagens;
        this._context = _context;
        this._escala = _context.getResources().getDisplayMetrics().density;
        this._largura = _context.getResources().getDisplayMetrics().widthPixels - (int) (14 * _escala + 0.5f);
        this._altura = (_largura / 16) * 9;
    }

    /**
     * Método implementado da BaseAdapter, retorna o tamanho da lista de imagens passada por parâmetro
     *
     * @return Tamanho da lista de imagens
     */
    @Override
    public int getCount() {
        if (_imagens != null) {
            return _imagens.size();
        } else {
            return 0;
        }
    }

    /**
     * Método implementado da BaseAdapter, retorna o objeto do item em uma determinada posição
     *
     * @param position Posição do item na lista
     * @return Object com o item da lista
     */
    @Override
    public Object getItem(int position) {
        return _imagens.get(position);
    }

    /**
     * Método implementado da BaseAdapter, retorna o id do item em uma determinada posição da lista
     *
     * @param position Posição do item na lista
     * @return Long com o id do item na posição da lista
     */
    @Override
    public long getItemId(int position) {
        return _imagens.get(position).getId();
    }

    /**
     * Método implementado da BaseAdapter, monta a View para cada item na lista. A exibição da imagem é feito com a biblioteca do Glide
     *
     * @param position    Posição do item na lista
     * @param convertView View que será alterada para o item atual
     * @param parent      Usado no inflate da View
     * @return View para o item
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Imagem imagem = _imagens.get(position);
        LayoutInflater inflater = LayoutInflater.from(_context);

        View view = convertView;
        if (view == null)
            view = inflater.inflate(R.layout.list_imagem, parent, false);

        ImageView campoFoto = view.findViewById(R.id.list_imagem_foto);

        if (campoFoto != null) {
            String caminhoFoto = imagem.getCaminhoFoto();

            Uri uri = Uri.parse(caminhoFoto);

            arquivo = new File(uri.toString());
            if (caminhoFoto != null) {

                Glide.with(_context)
                        .load(arquivo)
                        //.transform(new CircleTransform(..))
                        .override((_largura), (_altura))
                        //.centerCrop()
                        .fitCenter()
                        .into(campoFoto);
            }

            campoFoto.setId(position);
            campoFoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickImagem(position);
                }
            });

        }

        return view;
    }

    /**
     * Método chamado ao clicar em uma imagem. Será chamada a activity de visualização de imagens,
     * passando a lista de imagens carregadas e o id da imagem selecionada, para o viewpager
     * funcionar corretamente
     *
     * @param position Posição do item na lista
     */
    private void onClickImagem(int position) {
        Intent abrirImagem = new Intent(_context, ImagemActivity.class);
        abrirImagem.putExtra(_context.getString(R.string.param_posicaoselecionada), position);
        abrirImagem.putExtra(_context.getString(R.string.param_imagens), (Serializable) _imagens);
        abrirImagem.setFlags(abrirImagem.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        if (_context instanceof Activity)
            _context.startActivity(abrirImagem);
    }
}
