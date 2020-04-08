package com.example.smarthealthcarddemo2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper Mydb;
    EditText editName,editAge,editID;
    Button btnViewAll,btnUpdate,btnDelete,btnInsert,btnGet;
    ImageView image;
    private static final int pick_image=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Mydb = new DatabaseHelper(this);
        editName=(EditText)findViewById(R.id.editText_Name);
        editAge=(EditText)findViewById(R.id.editText_Age);
        editID=(EditText)findViewById(R.id.editText_ID);
        btnInsert=(Button)findViewById(R.id.button_insert);
        btnGet=(Button)findViewById(R.id.button_ViewImage);
        image=(ImageView)findViewById(R.id.imageView);
        btnViewAll=(Button)findViewById(R.id.button_View);
        btnUpdate=(Button)findViewById(R.id.button_update);
        btnDelete=(Button)findViewById(R.id.button_delete);
        btnGet.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        image.setImageBitmap(Mydb.getImage(Integer.valueOf(editID.getText().toString())));
                    }
                }
        );
        image.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OpenActivity2();
                    }
                }
        );
        btnInsert.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(Intent.ACTION_PICK, Uri.parse(
                                "content://media/internal/images/media"
                        ));
                        startActivityForResult(intent,pick_image);

                    }
                }
        );
        ViewAll();
        UpdateData();
        DeleteData();
    }

    public void OpenActivity2() {
        Intent intent=new Intent(this,Main2Activity.class);
        startActivity(intent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == pick_image) {
            Uri uri = data.getData();
            String x = getPath(uri);
            Integer num = Integer.parseInt(editID.getText().toString());
            String n = editName.getText().toString();
            if (Mydb.insertData(editName.getText().toString(),editAge.getText().toString(), x, num)) {
                Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(getApplicationContext(), "Not Successful", Toast.LENGTH_SHORT).show();
        }
    }

    public String getPath(Uri uri){
        if(uri==null)
            return null;
        String[] projection={MediaStore.Images.Media.DATA};
        Cursor cursor=managedQuery(uri,projection,null,null,null);
        if(cursor!=null){
            int column_index =cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }
    public void DeleteData(){
        btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer deletedRows=Mydb.deleteData(editID.getText().toString());
                        if(deletedRows>0)
                            Toast.makeText(MainActivity.this,"Data Deleted",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MainActivity.this,"Data not Deleted",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
    public void UpdateData(){
        btnUpdate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isUpdate = Mydb.updateData(editID.getText().toString(),
                                editName.getText().toString(),
                                editAge.getText().toString());
                        if (isUpdate==true)
                            Toast.makeText(MainActivity.this,"Data Updated",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MainActivity.this,"Data not Updated",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void ViewAll()
    {
        btnViewAll.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor res = Mydb.getAllData();
                        if(res.getCount()==0)
                        {
                            //show message
                            showMessage("Error","Nothing Found");
                            return;
                        }
                        StringBuffer buffer=new StringBuffer();
                        while(res.moveToNext()){
                            buffer.append("ID :"+res.getString(0)+"\n");
                            buffer.append("Name :"+res.getString(1)+"\n");
                            buffer.append("Date :"+res.getString(2)+"\n\n");
                        }
                        //show all data
                        showMessage("Data",buffer.toString());
                    }
                }
        );
    }
    public void showMessage(String title,String message){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
