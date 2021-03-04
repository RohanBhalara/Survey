package com.example.survey;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Form1 extends AppCompatActivity implements FilePick{

    private static final String TAG = "DEBUG";
    private static final int FILE_PICK_REQ_CODe = 3135;
    TextView txtHeading;
    Button btnSave;
    MyRecyclerViewFormRow adapter;
    private static int currentFilPickIndex = -1;
    ArrayList<AnswerObject> answerObjectList;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form1);

        db = new DBHelper(Form1.this);
        txtHeading = (TextView)findViewById(R.id.txtHeading);
        btnSave = (Button)findViewById(R.id.btnSave);

        Intent intent = getIntent();
        final String formNumber = intent.getStringExtra("formNumber");
        final String formName = intent.getStringExtra("formName");
        txtHeading.setText(formName);
        final int fNumber = Integer.parseInt(formNumber);

        // data to populate the RecyclerView with
        ArrayList<QuestionObject> questionObjects = new ArrayList<>();

        QuestionObject qObj1 = new QuestionObject(1,"Question 1", "Small Answer", null);
        QuestionObject qObj2 = new QuestionObject(2,"Question 2", "Big Answer", null);
        String[] str = new String[2];
        str[0] = "Java";
        str[1] = "C++";
        QuestionObject qObj3 = new QuestionObject(3,"Question 3", "Checkbox", str);
        String[] str2 = new String[2];
        str2[0] = "True";
        str2[1] = "False";
        QuestionObject qObj4 = new QuestionObject(4,"Question 4", "Radio Button", str2);
        String[] str3 = new String[2];
        str3[0] = "True";
        str3[1] = "False";
        QuestionObject qObj5 = new QuestionObject(5,"Question 5", "Radio Button", str3);
        QuestionObject qObj6 = new QuestionObject(6,"Question 6", "Upload", null);

        questionObjects.add(qObj1);
        questionObjects.add(qObj2);
        questionObjects.add(qObj3);
        questionObjects.add(qObj4);
        questionObjects.add(qObj5);
        questionObjects.add(qObj6);

        checkPermission(WRITE_EXTERNAL_STORAGE, 101);
        // set up the RecyclerView
        final RecyclerView recyclerView = findViewById(R.id.rvQuestions);
        recyclerView.setLayoutManager(new LinearLayoutManager(Form1.this));
        adapter = new MyRecyclerViewFormRow(this, questionObjects,Form1.this);

        recyclerView.setAdapter(adapter);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answerObjectList = adapter.getAnswerObjectList();
                Collections.sort(answerObjectList, AnswerObject.QuestionId);
                String fileName = "file";
                int i = 1;
                for(AnswerObject aObj : answerObjectList){
                    Log.d(TAG, "id = "+aObj.getId());
                    if(aObj.fileAnser != null){
                        fileName = fileName + "(" +i+ ")";
                        copyFileFromUri(Form1.this, fileName, aObj.fileAnser);
                        i++;
                    }
                }

                String fn = db.getFormData(fNumber);
                if(fn == null){
                    if(db.insertFormData(fNumber, answerObjectList.toString())){
                        Toast.makeText(getApplicationContext(), "Inset done", Toast.LENGTH_SHORT).show();
                    } else{
                        Toast.makeText(getApplicationContext(), "Insert not done", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    if(db.updateFormData(fNumber, answerObjectList.toString())){
                        Toast.makeText(getApplicationContext(), "Update done", Toast.LENGTH_SHORT).show();
                    } else{
                        Toast.makeText(getApplicationContext(), "Update not done", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_PICK_REQ_CODe:
                if (resultCode == RESULT_OK && currentFilPickIndex!=-1) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d("STRING", "File Uri: " + uri.toString());

                    adapter.fileResult(uri,currentFilPickIndex);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean copyFileFromUri(Context context, String fileName, Uri fileUri)
    {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        Log.d("DEBUG",String.valueOf(isPermissionGranted(WRITE_EXTERNAL_STORAGE)));
        Log.d("DEBUG",String.valueOf(isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)));
        try
        {
            ContentResolver content = context.getContentResolver();
            inputStream = content.openInputStream(fileUri);

            // create a directory
            File saveDirectory = new File(getFilesDir()+File.separator+"files");
            //   create direcotory if it doesn't exists
            if(!saveDirectory.exists())
                saveDirectory.mkdirs();

            //Creating file
            File yourFile = new File( getFilesDir(),fileName+".txt");
            yourFile.createNewFile();


            outputStream = new FileOutputStream(yourFile); // filename.png, .mp3, .mp4 ...
            if(outputStream != null){
                Log.e( "STRING", "Output Stream Opened successfully");
            }

            byte[] buffer = new byte[1000];
            int bytesRead = 0;
            while ( ( bytesRead = inputStream.read( buffer, 0, buffer.length ) ) >= 0 )
            {
                outputStream.write( buffer, 0, buffer.length );
            }
            inputStream.close();
            outputStream.close();
            Log.d("DEBUG",yourFile.getAbsolutePath());

        } catch ( Exception e ){
            e.printStackTrace();
            Log.e( "STRING", "Exception occurred " + e.getMessage());
        } finally{

        }
        return true;
    }



    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(this, new String[] { permission }, requestCode);
        }
        else {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    // This function is called when the user accepts or decline the permission.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super .onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isPermissionGranted(String permission){
        return ContextCompat.checkSelfPermission(Form1.this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void showFilePicker(int index) {
        currentFilPickIndex = index;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_PICK_REQ_CODe);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
