package ifrs.com.tcc2018.util;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by diego.gomes on 23/02/2017.
 */
public class Data {

    public static String converteDataHora(Date data) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(data);
    }

    /**
     * Data atual date.
     *
     * @return the date
     */
    public static Date dataHoraAtual() {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Log.i("diego", "data atual: " + sdf.format(date));
        String dateStrg = sdf.format(date);
        try {
            date = sdf.parse(dateStrg);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * Data atual date.
     *
     * @return the date
     */
    public static Date dataAtual() {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String dateStrg = sdf.format(date);
        Log.i("diego", "data atual: " + sdf.format(date));
        try {
            date = sdf.parse(dateStrg);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * Data atual formatada string.
     *
     * @return the string
     */
    public static String dataHoraAtualFormatada() {
        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return sdf.format(date);
    }

    public static String dataAtualFormatada() {
        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return sdf.format(date);
    }


    public static String dataAtualFormatadaSemAno() {
        DateFormat sdf = new SimpleDateFormat("dd/MM HH:mm:ss");
        Date date = new Date();
        return sdf.format(date);
    }

    /**
     * Fuso horario atual string.
     *
     * @return the string
     */
    public static String fusoHorarioAtual() {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        Log.d("Time zone", "=" + tz.getDisplayName());
        return TimeZone.getDefault().getDisplayName();
        //return tz.toString();
    }

    public String converteData(Date data) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(data);
    }

    public static String getAnoAtual() {
        String data = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        return data;
    }

    public static String getMesAtual() {
        String data = String.valueOf(Calendar.getInstance().get(Calendar.MONTH));
        return data;
    }
}
