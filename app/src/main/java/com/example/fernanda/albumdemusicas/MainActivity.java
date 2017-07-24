package com.example.fernanda.albumdemusicas;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String URL_TO_HIT = "https://jsonparsingdemo-cec5b.firebaseapp.com/jsonData/moviesData.txt";
    //private TextView tvData;
    private ListView lvAlbums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvAlbums = (ListView)findViewById(R.id.lvAlbums);

        new JSONtask().execute(URL_TO_HIT);

    }

    public class JSONtask extends AsyncTask<String, String, List<AlbumModels>> {
        @Override
        protected List<AlbumModels> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try{
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }
                String finalJSON = buffer.toString();
                JSONObject parentObject = new JSONObject();
                JSONArray parentArray = new JSONArray(finalJSON);
                parentArray = parentObject.getJSONArray("");

                List<AlbumModels> albumModelsList = new ArrayList<>();
                Gson gson = new Gson();


                //List<MovieModel> movieModelList = new ArrayList<>();
                for(int i=0; i<parentArray.length(); i++){
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    AlbumModels albumModel = gson.fromJson(finalObject.toString(), AlbumModels.class);
                    albumModelsList.add(albumModel);
                }
                return albumModelsList;

            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection != null){
                    connection.disconnect();
                }
                try{
                    if(reader != null){
                        reader.close();
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(final List<AlbumModels> result) {
            super.onPostExecute(result);
            AlbumsAdapter adapter = new AlbumsAdapter(getApplicationContext(), R.layout.tela1, result);
            lvAlbums.setAdapter(adapter);
        }
    }

    public class AlbumsAdapter extends ArrayAdapter{
        private int resource;
        private LayoutInflater inflater;
        public List<AlbumModels> albumsModelList;
        public AlbumsAdapter(Context context, int resource, List<AlbumModels> objects) {
            super(context, resource, objects);
            albumsModelList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if(convertView == null){
                holder = new ViewHolder();
                convertView = inflater.inflate(resource, null);
                holder.ivAlbum = (ImageView)findViewById(R.id.ivAlbum);
                holder.tvAlbum = (TextView)findViewById(R.id.tvAlbum);
                holder.tvAuthor = (TextView)findViewById(R.id.tvAuthor);
                holder.tvFollowers = (TextView)findViewById(R.id.tvFollowers);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }


            holder.tvAlbum.setText(albumsModelList.get(position).getCollectionTitle());
            holder.tvAuthor.setText(albumsModelList.get(position).getAuthor());
            holder.tvFollowers.setText(albumsModelList.get(position).getFollowers());


            return convertView;
        }

        class ViewHolder{
            private ImageView ivAlbum;
            private TextView tvAlbum;
            private TextView tvAuthor;
            private TextView tvFollowers;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            new JSONtask().execute(URL_TO_HIT);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

