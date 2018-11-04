package ifrs.com.tcc2018.model;

import android.os.Parcelable;

import org.parceler.Parcel;

import java.util.Date;
import java.util.Random;

import ifrs.com.tcc2018.util.Data;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


/**
 * Created by diego.gomes on 10/02/2017.
 */
@Parcel(implementations = {Imagem.class},
        value = Parcel.Serialization.BEAN,
        analyze = {Imagem.class})
public class Imagem extends RealmObject implements Parcelable {

    /**
     * The constant CREATOR.
     */
    public static final Creator<Imagem> CREATOR = new Creator<Imagem>() {
        @Override
        public Imagem createFromParcel(android.os.Parcel in) {
            return new Imagem(in);
        }

        @Override
        public Imagem[] newArray(int size) {
            return new Imagem[size];
        }
    };
    @PrimaryKey
    private long id;
    private String caminhoFoto;
    private String descricao;
    //@Ignore
    //private transient String base64;
    private String base64;
    private Date dtInc;

    /**
     * Instantiates a new Imagem.
     */
    public Imagem() {
    }

    /**
     * Instantiates a new Imagem.
     *
     * @param id          the id
     * @param caminhoFoto the caminho foto
     * @param descricao   the descricao
     * @param base64      the base 64
     */
    public Imagem(int id, String caminhoFoto, String descricao, String base64) {
        this.id = retornaProximoId();
        this.caminhoFoto = caminhoFoto;
        this.descricao = descricao;
        this.base64 = base64;
        this.dtInc = new Data().dataAtual();
    }

    /**
     * Instantiates a new Imagem.
     *
     * @param in the in
     */
    protected Imagem(android.os.Parcel in) {
        id = in.readInt();
        caminhoFoto = in.readString();
        descricao = in.readString();
        base64 = in.readString();
    }

    /**
     * Gets creator.
     *
     * @return the creator
     */
    /*public static Creator<Imagem> getCREATOR() {
        return CREATOR;
    }*/

    /**
     * Gets dt inc.
     *
     * @return the dt inc
     */
    public Date getDtInc() {
        return dtInc;
    }

    /**
     * Sets dt inc.
     *
     * @param dtInc the dt inc
     */
    public void setDtInc(Date dtInc) {
        this.dtInc = dtInc;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets caminho foto.
     *
     * @return the caminho foto
     */
    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    /**
     * Sets caminho foto.
     *
     * @param caminhoFoto the caminho foto
     */
    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }

    /**
     * Gets descricao.
     *
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Sets descricao.
     *
     * @param descricao the descricao
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Gets base 64.
     *
     * @return the base 64
     */
    public String getBase64() {
        return base64;
    }

    /**
     * Sets base 64.
     *
     * @param base64 the base 64
     */
    public void setBase64(String base64) {
        this.base64 = base64;
    }

    /**
     * Retorna proximo id int.
     *
     * @return the int
     */
    public int retornaProximoId() {
        final Random numRandomico = new Random();
        return numRandomico.nextInt(999999999);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt((int) id);
        dest.writeString(caminhoFoto);
        dest.writeString(descricao);
        dest.writeString(base64);
    }
}