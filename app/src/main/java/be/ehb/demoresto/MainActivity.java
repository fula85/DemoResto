package be.ehb.demoresto;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.io.IOException;

import be.ehb.demoresto.model.MockRestoDataSource;
import be.ehb.demoresto.utilities.RestoAdapter;
import be.ehb.demoresto.utilities.RestoHandler;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvRestos;
    private RestoHandler mRestoHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvRestos = findViewById(R.id.rc_resto);

        RestoAdapter mRestoAdapter = new RestoAdapter(MockRestoDataSource.getInstance().getRestos() );
        rvRestos.setAdapter(mRestoAdapter);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvRestos.setLayoutManager(mLayoutManager);

        mRestoHandler = new RestoHandler(mRestoAdapter);

        downloadData();
    }

    private void downloadData() {
        Thread backThread = new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("https://opendata.brussel.be/api/records/1.0/search/?dataset=eten-en-drinken&rows=80")
                        .get()
                        .build();

                try {
                    Response response = client.newCall(request).execute();


                    String responseBodyText = (response.body() != null) ? response.body().string() : null;

                    Message msg = new Message();
                    msg.obj = responseBodyText;
                    mRestoHandler.sendMessage(msg);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        backThread.start();
    }
}
