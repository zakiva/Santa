package com.example.zakiva.santa.Models;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.example.zakiva.santa.DrawingGame;
import com.example.zakiva.santa.Helpers.Cache;
import com.example.zakiva.santa.Loader;
import com.example.zakiva.santa.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

/**
 * Created by Ariel on 8/28/2016.
 */
public class Images {

    /*
    Use this method to update an image. The arguments are:
    imagename - the name of the image
    imageViewId - the integer that represents the imageView ID. The format is: [R.id.imageView]
    yourActivity - the activity that the imageView is in, The format is: [MainActivity.this]
    index - this helps the caching mechanism to search for the image. Each time you should call this method with a different index. The format is: ["1"]
    */
    public static void updateImage(final String imageName, final int imageViewId, final Activity yourActivity, final String index) {
        ImageView image = (ImageView) yourActivity.findViewById(imageViewId);
        Bitmap bitmap = (Bitmap) Cache.getInstance().getLru().get(imageName);
        if (bitmap != null) {
            image.setImageBitmap(bitmap);
            Log.d("Load from: ", "RAM");
        } else {
            SharedPreferences settings = yourActivity.getSharedPreferences("MY_DATA", 0);
            String Pic = settings.getString("IM1" + index, "NONE");
            String name = settings.getString("IM1 " + index + " " + "imageName", "NONE");
            if (!Pic.equals("NONE") && name.equals(imageName)) {
                byte[] imageAsBytes = Base64.decode(Pic.getBytes(), Base64.DEFAULT);
                Bitmap bm = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                image.setImageBitmap(bm);
                Cache.getInstance().getLru().put(imageName, bm);
                Log.d("Load from: ", "DISK");
            } else {
                downloadAndUpdateImage(imageName, imageViewId, yourActivity, index);
                Log.d("Load from: ", "WEB");
            }
        }
    }

    /*
    Use this method to update 2 images on the same time. The arguments are:
    imagename - the name of the image
    imageViewId - the integer that represents the imageView ID. The format is: [R.id.imageView]
    yourActivity - the activity that the imageView is in, The format is: [MainActivity.this]
    index - this helps the caching mechanism to search for the image. Each time you should call this method with a different index. The format is: ["1"]
    */
    public static void update2Images(final String imageName, final int imageViewId, final String imageName2, final int imageViewId2, final Activity yourActivity, final String index, final String index2) {
        ImageView image = (ImageView) yourActivity.findViewById(imageViewId);
        Bitmap bitmap = (Bitmap) Cache.getInstance().getLru().get(imageName);
        ImageView image2 = (ImageView) yourActivity.findViewById(imageViewId2);
        Bitmap bitmap2 = (Bitmap) Cache.getInstance().getLru().get(imageName2);
        if (bitmap != null && bitmap2 != null) {
            image.setImageBitmap(bitmap);
            image2.setImageBitmap(bitmap2);
            Log.d("Load from: ", "RAM");
        } else {
            SharedPreferences settings = yourActivity.getSharedPreferences("MY_DATA", 0);
            String Pic = settings.getString("IM1" + index, "NONE");
            String name = settings.getString("IM1 " + index + " " + "imageName", "NONE");
            String Pic2 = settings.getString("IM1" + index2, "NONE");
            String name2 = settings.getString("IM1 " + index2 + " " + "imageName", "NONE");
            if ((!Pic.equals("NONE") && name.equals(imageName)) && (!Pic2.equals("NONE") && name2.equals(imageName2))) {
                byte[] imageAsBytes = Base64.decode(Pic.getBytes(), Base64.DEFAULT);
                Bitmap bm = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                byte[] imageAsBytes2 = Base64.decode(Pic2.getBytes(), Base64.DEFAULT);
                Bitmap bm2 = BitmapFactory.decodeByteArray(imageAsBytes2, 0, imageAsBytes2.length);
                image.setImageBitmap(bm);
                image2.setImageBitmap(bm2);
                Cache.getInstance().getLru().put(imageName, bm);
                Cache.getInstance().getLru().put(imageName2, bm2);
                Log.d("Load from: ", "DISK");
            } else {
                downloadAndUpdate2Images(imageName, imageViewId, imageName2, imageViewId2, yourActivity, index, index2);
                Log.d("Load from: ", "WEB");
            }
        }
    }

