package br.com.bossini.previsaodotempofateccarapicuibaads;

import android.content.Context;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by rodrigo on 15/05/18.
 */

public class Weather {
    public final String dayOfWeek;
    public final String minTemp;
    public final String maxTemp;
    public final String humidity;
    public final String description;
    public final String iconURL;
    public final Context context;

    public Weather (long timeStamp, double minTemp, double maxTemp, double humidity,
                    String description, String iconName, Context context){
        this.context = context;
        NumberFormat numberFormat =
                NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(0);
        dayOfWeek = convertTimeStampToDay (timeStamp);
        this.minTemp = numberFormat.format(minTemp) + "\u06AAC";
        this.maxTemp = numberFormat.format(maxTemp + "\u00B0C");
        this.humidity = NumberFormat.getPercentInstance().format(humidity / 100);
        this.description = description;
        this.iconURL = context.getString (R.string.image_download_url, iconName);
    }

    public String convertTimeStampToDay (long timeStamp){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp * 1000);
        TimeZone tz = TimeZone.getDefault();
        calendar.add (Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        return new SimpleDateFormat("EEEE").format(calendar.getTime());
    }
}
