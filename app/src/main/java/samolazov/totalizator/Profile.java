package samolazov.totalizator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Profile extends AppCompatActivity {
    private static final String BASE_URL = "https://samolazov-totalizator.azurewebsites.net/";

    String ACCESS_TOKEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Intent intent = getIntent();
        ACCESS_TOKEN = intent.getStringExtra("ACCESS_TOKEN");


        TextView winText = findViewById(R.id.textView8);


        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .addHeader("Authorization", "Bearer " + ACCESS_TOKEN)
                .url(BASE_URL + "api/bet/GetWinAmount?userId=1")
                .get()
                .build();

        Call call = client.newCall(request);

        Response response = null;
        try {
            response = call.execute();
            winText.setText("Win Amount = " + response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
