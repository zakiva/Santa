package com.example.zakiva.santa.Models;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.example.zakiva.santa.Helpers.Cache;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;

/**
 * Created by Ariel on 8/28/2016.
 */
public class Images {

    private static LruCache<String, Bitmap> mMemoryCache;

    /*
Use this method to update an image. The arguments are:
imagename - the name of the image
imageViewId - the integer that represents the imageView ID. The format is: [R.id.imageView]
yourActivity - the activity that the imageView is in, The format is: [MainActivity.this]
index - this helps the caching mechanism to search for the image. Each time you should call this method with a different index. The format is: ["1"]
*/
    static void updateImage(final String imageName, final int imageViewId, final Activity yourActivity, final String index){
        ImageView image = (ImageView) yourActivity.findViewById(imageViewId);
        //final Bitmap bitmap = getBitmapFromMemCache(imageName);
        Bitmap bitmap = (Bitmap) Cache.getInstance().getLru().get(imageName);
        if (bitmap != null) {
            image.setImageBitmap(bitmap);
            Log.d("aaaaaa: ", "RAM");
        } else {
            SharedPreferences settings = yourActivity.getSharedPreferences("MY_DATA", 0);
            String Pic = settings.getString("IM1" + index, "NONE");
            String name = settings.getString("IM1 " + index + " " + imageName, "NONE");
            if (!Pic.equals("NONE") && name.equals(imageName)){
                byte[] imageAsBytes = Base64.decode(Pic.getBytes(), Base64.DEFAULT);
                Bitmap bm = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                image.setImageBitmap(bm);
                Cache.getInstance().getLru().put(imageName, bm);
                Log.d("aaaaaa: ", "DISK");
            }
            else {
                downloadAndUpdateImage(imageName, imageViewId, yourActivity, index);
                Log.d("aaaaaa: ", "WEB");
            }
        }
    }

    static void downloadAndUpdateImage(final String imageName, final int imageViewId, final Activity yourActivity, final String index){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://windis-72265.appspot.com");

        storageRef.child(imageName).getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                ImageView imageView = (ImageView) yourActivity.findViewById(imageViewId);
                imageView.setImageBitmap(bm);
                imageToDisk(bm, imageName, yourActivity, index);
                //addBitmapToMemoryCache(imageName, bm);
                Cache.getInstance().getLru().put(imageName, bm);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("problem: ","can't load image");
            }
        });
    }

    static void imageToDisk(Bitmap bm, String imageName, final Activity yourActivity, String index){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String pic1 = Base64.encodeToString(byteArray, Base64.DEFAULT);

        //save image to local database
        SharedPreferences settings = yourActivity.getSharedPreferences("MY_DATA", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("IM1" + index, pic1);
        editor.putString("IM1 " + index + " " + imageName, imageName);
        //editor.putString("PicName1", imageName);
        editor.commit();
    }


}
