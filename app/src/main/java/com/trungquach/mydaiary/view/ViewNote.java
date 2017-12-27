package com.trungquach.mydaiary.view;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.trungquach.mydaiary.R;
import com.trungquach.mydaiary.controller.NoteDataSource;
import com.trungquach.mydaiary.controller.NoteSQLiteHelper;
import com.trungquach.mydaiary.model.NoteModel;
import com.trungquach.mydaiary.utility.Config;
import com.trungquach.mydaiary.utility.Util;

import org.w3c.dom.Text;

public class ViewNote extends AppCompatActivity {

    private Toolbar my_toolbar;
    private TextView lblContent;
    private ImageView imgView;
    private NoteDataSource dataSource;
    private NoteModel model;
    private int Note_ID= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        my_toolbar= (Toolbar) findViewById(R.id.my_toolbar);
        lblContent = (TextView) findViewById(R.id.lbl_content_view_note);
        imgView = (ImageView) findViewById(R.id.img_view_note);

        dataSource = new NoteDataSource(this);
        dataSource.open();

        //
        Note_ID = getIntent().getExtras().getInt(NoteSQLiteHelper.COL_ID);
        model = dataSource.getNote(Note_ID);

        my_toolbar.setTitle(model.getTitle());
        my_toolbar.setSubtitle(model.getDateTime());
        setSupportActionBar(my_toolbar);

        //binding data
        lblContent.setText(model.getContent());
        Util.setBitmapToImg(this,Config.FOLDER_IMAGES, model.getImage(),imgView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit:
                break;
            case R.id.delete:
                dataSource.deteleNote(Note_ID);
                this.finish();
                Toast.makeText(this,"Delete note successfully", Toast.LENGTH_LONG).show();
                break;
            default:
                return onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataSource.close();
    }
}
