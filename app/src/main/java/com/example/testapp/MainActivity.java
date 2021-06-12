package com.example.testapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private static final int FILE_EXPORT_REQUEST_CODE = 12;

    public EditText mEditText;
    Button mSaveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = findViewById(R.id.BigTextET);
        mSaveButton = findViewById(R.id.SaveB);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;

        if (requestCode == FILE_EXPORT_REQUEST_CODE) {
            if (data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                        new ExportTXT(this).execute(uri);
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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