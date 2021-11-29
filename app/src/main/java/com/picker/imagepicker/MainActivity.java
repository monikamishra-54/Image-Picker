package com.picker.imagepicker;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import kotlin.jvm.internal.Intrinsics;

public class MainActivity extends AppCompatActivity {
    Button btn_picker;
    ImageView img_show;
    Bitmap takenImage;
    byte[] byteArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img_show=findViewById(R.id.img_show);
        btn_picker=findViewById(R.id.btn_picker);

        btn_picker.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                Intent takePictureIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                try {
                    ImagePickT.access$setPhotoFile$p(MainActivity.this.getPhotoFile("photo.jpg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Uri fileProvider = FileProvider.getUriForFile((Context)MainActivity.this, "edu.stanford.pandey.fileprovide", ImagePickT.access$getPhotoFile$p());
                takePictureIntent.putExtra("output", (Parcelable)fileProvider);
                MainActivity.this.startActivityForResult(takePictureIntent, 42);

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 42 && resultCode == -1) {
            takenImage = BitmapFactory.decodeFile(ImagePickT.access$getPhotoFile$p().getAbsolutePath());

            img_show.setImageBitmap(takenImage);
            final Uri tempUri = getImageUri(MainActivity.this, takenImage);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            takenImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byteArray = stream.toByteArray();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    UploadImage(tempUri);


                }
            }, 2000);

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }


    }

    private void UploadImage(Uri tempUri) {

        //upload image to server

    }


    public static final class ImagePickT {
        private static File photoFile;

        // $FF: synthetic method
        public static final File access$getPhotoFile$p() {
            File var10000 = photoFile;
            if (var10000 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("photoFile");
            }

            return var10000;
        }

        public static final void access$setPhotoFile$p(File var0) {
            photoFile = var0;
        }
    }



    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        Log.e("Dimensions", inImage.getWidth() + " " + inImage.getHeight());
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    private final File getPhotoFile(String fileName) throws IOException {
        File storageDirectory = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File var10000 = File.createTempFile(fileName, ".jpg", storageDirectory);
        Intrinsics.checkExpressionValueIsNotNull(var10000, "File.createTempFile(file…\".jpg\", storageDirectory)");
        return var10000;
    }

}