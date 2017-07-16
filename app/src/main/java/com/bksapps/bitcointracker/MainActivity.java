package com.bksapps.bitcointracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    //Contants
    //https://apiv2.bitcoinaverage.com/convert/global?from=BTC&to=USD&amount=1
    private final String BASE_URL="https://apiv2.bitcoinaverage.com/convert/global";
    private final String BTC_SYMBOL="BTC";
    private final String TAG="BitcoinTracker";

    TextView priceLabel;
    Spinner currencySpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        priceLabel=(TextView)findViewById(R.id.priceLabel);
        currencySpinner=(Spinner)findViewById(R.id.currency_spinner);

        //Create Adapter using string array and spinner item layout
        ArrayAdapter<CharSequence> arrayAdapter=ArrayAdapter
                .createFromResource(this,R.array.currency_array, R.layout.spinner_items);

        // Specify the layout to use when list of choices appear
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_items);

        //Apply the adapter to spinner
        currencySpinner.setAdapter(arrayAdapter);

        currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RequestParams params=new RequestParams();
                params.put("from", BTC_SYMBOL);
                params.put("to", parent.getItemAtPosition(position));
                params.put("amount", 1);

                letsDoSomeNetworking(params);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("Bitcoin", "Nothing selected :C");
            }
        });

    }

    private void letsDoSomeNetworking(RequestParams params){
        AsyncHttpClient client=new AsyncHttpClient();
        client.get(BASE_URL, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, "onSuccess: Data: "+ response.toString());
                BitcoinModel model= BitcoinModel.fromJSON(response);
                UpdateUI(model);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(TAG, "onFailure: Something Went Wrong!!");
            }
        });
    }

    private void UpdateUI(BitcoinModel model){

        priceLabel.setText(String.valueOf(model.getPrice()));
    }

}
