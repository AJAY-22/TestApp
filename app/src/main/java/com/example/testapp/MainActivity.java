package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
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
import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private static final int FILE_EXPORT_REQUEST_CODE = 12;
    private static final String FILE_NAME = "example.txt";
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 1;

    public static EditText mEditText;
    Button mSaveButton, mLoadButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = findViewById(R.id.BigTextET);
        mSaveButton = findViewById(R.id.SaveB);
//        mLoadButton = findViewById(R.id.LoadB);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case FILE_EXPORT_REQUEST_CODE:
                if (data != null) {
                    Uri uri = data.getData();
                    if (uri != null) {
//                        new ExportTXT(this).execute(uri);
                    }
                }
                break;
        }
    }

    public void saveFile(View v) {
        Intent exportIntent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        exportIntent.addCategory(Intent.CATEGORY_OPENABLE);
        exportIntent.setType("text/plain");
        String filename = "test.txt";
        exportIntent.putExtra(Intent.EXTRA_TITLE, filename);
        startActivityForResult(exportIntent, FILE_EXPORT_REQUEST_CODE);
    }

    private class ExportTXT extends AsyncTask<Uri, Void, Boolean>{
        private final WeakReference<Context> context;

        ExportTXT(Context c) {
            context = new WeakReference<>(c);
        }

        @Override
        protected Boolean doInBackground(Uri... uris) {
            Uri uri = uris[0];
            Context c = context.get();

            if( c == null ) {
                return false;
            }

            String data = mEditText.getText().toString();
            boolean success = false;

            try {
                ParcelFileDescriptor pfd = c.getContentResolver().openFileDescriptor(uri, "w");
                if( pfd != null ) {
                    FileOutputStream fileOutputStream = new FileOutputStream(pfd.getFileDescriptor());
                    fileOutputStream.write(data.getBytes());
                    fileOutputStream.close();
                    success = true;
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            return success;
        }
    }

}