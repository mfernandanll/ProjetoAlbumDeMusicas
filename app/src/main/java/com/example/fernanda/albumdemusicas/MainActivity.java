package com.example.fernanda.albumdemusicas;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String URL_TO_HIT = "https://indt-playlists.herokuapp.com/api/playlists";
    //private TextView tvData;
    private ListView lvAlbums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvAlbums = (ListView)findViewById(R.id.lvAlbums);

        new JSONtask().execute(URL_TO_HIT);

    }
    //Construindo a AsyncTask para que a conexão com a internet possa ser feita
    public class JSONtask extends AsyncTask<String, String, List<AlbumModels>> {
        @Override
        //conectando e baixando da internet os dados do JSON na Thread Background
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
                Log.e("JSON", finalJSON);

                List<AlbumModels> albumModelsList = new ArrayList<>();
                Gson gson = new Gson();

                AlbumModels[] wrapper = gson.fromJson(finalJSON, AlbumModels[].class);
                albumModelsList.addAll(Arrays.asList(wrapper));

                for(AlbumModels album : albumModelsList){
                    Log.e("JSON", "Author: "+album.getAuthor());
                    Log.e("JSON", "Followers: "+album.getFollowers());
                    Log.e("JSON", "Cover: "+album.getCover());
                    Log.e("JSON", "Title: "+album.getCollectionTitle());
                    Log.e("JSON", "------------------------------------");
                }

                return albumModelsList;

            }catch (MalformedURLException e){
                Log.e("Erro",e.getMessage());
            }catch (IOException e){
                Log.e("Erro",e.getMessage());
            }

            finally {
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
    //criando o Adapter para poder assimilar os objetos java vindos do JSON com a lista
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
                holder.ivAlbum = (ImageView)convertView.findViewById(R.id.ivAlbum);
                holder.tvAlbum = (TextView)convertView.findViewById(R.id.tvAlbum);
                holder.tvAuthor = (TextView)convertView.findViewById(R.id.tvAuthor);
                holder.tvFollowers = (TextView)convertView.findViewById(R.id.tvFollowers);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tvAlbum.setText(albumsModelList.get(position).getCollectionTitle());
            holder.tvAuthor.setText(albumsModelList.get(position).getAuthor());
            holder.tvFollowers.setText(albumsModelList.get(position).getFollowers());

            return convertView;
        }

        @Override
        public int getCount() {
            return albumsModelList.size();
        }

        //implementando um ViewHolder para a lista carregar mais rápido e não travar
        class ViewHolder{
            private ImageView ivAlbum;
            private TextView tvAlbum;
            private TextView tvAuthor;
            private TextView tvFollowers;
        }
    }
    //implementando menu com o Refresh
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

