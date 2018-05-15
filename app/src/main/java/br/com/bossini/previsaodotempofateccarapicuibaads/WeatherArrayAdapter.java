package br.com.bossini.previsaodotempofateccarapicuibaads;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rodrigo on 15/05/18.
 */

public class WeatherArrayAdapter extends ArrayAdapter <Weather> {

    public WeatherArrayAdapter (Context context, List<Weather> forecast){
        super (context, -1, forecast);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Context context = getContext();
        Weather caraDaVez = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item, parent, false);
        TextView dayTextView = view.findViewById(R.id.dayTextView);
        dayTextView.setText(context.getString(R.string.day_description, caraDaVez.dayOfWeek, caraDaVez.description));
        TextView lowTextView = view.findViewById(R.id.lowTextView);
        lowTextView.setText (context.getString(R.string.low_temp, caraDaVez.minTemp));
        TextView highTextView = view.findViewById(R.id.highTextView);
        highTextView.setText(context.getString(R.string.high_temp, caraDaVez.maxTemp));
        TextView humidityTextView = view.findViewById(R.id.humidityTextView);
        humidityTextView.setText(context.getString(R.string.humidity, caraDaVez.humidity));
        return view;
    }
}
