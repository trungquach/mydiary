package com.trungquach.mydaiary.controller;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trungquach.mydaiary.R;
import com.trungquach.mydaiary.model.NoteModel;
import com.trungquach.mydaiary.utility.Config;
import com.trungquach.mydaiary.utility.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * Created by trungqn on 12/21/2017.
 */

public class NoteAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NoteModel> arr;
    private Random rand;
    private String colors[] = {"#EF5350", "#EC407A", "#AB47BC", "#7E57C2", "#5C6BC0", "#42A5F5", "#29B6F6" };

    public NoteAdapter(Context context, ArrayList<NoteModel> arr) {
        this.context = context;
        this.arr = arr;

        rand = new Random();
    }

    @Override
    public int getCount() {
        return arr.size();
    }

    @Override
    public Object getItem(int position) {
        if (arr == null)
            return null;
        return arr.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (arr == null)
            return 0;
        return arr.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View rowView = inflater.inflate(R.layout.layout_note, parent, false);

        TextView lblDay = (TextView) rowView.findViewById(R.id.lbl_day);
        TextView lblDate = (TextView) rowView.findViewById(R.id.lbl_date);
        TextView lblTime = (TextView) rowView.findViewById(R.id.lbl_time);
        TextView lblTitle = (TextView) rowView.findViewById(R.id.lbl_title_view);
        TextView lblContent = (TextView) rowView.findViewById(R.id.lbl_content_view);
        ImageView imgView = (ImageView) rowView.findViewById(R.id.imageView);
        LinearLayout linearLayout = (LinearLayout) rowView.findViewById(R.id.lin_layout);

        linearLayout.setBackgroundColor(Color.parseColor(colors[rand.nextInt(colors.length)]));
        //get model
        NoteModel note = arr.get(position);
        //binding value
        lblTitle.setText(note.getTitle());
        lblContent.setText(note.getContent());
        //
        Date date = Util.convertStringToDate(note.getDateTime());
        String dayOfWeek = (String) DateFormat.format("EEEE",date);
        String dateOfMonth = (String) DateFormat.format("dd",date);
        String month = (String) DateFormat.format("MMM",date);
        String time = (String) DateFormat.format("hh:mm",date);

        lblDay.setText(dayOfWeek);
        lblDate.setText(dateOfMonth+" "+month);
        lblTime.setText(time);
        //set image
        Util.setBitmapToImg(context, Config.FOLDER_IMAGES,note.getImage(),imgView);

        return rowView;
    }
}
