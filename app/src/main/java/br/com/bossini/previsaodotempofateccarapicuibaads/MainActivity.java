package br.com.bossini.previsaodotempofateccarapicuibaads;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private ListView weatherListView;
    private WeatherArrayAdapter adapter;
    private List<Weather> forecast = new ArrayList <> ();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherListView = findViewById(R.id.weatherListView);
        adapter = new WeatherArrayAdapter(this, forecast);
        weatherListView.setAdapter(adapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText locationEditText = findViewById(R.id.locationEditText);
                String cidade = locationEditText.getEditableText().toString();
                String url = montaURL(cidade);
                ObtemTemperaturas o =
                        new ObtemTemperaturas();
                o.execute(url);

            }
        });
    }

    private String montaURL (String cidade){
        StringBuilder sb = new StringBuilder (
                getString(R.string.web_service_url));
                sb.append(cidade).
                append("&APPID=").
                append(getString(R.string.api_key)).
                append("&units=metric&").
                append("cnt=12");
        return sb.toString();
    }


    class ObtemTemperaturas
            extends AsyncTask <String, Void, String>{

        @Override
        protected String doInBackground(String... url) {
            try{

                //instanciar a classe obter temperaturas
                //executar
                OkHttpClient client =
                        new OkHttpClient();
                Request request =
                        new Request.Builder().
                                url(url[0]).build();
                Response response =
                        client.newCall(request).execute();
                String json = response.body().string();
                return json;
            }
            catch (IOException e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            forecast.clear();
            try{
                JSONObject jsonObj = new JSONObject(s);
                JSONArray list = jsonObj.getJSONArray("list");
                for (int i = 0; i < list.length(); i++){
                    JSONObject dia = list.getJSONObject(i);
                    long dt = dia.getLong("dt");
                    String description =
                            dia.getJSONArray("weather").
                                    getJSONObject(0).
                                    getString("description");
                    String icon =
                           dia.getJSONArray("weather").
                           getJSONObject(0).
                                   getString("icon");
                    double min =
                            dia.getJSONObject("temp").
                                    getDouble("min");
                    double max =
                            dia.getJSONObject("temp").
                                    getDouble("max");
                    int humidity =
                            dia.getInt("humidity");

                    Weather weather = new Weather(dt, min, max, humidity,
                            description, icon, MainActivity.this);
                    forecast.add(weather);

                }//for
                adapter.notifyDataSetChanged();
            }
            catch (JSONException e){
                e.printStackTrace();
                Toast.makeText(MainActivity.this,
                        getString(R.string.read_error),
                        Toast.LENGTH_SHORT).show();
            }

        }
    }
}
