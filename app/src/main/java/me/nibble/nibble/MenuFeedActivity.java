package me.nibble.nibble;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MenuFeedActivity extends AppCompatActivity {
    private static final String TAG = "Local Bites";
    private List<FeedItem> feedsList;
    private RecyclerView mRecyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_feed);

        //Init recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        //Download data from server
        final String url = getResources().getString(R.string.json_host); //"http://nibbleme.herokuapp.com/"; //"http://javatechig.com/?json=get_recent_posts&count=45";
        new AsyncHttpTask().execute(url);
    }

    public void updateUI() {
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(MenuFeedActivity.this, feedsList);
        mRecyclerView.setAdapter(adapter);
    }

    public class AsyncHttpTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected String doInBackground(String... params) {
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;
            String response = "";
            try {
                // form the java.net.URL object
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                // optional request header
                urlConnection.setRequestProperty("Content-Type", "application/json");

                // optional request header
                urlConnection.setRequestProperty("Accept", "application/json");

                // for Get request
                urlConnection.setRequestMethod("GET");
                int statusCode = urlConnection.getResponseCode();

                //DEBUGGING ONLY
                lastStatusCode = String.valueOf(statusCode);

                // 200 represents HTTP OK
                if (statusCode == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    response = convertInputStreamToString(inputStream);
                }
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return response;
        }

        //DEBUGGING ONLY
        String lastStatusCode = "debug failed ;)";

        @Override
        protected void onPostExecute(String result) {
            //Download complete. Let's update UI
            progressBar.setVisibility(View.GONE);

            if (lastStatusCode.equals(String.valueOf(200))) {
                parseResult(result);
            } else {
                cheersToDebugging("Failed to fetch data. Status code: " + lastStatusCode);
            }
        }

        private String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while((line = bufferedReader.readLine()) != null) {
                result += line;
            }

            // close stream
            if (null != inputStream) {
                inputStream.close();
            }

            return result;
        }
    }

    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            JSONArray products = response.optJSONArray("users");
            feedsList = new ArrayList<>();

            for(int i = 0; i < products.length(); i++) {
                JSONObject product = products.optJSONObject(i);
                feedsList.add(new FeedItem(product.optString("name")));
            }
            updateUI();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void cheersToDebugging(String msg) {
        Toast.makeText(MenuFeedActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}





















