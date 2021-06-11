package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private static final String FILE_NAME = "example.txt";
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 1;

    EditText mEditText;
    Button mSaveButton, mLoadButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = findViewById(R.id.BigTextET);
        mSaveButton = findViewById(R.id.SaveB);
        mLoadButton = findViewById(R.id.LoadB);
    }

    public void save(View V) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, WRITE_EXTERNAL_STORAGE_CODE);
            }
            else {
                String text = mEditText.getText().toString();
                FileOutputStream fos = null;

                try {
                    Context context = getApplicationContext();
                    File mydir = new File(context.getExternalFilesDir("AppDataNew").getAbsolutePath());
                    if (!mydir.exists())
                    {
                        mydir.mkdirs();
                        Toast.makeText(getApplicationContext(),"Directory Created",Toast.LENGTH_LONG).show();
                    }

                    fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                    fos.write(text.getBytes());
                    System.out.println("Written !");

                    mEditText.getText().clear();
                    Toast.makeText(this, "Saved to" + FILE_NAME, Toast.LENGTH_LONG).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        else {
            String text = mEditText.getText().toString();
            FileOutputStream fos = null;

            try {
                fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                fos.write(text.getBytes());

                mEditText.getText().clear();
                Toast.makeText(this, "Saved to" + getFilesDir() + "/" + FILE_NAME, Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    public  void load(View V) {
        FileInputStream fis = null;

        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }

            mEditText.setText(sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public  void navuSave(View V) {
        wrtieFileOnInternalStorage(getApplicationContext(), "NewFile", "finally it happened !");
    }

    public void wrtieFileOnInternalStorage(Context context,String sFileName, String sBody){
        try {
            String h = DateFormat.format("MM-dd-yyyyy-h-mmssaa", System.currentTimeMillis()).toString();
// this will create a new name every time and unique
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
// if external memory exists and folder with name Notes
            if (!root.exists()) {
                root.mkdirs(); // this will create folder.
            }
            File filepath = new File(root, h + ".txt"); // file path to save
            FileWriter writer = new FileWriter(filepath);
            writer.append(sBody);
            writer.flush();
            writer.close();
            String m = "File generated with name " + h + ".txt";
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}