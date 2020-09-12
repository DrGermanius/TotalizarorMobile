package samolazov.totalizator;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


    private static final String BASE_URL = "https://samolazov-totalizator.azurewebsites.net/";
    public static String ACCESS_TOKEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void loginClick(View view) throws IOException {
        if(isOnline(getApplicationContext()))
        {
            
      
        OkHttpClient client = new OkHttpClient();

        EditText loginTextField = findViewById(R.id.loginTextField);
        EditText passwordTextField = findViewById(R.id.passwordTextField);

        String loginText = loginTextField.getText().toString();
        String passwordText = passwordTextField.getText().toString();

        RequestBody formBody = new FormBody.Builder()
                .add("username", loginText)
                .add("password", passwordText)
                .add("grant_type", "password")
                .build();

        final Request request = new Request.Builder()
                .url(BASE_URL + "token")
                .post(formBody)
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();

        Gson g = new Gson();
        AccessTokenClass object = g.fromJson(response.body().string(), AccessTokenClass.class);

        ACCESS_TOKEN = object.access_token;

        Intent intent = new Intent(this, MainPage.class);
        intent.putExtra(ACCESS_TOKEN, ACCESS_TOKEN);
        startActivity(intent);

        }
        else
        {
            Toast.makeText(this, "Please connect to the internet", Toast.LENGTH_SHORT).show();
        }
    }

    public void registerClick(View view) {
        if(isOnline(getApplicationContext())) {
            Intent intent = new Intent(this, Registration.class);
            startActivity(intent);
        }else
        {
            Toast.makeText(this, "Please connect to the internet", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }
}
