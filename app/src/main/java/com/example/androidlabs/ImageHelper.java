package com.example.androidlabs;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class ImageHelper {
    public static String saveToInternalStorage(Bitmap bitmapImage, String emailId, Context context) throws IOException {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("profilePhotosDirectory", Context.MODE_PRIVATE);
        String fileName = emailId.split("@")[0].concat(".jpg");
        File myPath = new File(directory, fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 50, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return myPath.getAbsolutePath();
    }
    public static Bitmap loadImageFromStorage(String pathToPhoto)
    {
        try {
            File f=new File(pathToPhoto);
            return BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
