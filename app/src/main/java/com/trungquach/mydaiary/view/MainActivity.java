package com.trungquach.mydaiary.view;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.trungquach.mydaiary.R;
import com.trungquach.mydaiary.controller.NoteAdapter;
import com.trungquach.mydaiary.controller.NoteDataSource;
import com.trungquach.mydaiary.controller.NoteSQLiteHelper;
import com.trungquach.mydaiary.model.NoteModel;

import java.util.ArrayList;

import static com.trungquach.mydaiary.R.id.lst_note;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private TextView lblNoContent;
    private ProgressBar prbLoading;
    private ListView lstNote;
    private Toolbar toolbar;
    private NoteDataSource noteDataSource;
    private ArrayList<NoteModel> arrNotes = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lblNoContent = (TextView) this.findViewById(R.id.lbl_no_content);
        prbLoading = (ProgressBar) this.findViewById(R.id.prd_load);
        lstNote = (ListView) this.findViewById(lst_note);
        //get action bar
        toolbar = (Toolbar) this.findViewById(R.id.my_toolbar);
//        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        //create data source
        noteDataSource = new NoteDataSource(this);
        noteDataSource.open();

        //read data from db
        viewAllNotes();

        //create adapter
        NoteAdapter adapter = new NoteAdapter(this,arrNotes);
        lstNote.setAdapter(adapter);
        lstNote.setOnItemClickListener(this);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            //update UI
            if(arrNotes == null || arrNotes.size() == 0){
                //there is no note
                lstNote.setVisibility(View.INVISIBLE);
                lblNoContent.setVisibility(View.VISIBLE);
                prbLoading.setVisibility(View.INVISIBLE);
            } else {
                //view notes
                NoteAdapter adapter = new NoteAdapter(MainActivity.this,arrNotes);
                lstNote.setAdapter(adapter);
                //hide lable noContent
                lblNoContent.setVisibility(View.INVISIBLE);
                prbLoading.setVisibility(View.INVISIBLE);
                lstNote.setVisibility(View.VISIBLE);
            }
        }
    };

    private void viewAllNotes() {
        //create new thread to get all note in background task
        new Thread(new Runnable() {
            @Override
            public void run() {
                arrNotes = noteDataSource.getAllNotes();
                handler.sendEmptyMessage(0);
            }
        }).start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        noteDataSource.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewAllNotes();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_note:
                Intent intent = new Intent(this, NewNote.class);
                startActivity(intent);
                break;
            case R.id.setting:
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ViewNote.class);
        intent.putExtra(NoteSQLiteHelper.COL_ID,arrNotes.get(position).getId());

//        intent.putExtra(NoteSQLiteHelper.COL_TITLE, arrNotes.get(position).getTitle());
//        intent.putExtra(NoteSQLiteHelper.COL_CONTENT, arrNotes.get(position).getContent());
//        intent.putExtra(NoteSQLiteHelper.COL_DATETIME, arrNotes.get(position).getDateTime());
//        intent.putExtra(NoteSQLiteHelper.COL_IMAGE, arrNotes.get(position).getImage());

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar,menu);
        return true;
    }
}
