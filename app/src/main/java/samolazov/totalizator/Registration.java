package samolazov.totalizator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Registration extends AppCompatActivity {
    private static final String BASE_URL = "https://samolazov-totalizator.azurewebsites.net/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void registerClick(View view) throws IOException {

        EditText loginTextField = findViewById(R.id.userNameField);
        EditText emailTextField = findViewById(R.id.emailField);
        EditText passwordTextField = findViewById(R.id.passwordField);

        String username = loginTextField.getText().toString();
        String email = emailTextField.getText().toString();
        String pass = passwordTextField.getText().toString();


        OkHttpClient client = new OkHttpClient();


        RequestBody formBody = new FormBody.Builder()
                .add("UserName", username)
                .add("Email", email)
                .add("Password", pass)
                .build();

        final Request request = new Request.Builder()
                .url(BASE_URL + "api/user/Register")
                .post(formBody)
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();
        if(response.code() == 200)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }
}
