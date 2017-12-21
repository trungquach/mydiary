package com.trungquach.mydaiary.utility;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by trungqn on 12/21/2017.
 */

public class Util {
    public static boolean saveImgToSDCard(Bitmap image, String folder, String name){
        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+folder+"/";

        try {
            File dir = new File(fullPath);
            if (!dir.exists())
                dir.mkdir();

            OutputStream fOut = null;
            File file = new File(fullPath, name);
            if (!file.exists()) {
                file.createNewFile();
            }


            fOut = new FileOutputStream(file);

            //
            image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static String getCurrentDateTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datetime = dateFormat.format(new Date());
        return datetime;
    }

    public static boolean iSDReadable(){
        boolean mExtStorageAvailable = false;
        String state = Environment.getExternalStorageState();

        if(state.equals(Environment.MEDIA_MOUNTED)){
            mExtStorageAvailable = true;
            Log.i("isSDReadable", "External storage card is readable.");
        } else if(Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
            Log.i("isSDReadable", "External storage card is readable.");
            mExtStorageAvailable = true;
        } else {
            mExtStorageAvailable = false;
        }
        return mExtStorageAvailable;
    }

    public static Date convertStringToDate(String dateTime){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = dateFormat.parse(dateTime);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}
