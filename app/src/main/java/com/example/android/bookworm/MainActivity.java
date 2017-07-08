/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.bookworm;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.bookworm.QueryUtils.fetchEarthquakeData;

public class MainActivity extends AppCompatActivity {

    String searchResult;
    BookAdapter mAdapter;

    private static  final String USGS_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=Android";

//            "https://www.googleapis.com/books/v1/volumes?key=API_KEY AIzaSyBRh677vmv1BiYMJTr1oUotcK3TmY0GNUI";

//    /** URL for earthquake data from the USGS dataset */
//    private static final String USGS_REQUEST_URL =
//            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=6&limit=10";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_list, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Fetch the data remotely
//                String url;
//                url = ("https://www.googleapis.com/books/v1/volumes?q=" + query);
//                Uri.Builder builder = new Uri.Builder();
//                builder.scheme("https")
//                        .authority("https://www.googleapis.com")
//                        .appendPath("books")
//                        .appendPath("v1")
//                        .appendPath("volumes")
//                        .appendPath(query);
////                        .appendQueryParameter("type", "1")
////                        .appendQueryParameter("sort", "relevance")
////                        .fragment("section-name");
//                String myUrl = builder.build().toString();
//                Log.v("MainActivity", "My URL" + myUrl);



                //USGS_REQUEST_URL =  "https://www.googleapis.com/books/v1/volumes?q=" + query;
//                Log.v("MainAcivity", "fetchEarthQuakeData" + fetchEarthquakeData(query));
//
//                Log.v("MainActivity", "Query: " + query);
                // Reset SearchView
                String searchResult =  ("https://www.googleapis.com/books/v1/volumes?q=" + query);
                EarthquakeAsyncTask task = new EarthquakeAsyncTask();
                task.execute(searchResult);

               
                Log.v("MainActivity", "task.execute(searchResult): " + searchResult);
                Log.v("MainActivity", "USGS_rEQUEST_URL: " + USGS_REQUEST_URL);

                Log.v("MainActivity", "searchResult: "+ searchResult);

                //searchResult =  fetchEarthquakeData("https://www.googleapis.com/books/v1/volumes?q=" + query).toString();
                searchView.clearFocus();
                searchView.setQuery("", false);
                searchView.setIconified(true);
                searchItem.collapseActionView();
                // Set activity title to search query
                MainActivity.this.setTitle(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                Book currentEarthquake = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Start the AsyncTask to fetch the earthquake data
//        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
////        task.execute(searchResult);
//
//        task.execute(searchResult);
//        Log.v("MainActivity", "task.execute(searchResult): " + searchResult);
//        Log.v("MainActivity", "USGS_rEQUEST_URL: " + USGS_REQUEST_URL);



//        // Start the AsyncTask to fetch the earthquake data
//        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
//        task.execute(USGS_REQUEST_URL );
    }

    /**
     * {@link AsyncTask} to perform the network request on a background thread, and then
     * update the UI with the list of earthquakes in the response.
     *
     * AsyncTask has three generic parameters: the input type, a type used for progress updates, and
     * an output type. Our task will take a String URL, and return an Earthquake. We won't do
     * progress updates, so the second generic is just Void.
     *
     * We'll only override two of the methods of AsyncTask: doInBackground() and onPostExecute().
     * The doInBackground() method runs on a background thread, so it can run long-running code
     * (like network activity), without interfering with the responsiveness of the app.
     * Then onPostExecute() is passed the result of doInBackground() method, but runs on the
     * UI thread, so it can use the produced data to update the UI.
     */
    private class EarthquakeAsyncTask extends AsyncTask<String, Void, List<Book>> {

//        JSONParser jParser;
        JSONArray productList;
        String url= new String();
        String textSearch;
//        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            productList = new JSONArray();


        }

        /**
         * This method runs on a background thread and performs the network request.
         * We should not update the UI from a background thread, so we return a list of
         * {@link Book}s as the result.
         */
        @Override
        protected List<Book> doInBackground(String... urls) {

//             String returnResult = getProductList(url);
            // Don't perform the request if there are no URLs, or the first URL is null
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            //List<Book> result = fetchEarthquakeData(urls[0]);
            List<Book> result = fetchEarthquakeData(urls[0]);
            Log.v("MainActivity", "fetchEarthquakeData" + fetchEarthquakeData(urls[0]));
            return result;
        }

        /**
         * This method runs on the main UI thread after the background work has been
         * completed. This method receives as input, the return value from the doInBackground()
         * method. First we clear out the adapter, to get rid of earthquake data from a previous
         * query to USGS. Then we update the adapter with the new list of earthquakes,
         * which will trigger the ListView to re-populate its list items.
         */
        @Override
        protected void onPostExecute(List<Book> data) {
            // Clear the adapter of previous earthquake data
            mAdapter.clear();

            // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (data != null && !data.isEmpty()) {
                mAdapter.addAll(data);
            }
        }
    }

//    private void fetchBooks(String query) {
//        client = new BookClient();
//        client.getBooks(query, new JsonHttpResponseHandler() {
//      ...
//        });
//    }




//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_book_list, menu);
//        final MenuItem searchItem = menu.findItem(R.id.action_search);
//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                // Fetch the data remotely
////                String url;
////                url = ("https://www.googleapis.com/books/v1/volumes?q=" + query);
////                Uri.Builder builder = new Uri.Builder();
////                builder.scheme("https")
////                        .authority("https://www.googleapis.com")
////                        .appendPath("books")
////                        .appendPath("v1")
////                        .appendPath("volumes")
////                        .appendPath(query);
//////                        .appendQueryParameter("type", "1")
//////                        .appendQueryParameter("sort", "relevance")
//////                        .fragment("section-name");
////                String myUrl = builder.build().toString();
////                Log.v("MainActivity", "My URL" + myUrl);
//
//
//
//                searchResult =  fetchEarthquakeData("https://www.googleapis.com/books/v1/volumes?q=" + query);
//                Log.v("MainAcivity", "fetchEarthQuakeData" + fetchEarthquakeData(query));
//
//                Log.v("MainActivity", "Query: " + query);
//                // Reset SearchView
////                searchResult =  fetchEarthquakeData("https://www.googleapis.com/books/v1/volumes?q=" + query);
//                searchView.clearFocus();
//                searchView.setQuery("", false);
//                searchView.setIconified(true);
//                searchItem.collapseActionView();
//                // Set activity title to search query
//                MainActivity.this.setTitle(query);
//                return true;
//
//
//            }
//
//
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                return false;
//            }
//        });
//        return true;
//
//    }

}


