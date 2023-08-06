package com.example.mplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        // Toast.makeText(MainActivity.this, "Access to Storage", Toast.LENGTH_SHORT).show();
                        ArrayList<File> mySong = fetchSong(Environment.getExternalStorageDirectory());
                        String item[] = new String[mySong.size()];
                        for (int i=0; i<mySong.size();i++){
                            item[i] =mySong.get(i).getName().replace(".mp3","");
                        }

//                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,item);
//                        listView.setAdapter(arrayAdapter);

                        NewListAdpter ad1 = new NewListAdpter(MainActivity.this,R.layout.song_home_app,item);
                        listView.setAdapter(ad1);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(MainActivity.this,OpenMusic.class);
                                String currentSong = listView.getItemAtPosition(position).toString();
                                intent.putExtra("songlist",mySong);
                                intent.putExtra("currentsong",currentSong);
                                intent.putExtra("position",position);
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }

    public ArrayList<File> fetchSong(File file){
        ArrayList arrayList = new ArrayList();
        File song[] = file.listFiles();
        if (song != null) {
            for (File myfile : song) {
                if (!myfile.isHidden() && myfile.isDirectory()) {
                    arrayList.addAll(fetchSong(myfile));
                } else {
                    if (myfile.getName().endsWith(".mp3") && !myfile.getName().startsWith(".")) {
                        arrayList.add(myfile);
                    }
                }
            }
        }
        return arrayList;
    }
}