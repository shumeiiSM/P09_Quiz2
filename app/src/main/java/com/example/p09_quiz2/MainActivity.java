package com.example.p09_quiz2;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity {

    EditText etCood;
    Button btnSave, btnRead, btnMap;
    TextView tvShow;

    String folderLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCood = findViewById(R.id.etCoord);
        btnSave = findViewById(R.id.btnSave);
        btnRead = findViewById(R.id.btnRead);
        btnMap = findViewById(R.id.btnMap);

        tvShow = findViewById(R.id.tvShow);

        if (checkPermission()) {
            folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Quiz9b";
            File folder = new File(folderLocation);
            if (folder.exists() == false) {
                boolean result = folder.mkdir();
                if (result == true) {
                    Log.d("File Read/Write", "Folder created");
                }
            }
        } else {
            String msg = "Permission not granted to retrieve location info";
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Code for file writing
                String cood = etCood.getText().toString();

                // EXTERNAL FILE
                try {
                    folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Quiz9b";
                    File targetFile_I = new File(folderLocation, "quiz.txt");
                    FileWriter writer_I = new FileWriter(targetFile_I, false);
                    writer_I.write(cood + "\n");
                    writer_I.flush();
                    writer_I.close();

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Failed to write!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        });

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Code for file reading

                // EXTERNAL FILE
                if (checkPermission()) {
                    folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Quiz9b";
                    File targetFile = new File(folderLocation, "quiz.txt");

                    if (targetFile.exists() == true) {
                        String data = "";
                        try {
                            FileReader reader = new FileReader(targetFile);
                            BufferedReader br = new BufferedReader(reader);

                            String line = br.readLine();
                            while (line != null) {
                                data += line + "\n";
                                line = br.readLine();
                            }
                            br.close();
                            reader.close();
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, "Failed to read!", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                        Log.d("Content", data);
                        tvShow.setText(data);
                    }
                } else {
                    String msg = "Permission not granted to retrieve location info";
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                }
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),SecondActivity.class);
                intent.putExtra("cood", tvShow.getText().toString());
                startActivity(intent);
            }
        });


    }

    private boolean checkPermission() {
        int permissionCheck_Write = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheck_Read = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCheck_Write == PermissionChecker.PERMISSION_GRANTED
                || permissionCheck_Read == PermissionChecker.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
}
