package com.example.exempleappinfotempsretrofit;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/"; //URL base de l'app d'OpenWeather
    private static final String API_KEY = "fe0a843a5afb9263e58ee4ca060273f5"; // Reemplaça amb la teva Key de OpenWeather

    private TextView temperatureTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Obté l'id del textView on es mostrarà la temperatura
        temperatureTextView = findViewById(R.id.temperatureTextView);

        // Iniciar el AsyncTask para realizar la solicitud en segundo plano
        new WeatherAsyncTask().execute("Barcelona");
    }

    private class WeatherAsyncTask extends AsyncTask<String, Void, Double> {

        @Override
        protected Double doInBackground(String... strings) {
            try {
                String city = strings[0];

                // Configurar Retrofit
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                // Crear una instància de la interfície de l'API
                WeatherApi weatherApi = retrofit.create(WeatherApi.class);

                // Realitza la solicitud a la API
                Call<WeatherResponse> call = weatherApi.getWeather("Barcelona", API_KEY);
                Response<WeatherResponse> response = call.execute();
                if (response.isSuccessful()) {
                    WeatherResponse weatherResponse = response.body();
                    if (weatherResponse != null) {
                        return weatherResponse.getMain().getTemperature();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Double temperature) {
            if (temperature != null) {
                temperatureTextView.setText(String.format("%.1f°C", temperature));
            } else {
                // Manejar errores aquí
            }
        }
    }


}