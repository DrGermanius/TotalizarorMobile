package samolazov.totalizator;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainPage extends AppCompatActivity {
    private static final String BASE_URL = "https://samolazov-totalizator.azurewebsites.net/";
    private static final int PAGE_NUMBER = 1;
    String ACCESS_TOKEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Intent intent = getIntent();
        ACCESS_TOKEN = intent.getStringExtra(MainActivity.ACCESS_TOKEN);
        try {
            final Intent ii = new Intent(this, BetActivity.class);
            final ListView listView = findViewById(R.id.ListViewGames);

            OkHttpClient client = new OkHttpClient();

            final Request request = new Request.Builder()
                    .addHeader("Authorization", "Bearer " + ACCESS_TOKEN)
                    .url(BASE_URL + "api/event/GetEventsList?pageNumber=" + PAGE_NUMBER)
                    .get()
                    .build();

            Call call = client.newCall(request);

            Response response = null;
            try {
                response = call.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
            Gson g = gsonBuilder.create();


            String a = null;

            a = response.body().string();
            a = validate(a);
            final EventClass[] b = g.fromJson(a, EventClass[].class);

            String[] namesOfEvents = new String[b.length];

            for (int i = 0; i < b.length; i++)
            {
                namesOfEvents[i] = b[i].Name + " " + b[i].TeamFirstName + " VS " + b[i].TeamSecondName + "\n" + b[i].Date;
            }

            ArrayAdapter adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, namesOfEvents);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String a = String.valueOf(b[(int) id].Id);
                    ii.putExtra("firstTeamName", b[position].TeamFirstName);
                    ii.putExtra("firstTeamId", String.valueOf(b[position].TeamFirstId));
                    ii.putExtra("secondTeamName", b[position].TeamSecondName);
                    ii.putExtra("secondTeamId", String.valueOf(b[position].TeamSecondId));
                    ii.putExtra("text", String.valueOf(listView.getAdapter().getItem(position)));
                    ii.putExtra("eventId", String.valueOf(b[position].Id));
                    ii.putExtra("ACCESS_TOKEN", ACCESS_TOKEN);
                    startActivity(ii);
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String validate(String s) {
        final char dm = (char) 34;
        String b = s.substring(1, s.length() - 1);
        b = b.replaceAll("\\\\", "");
        String res = "{" + dm + "EventClass" + dm + ":" + b + "}";
        return b;
    }

    public void goToProfile(View view) {
        Intent intent = new Intent(this, Profile.class);
        intent.putExtra("ACCESS_TOKEN", ACCESS_TOKEN);
        startActivity(intent);
    }
}
