package com.example.otavio.sunshine_myversion;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    ListView forecastListView;
    WeatherListAdapter arrayAdapter;
    SharedPreferences sharedPreferences;

    private class APITask extends AsyncTask<String,Void,ArrayList<WeatherElement>>{

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(MainActivity.this);
        }

        @Override
        protected ArrayList<WeatherElement> doInBackground(String... string) {

            JSONArray resultados;
            JSONObject jsonObject;

            String format = "json";
            String units = "metric";
            Integer numDays = 7;
            String appId = "f0c18761006dd98b057f10e42c846360";

            try {

                final String FORECAST_BASE_URL ="http://api.openweathermap.org/data/2.5/forecast/daily?";
                final String QUERY_PARAM = "q";
                final String FORMAT_PARAM = "mode";
                final String UNIT_PARAM = "units";
                final String DAYS_PARAM = "cnt";
                final String ID_PARAM = "appid";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon().appendQueryParameter(QUERY_PARAM, string[0])
                        .appendQueryParameter(FORMAT_PARAM, format)
                        .appendQueryParameter(UNIT_PARAM, units)
                        .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                        .appendQueryParameter(ID_PARAM, appId)
                        .build();

                URL url = new URL(builtUri.toString());

                String conteudo = HTTPUtils.acessar(url.toString());

                if (conteudo != null) {
                    jsonObject = new JSONObject(conteudo);
                } else{
                    jsonObject = null;
                }
                if (conteudo != null) {
                    resultados = jsonObject.getJSONArray("list");
                }else{
                    resultados = null;
                }
                ArrayList<WeatherElement> results = new ArrayList<>();

                if(resultados != null) {
                    for (int i = 0; i < resultados.length(); i++) {
                        JSONObject dia = resultados.getJSONObject(i);
                        //DATA
                        String dt = dia.getString("dt");
                        Long timestamp = Long.valueOf(dt).longValue();
                        Date date = new Date((Long)timestamp*1000);
                        DateFormat df = new SimpleDateFormat("dd/MM");
                        String data = df.format(date);
                        //MAX MIN
                        JSONObject temp = dia.getJSONObject("temp");
                        String min = temp.getString("min").substring(0,2);
                        String max = temp.getString("max").substring(0,2);
                        //Criando Objeto
                        WeatherElement element = new WeatherElement(data,min,max);

                        results.add(element);
                    }
                } else{
                    results.add(new WeatherElement(url.toString(),"erro","erro"));
                }
                return results;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<WeatherElement> strings) {
            super.onPostExecute(strings);

            if(strings != null){
                arrayAdapter = new WeatherListAdapter(getBaseContext(), strings);
                forecastListView.setAdapter(arrayAdapter);
            }
        }
    }

    public static class HTTPUtils {
        public static String acessar(String endereco) throws IOException {
            try {
                URL url = new URL(endereco);

                URLConnection connection = url.openConnection();

                InputStream inputStream = connection.getInputStream();

                Scanner scanner = new Scanner(inputStream);

                String conteudo = scanner.useDelimiter("\\A").next();

                scanner.close();

                return conteudo;
            } catch (Exception e) {
                return null;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Nao acontece nada!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        forecastListView = (ListView)findViewById(R.id.listview_forecast);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String location = sharedPreferences.getString(getString(R.string.pref_location_key),getString(R.string.pref_location_default));

        new APITask().execute(location);

        forecastListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                WeatherElement weatherElement = arrayAdapter.getItem(i);
                String date = weatherElement.getDate();
                String min = weatherElement.getMin();
                String max = weatherElement.getMax();

                Intent intent = new Intent(MainActivity.this , DetailsActivity.class)
                        .putExtra("date", date)
                        .putExtra("min", min)
                        .putExtra("max", max);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        if (id == R.id.action_refresh){
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            APITask apiTask = new APITask();
            String location = sharedPreferences.getString(getString(R.string.pref_location_key)
                    ,getString(R.string.pref_location_default));
            apiTask.execute(location);

            return true;
        }

        if (id == R.id.action_map){
            openPreferedLocation();
        }

        return super.onOptionsItemSelected(item);
    }

    public void openPreferedLocation(){
        String location = sharedPreferences.getString(getString(R.string.pref_location_key)
                ,getString(R.string.pref_location_default));

        Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
                .appendQueryParameter("q", location)
                .build();

        Intent intent = new Intent(Intent.ACTION_VIEW).setData(geoLocation);

        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        } else{
            Log.d("LOG_TAG","nao deu certo");
        }
    }
}
