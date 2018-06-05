package br.com.bossini.previsaodotempofateccarapicuibaads;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rodrigo on 15/05/18.
 */

public class WeatherArrayAdapter extends ArrayAdapter <Weather> {

    public WeatherArrayAdapter (Context context, List<Weather> forecast){
        super (context, -1, forecast);
    }

    private Map <String, Bitmap> cacheDeFiguras = new HashMap<>();

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Context context = getContext();
        Weather caraDaVez = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
            viewHolder.dayTextView = convertView.findViewById(R.id.dayTextView);
            viewHolder.dayTextView.setText(context.getString(R.string.day_description, caraDaVez.dayOfWeek, caraDaVez.description));
            viewHolder.lowTextView = convertView.findViewById(R.id.lowTextView);
            viewHolder.lowTextView.setText (context.getString(R.string.low_temp, caraDaVez.minTemp));
            viewHolder.highTextView = convertView.findViewById(R.id.highTextView);
            viewHolder.highTextView.setText(context.getString(R.string.high_temp, caraDaVez.maxTemp));
            viewHolder.humidityTextView = convertView.findViewById(R.id.humidityTextView);
            viewHolder.humidityTextView.setText(context.getString(R.string.humidity, caraDaVez.humidity));
            viewHolder.iconImageView = convertView.findViewById(R.id.iconImageView);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }


        if (cacheDeFiguras.containsKey(caraDaVez.nomeFigura)){
            viewHolder.iconImageView.setImageBitmap(cacheDeFiguras.get(caraDaVez.nomeFigura));
        }
        else{
            new ObtemFigura(viewHolder.iconImageView, caraDaVez.nomeFigura).execute(caraDaVez.iconURL);
        }


        return convertView;
    }

    class ViewHolder{
        ImageView iconImageView;
        TextView dayTextView, lowTextView, highTextView, humidityTextView;
    }
    class ObtemFigura extends AsyncTask<String, Void, Bitmap> {
        private ImageView iconImageView;
        private String nomeFigura;
        public ObtemFigura (ImageView iconImageView, String nomeFigura){
            this.iconImageView = iconImageView;
            this.nomeFigura = nomeFigura;
        }
        @Override
        protected Bitmap doInBackground(String... urls) {
            try{
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                Bitmap figura = BitmapFactory.decodeStream(inputStream);
                return figura;
            }
            catch (MalformedURLException e){
                e.printStackTrace();
            }
            catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap figura) {
            this.iconImageView.setImageBitmap(figura);
            cacheDeFiguras.put(nomeFigura, figura);
        }
    }
}
