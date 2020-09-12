package samolazov.totalizator;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BetActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://samolazov-totalizator.azurewebsites.net/";

    EditText price;
    RadioGroup radioGroup;
    String firstTeamName;
    String secondTeamName;
    String firstTeamId;
    String secondTeamId;
    String eventId;
    String ACCESS_TOKEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bet);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        Intent intent = getIntent();
        firstTeamName = intent.getStringExtra("firstTeamName");
        secondTeamName = intent.getStringExtra("secondTeamName");
        firstTeamId = intent.getStringExtra("firstTeamId");
        secondTeamId = intent.getStringExtra("secondTeamId");
        eventId = intent.getStringExtra("eventId");
        ACCESS_TOKEN  = intent.getStringExtra("ACCESS_TOKEN");
        String text = intent.getStringExtra("text");

        Toast.makeText(this, eventId, Toast.LENGTH_SHORT).show();

        TextView info = findViewById(R.id.textView6);
        info.setText(text);
        RadioButton radioButton0 = findViewById(R.id.first);
        RadioButton radioButton1 = findViewById(R.id.second);
        radioButton0.setText(firstTeamName);
        radioButton1.setText(secondTeamName);
        radioGroup = findViewById(R.id.radio);
        price = findViewById(R.id.count);
    }

    public void apply(View view) throws IOException {
        int radioId = radioGroup.getCheckedRadioButtonId();

        RadioButton radioButton = findViewById(radioId);
        if(radioButton.getText() == firstTeamName) {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder()
                    .add("Amount", price.getText().toString())
                    .add("EventId", eventId)
                    .add("TeamId", firstTeamId)
                    .add("UserId", "1")
                    .build();

            final Request request = new Request.Builder()
                    .addHeader("Authorization", "Bearer " + ACCESS_TOKEN)
                    .url(BASE_URL + "api/bet/CreateBet")
                    .post(formBody)
                    .build();

            Call call = client.newCall(request);
            Response response = call.execute();

            if(response.code() != 200) Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();

        } else if(radioButton.getText() == secondTeamName) {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder()
                    .add("Amount", price.getText().toString())
                    .add("EventId", eventId)
                    .add("TeamId", secondTeamId)
                    .add("UserId", "1")
                    .build();

            final Request request = new Request.Builder()
                    .addHeader("Authorization", "Bearer " + ACCESS_TOKEN)
                    .url(BASE_URL + "api/bet/CreateBet")
                    .post(formBody)
                    .build();

            Call call = client.newCall(request);
            Response response = call.execute();

            if(response.code() != 200) Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }
}
