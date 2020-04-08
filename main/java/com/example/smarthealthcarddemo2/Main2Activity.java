package com.example.smarthealthcarddemo2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

public class Main2Activity extends AppCompatActivity {
    ImageView image;
    DatabaseHelper Mydb;
    EditText editID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Mydb = new DatabaseHelper(this);
        editID=(EditText)findViewById(R.id.editText_ID);
        image=(ImageView)findViewById(R.id.imageView2);
        image.setImageBitmap(Mydb.getImage(Integer.valueOf(editID.getText().toString())));
    }
}
