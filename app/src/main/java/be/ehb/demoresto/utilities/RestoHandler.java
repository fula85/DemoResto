package be.ehb.demoresto.utilities;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import be.ehb.demoresto.model.MockRestoDataSource;
import be.ehb.demoresto.model.Resto;
import okhttp3.Response;

public class RestoHandler extends Handler {

    private RestoAdapter mRestoAdapter;

    public RestoHandler(RestoAdapter mRestoAdapter) {
        this.mRestoAdapter = mRestoAdapter;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        String data = (String) msg.obj;

        try {
            JSONObject rootObject = new JSONObject(data);

            JSONArray records = rootObject.getJSONArray("records");

            int nrOfRecords = records.length();
            int index = 0;

            while (index < nrOfRecords){

                JSONObject currentRecord = records.getJSONObject(index);
                JSONObject fields = currentRecord.getJSONObject("fields");

                //controlz of kzy voorkomt in Jqon data via.has(key)
                //enkel indien key bestaat kan je de value eruit halen met bv. getString(
                String adres = (fields.has("adres"))?fields.getString("adres") : "geen adres";
                String naam = fields.getString("naam");

                Resto currentResto = new Resto(naam, adres);
                MockRestoDataSource.getInstance().addResto(currentResto);

                index++;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        mRestoAdapter.setItems(MockRestoDataSource.getInstance().getRestos() );
        mRestoAdapter.notifyDataSetChanged();

    }
}

