package com.trungquach.mydaiary.utility;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.trungquach.mydaiary.view.MainActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by trungqn on 12/21/2017.
 */

public class Util implements Serializable{
    //save image to SD Card
    public static boolean saveImgToSDCard(Bitmap image, String folder, String name){
        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+folder+"/";
        if(!iSDReadable())
            return false;
        try {
            File dir = new File(fullPath);
            if (!dir.exists()) {
                if(!dir.mkdirs())
                    Log.i("saveImgToSDCard", "Can not create dir.");
            }

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

    // Save image to internal memory
    public static boolean saveImageToInternalStorage(Context context, Bitmap image, String name) {
        try {
            // Use the compress method on the Bitmap object to write image to
            // the OutputStream
            FileOutputStream fos = context.openFileOutput(name, Context.MODE_PRIVATE);

            // Writing the bitmap to the output stream
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

            return true;
        } catch (Exception e) {
            Log.e("saveToInternalStorage()", e.getMessage());
            return false;
        }
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

    public static void setBitmapToImg (final Context ctx, final String folder, final String name, final ImageView imageView){
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Bitmap bitmap = (Bitmap) msg.obj;
                if(bitmap != null)
                    imageView.setImageBitmap(bitmap);
                else
                    imageView.setVisibility(View.GONE);
            }
        };

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = Util.readImage(folder,name,ctx);
                Message msg = new Message();
                msg.obj = bitmap;
                handler.sendMessage(msg);
            }
        });
        thread.start();
    }

    private static Bitmap readImage(String folder, String name, Context ctx) {
        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/" + folder + "/"+ name;
        Bitmap bitmap = null;

        //read image from SD card
        try{
            bitmap = BitmapFactory.decodeFile(fullPath);
        }catch (Exception e ){
            Log.i("Read Image", "Can not read image from SD card.");
        }

        //read image from internal storage
        try{
            File myFile = ctx.getFileStreamPath(fullPath);
            FileInputStream fIn = new FileInputStream(myFile);

            bitmap = BitmapFactory.decodeStream(fIn);
        }catch (Exception e ){
            Log.i("Read Image", "Can not read image from internal storage.");
        }
        return bitmap;
    }

    public static String convertStringDateTimeToFileName(String date){
        return date.toString().replace("-", "").replace(" ", "").replace(":","");
    }
}
