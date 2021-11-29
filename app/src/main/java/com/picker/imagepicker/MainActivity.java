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
                Toast.makeText(MainActivity.this,"Button Clicked",Toast.LENGTH_LONG).show();

                Intent takePictureIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                try {
                    MainActivityKt.access$setPhotoFile$p(MainActivity.this.getPhotoFile("photo.jpg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Uri fileProvider = FileProvider.getUriForFile((Context)MainActivity.this, "edu.stanford.pandey.fileprovide", MainActivityKt.access$getPhotoFile$p());
                takePictureIntent.putExtra("output", (Parcelable)fileProvider);


                // if (takePictureIntent.resolveActivity(DocumentVerificationActivity.this.getPackageManager()) != null) {
                MainActivity.this.startActivityForResult(takePictureIntent, 42);
                /*} else {
                    Toast.makeText((Context)DocumentVerificationActivity.this, (CharSequence)"Unable to open camera", Toast.LENGTH_LONG).show();
                }*/



            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       // super.onActivityResult(requestCode, resultCode, data);

        Log.d("getTemp==","getcode");
        Log.d("getTemp==",resultCode+"  v");
        Log.d("getTemp==",requestCode+"  vgg");
        if (requestCode == 42 && resultCode == -1) {
            takenImage = BitmapFactory.decodeFile(MainActivityKt.access$getPhotoFile$p().getAbsolutePath());

            Log.d("getTemp==","fist");

            img_show.setImageBitmap(takenImage);
            final Uri tempUri = getImageUri(MainActivity.this, takenImage);
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            takenImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byteArray = stream.toByteArray();


            Handler handler = new Handler();
            Log.d("getTemp==","second");
            handler.postDelayed(new Runnable() {
                public void run() {
                    Log.d("getTemp==","third");
                   // UploadImage(tempUri);
                    Log.d("getTemp==",tempUri+"");

                }
            }, 2000);
            Log.d("getTemp==","fourth");
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            Log.d("getTemp==","fifth");
        }


    }


    public static final class MainActivityKt {
        private static final String FILE_NAME = "photo.jpg";
        private static final int REQUEST_CODE = 42;
        private static File photoFile;

        // $FF: synthetic method
        public static final File access$getPhotoFile$p() {
            File var10000 = photoFile;
            if (var10000 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("photoFile");
            }

            return var10000;
        }

        // $FF: synthetic method
        public static final void access$setPhotoFile$p(File var0) {
            photoFile = var0;
        }
    }



    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        // inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        //153 204
        Log.e("Dimensions", inImage.getWidth() + " " + inImage.getHeight());
        // /storage/emulated/0/DCIM/Camera/temp/IMG20210409150148.jpg
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        Log.e("getpath===",Uri.parse(path)+"");
        return Uri.parse(path);
    }


    private final File getPhotoFile(String fileName) throws IOException {
        File storageDirectory = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File var10000 = File.createTempFile(fileName, ".jpg", storageDirectory);
        Intrinsics.checkExpressionValueIsNotNull(var10000, "File.createTempFile(file…\".jpg\", storageDirectory)");
        return var10000;
    }


    private void showCustomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_select);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        // ((TextView) dialog.findViewById(R.id.title)).setText(p.name);
        // ((CircleImageView) dialog.findViewById(R.id.image)).setImageResource(p.image);

        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        ((AppCompatButton) dialog.findViewById(R.id.bt_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);*/


            }
        });

        ((AppCompatButton) dialog.findViewById(R.id.bt_login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);*/
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }




}