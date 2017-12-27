package com.trungquach.mydaiary.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.trungquach.mydaiary.R;
import com.trungquach.mydaiary.controller.NoteDataSource;
import com.trungquach.mydaiary.utility.Config;
import com.trungquach.mydaiary.utility.Util;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class NewNote extends AppCompatActivity {

    private final int PICK_PHOTO_FOR_NOTE=0;
    private Toolbar toolbar;
    private ImageView imgAttach;
    private Bitmap bmpAttach;
    private EditText edtTitle, edtContent;
    private NoteDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        toolbar = (Toolbar) this.findViewById(R.id.my_toolbar);
        imgAttach = (ImageView) this.findViewById(R.id.img_attach);
        edtTitle = (EditText) this.findViewById(R.id.edt_title);
        edtContent = (EditText) this.findViewById(R.id.edt_content);

        //get action bar
        toolbar.setTitle("New Note");
        setSupportActionBar(toolbar);
        //create data source
        dataSource = new NoteDataSource(this);
        dataSource.open();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save:
                //check title
                if(edtTitle.getText().toString().trim().length() <=0){
                    Toast.makeText(this,"Please input title of note !!!", Toast.LENGTH_SHORT).show();
                    return true;
                }
                if(edtContent.getText().toString().trim().length() <=0){
                    Toast.makeText(this,"Please input content of note !!!", Toast.LENGTH_SHORT).show();
                    return true;
                }
                String dateTime = Util.getCurrentDateTime();
                String imgName = Util.convertStringDateTimeToFileName(dateTime)+".png";
                //save note to SQLite
                dataSource.addNewNote(edtTitle.getText().toString(), edtContent.getText().toString(),imgName,dateTime);
                //save image to SDCard
                if(bmpAttach != null)
                    Util.saveImgToSDCard(bmpAttach, Config.FOLDER_IMAGES, imgName);
                this.finish();
                Toast.makeText(this, "Add new note successfully", Toast.LENGTH_LONG).show();
                break;
            case R.id.attach:
                pickImage();
                break;
            default:
                return onOptionsItemSelected(item);
        }
        return true;
    }

    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select picture !!!"), PICK_PHOTO_FOR_NOTE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_PHOTO_FOR_NOTE && resultCode == Activity.RESULT_OK){
            if(data == null){
                //display an error
                Toast.makeText(this, "There is no selected photo", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                bmpAttach = BitmapFactory.decodeStream(bufferedInputStream);
                //show image in screen
                imgAttach.setImageBitmap(bmpAttach);
                imgAttach.setVisibility(View.VISIBLE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataSource.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_note,menu);
        return true;
    }
}