    /*
    Another option to update image. Same args as "updateImage".
    This method do the same thing as "updateImage". Use both and check which one perform better and use it.
    */
    public static void updateImageWithUrl(final String imageName, final int imageViewId, final Activity yourActivity, final String index){
        SharedPreferences settings = yourActivity.getSharedPreferences("MY_DATA", 0);
        String url = settings.getString("IM2" + " " + index, "NONE");
        String name = settings.getString("IM2" + " name " + index, "NONE");
        if (!url.equals("NONE") && imageName.equals(name)){
            ImageView image = (ImageView) yourActivity.findViewById(imageViewId);
            Picasso.with(yourActivity).load(url).into(image);
        } else {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl(yourActivity.getString(R.string.firebase_storage));
            storageRef.child(imageName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    ImageView image = (ImageView) yourActivity.findViewById(imageViewId);
                    Picasso.with(yourActivity).load(uri).into(image);

                    SharedPreferences settings = yourActivity.getSharedPreferences("MY_DATA", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("IM2" + " " + index, uri.toString());
                    editor.putString("IM2" + " name " + index, imageName);
                    editor.commit();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }
    }

    static void downloadAndUpdateImage(final String imageName, final int imageViewId, final Activity yourActivity, final String index) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(yourActivity.getString(R.string.firebase_storage));
        storageRef.child(imageName).getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                ImageView imageView = (ImageView) yourActivity.findViewById(imageViewId);
                imageView.setImageBitmap(bm);
                imageToDisk(bm, imageName, yourActivity, index);
                Cache.getInstance().getLru().put(imageName, bm);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("problem: ", "can't load image");
            }
        });
    }

    static void imageToDisk(Bitmap bm, String imageName, final Activity yourActivity, String index) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String pic1 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        //save image to local database
        SharedPreferences settings = yourActivity.getSharedPreferences("MY_DATA", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("IM1" + index, pic1);
        editor.putString("IM1 " + index + " " + "imageName", imageName);
        editor.commit();
    }

    static void downloadAndUpdate2Images(final String imageName, final int imageViewId, final String imageName2, final int imageViewId2, final Activity yourActivity, final String index, final String index2) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReferenceFromUrl(yourActivity.getString(R.string.firebase_storage));
        storageRef.child(imageName).getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                final ImageView imageView = (ImageView) yourActivity.findViewById(imageViewId);
                storageRef.child(imageName2).getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes2) {
                        Bitmap bm2 = BitmapFactory.decodeByteArray(bytes2, 0, bytes2.length);
                        ImageView imageView2 = (ImageView) yourActivity.findViewById(imageViewId2);
                        imageView.setImageBitmap(bm);
                        imageView2.setImageBitmap(bm2);
                        imageToDisk(bm, imageName, yourActivity, index);
                        imageToDisk(bm2, imageName2, yourActivity, index2);
                        Cache.getInstance().getLru().put(imageName, bm);
                        Cache.getInstance().getLru().put(imageName2, bm2);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d("problem: ", "can't load image");
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("problem: ", "can't load image");
            }
        });
    }

    // Use this method like this:
    // Images.downloadImageToDisk("drawing1.jpg", getApplicationContext());
    public static void downloadImageToDisk(final String imageName, final Context context) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(context.getString(R.string.firebase_storage));
        storageRef.child(imageName).getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                try {
                    DB snappydb = DBFactory.open(context);
                    snappydb.put(imageName, bytes);
                    snappydb.close();
                    DrawingGame.imagesOnDisk.add(imageName);
                    // Use next line only if you use this method in Loader activity
                    Loader.increase();
                } catch (SnappydbException e) {
                    Log.d("Problem: ", e.toString());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("problem: ", "can't load image");
            }
        });
    }

    // Use this method like this:
    // Images.getBitmapFromDisk("drawing1.jpg", getApplicationContext());
    public static Bitmap getBitmapFromDisk(final String imageName, final Context context){
        try {
            DB snappydb = DBFactory.open(context);
            if (snappydb.exists(imageName)){
                byte[] bytes  =  snappydb.getBytes(imageName);
                snappydb.close();
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                return bm;
            }
        } catch (SnappydbException e) {
            Log.d("Problem: ", e.toString());
        }
        return null;
    }
    
    // Use this method like this:
    // Images.deleteImageFromDisk("drawing1.jpg", getApplicationContext());
    public static void deleteImageFromDisk(final String imageName, final Context context){
        try {
            DB snappydb = DBFactory.open(context);
            snappydb.del(imageName);
            snappydb.close();
            DrawingGame.imagesOnDisk.remove(imageName);
        } catch (SnappydbException e) {
            Log.d("Problem: ", e.toString());
        }
    }

    // Use this method like this:
    // Images.isImageInDisk("drawing1.jpg", getApplicationContext());
    public static boolean isImageInDisk(final String imageName, final Context context){
        boolean isKeyExist = false;
        try {
            DB snappydb = DBFactory.open(context);
            isKeyExist = snappydb.exists(imageName);
            snappydb.close();
        } catch (SnappydbException e) {
            Log.d("Problem: ", e.toString());
        }
        return isKeyExist;
    }
}