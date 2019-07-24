package com.example.checfsmenu;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ImageActivity extends AppCompatActivity implements Serializable{

    // Views
    private Button btn;
    private ImageView imageview;
    private Button back;
    private Spinner spinner;

    // Lists
    private List<Bitmap> imageFiles;
    private ArrayAdapter<String> dataAdapterForFood;
    private ArrayList<Food> foodObjectList;

    // Immutable variable
    protected static final String IMAGE_DIRECTORY = "/demonuts";

    // Mutable variables
    Bitmap selectedImage;
    int imageIndex;
    int itemPosition;
    private String selecteditem;
    private int GALLERY = 1, CAMERA = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        initActivity();
        setActions();

        requestMultiplePermissions();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });

    }

   // @Override
  /*  protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }*/

    private void initActivity() {
        btn = (Button) findViewById(R.id.btn);
        imageview = (ImageView) findViewById(R.id.iv);
        back=(Button)findViewById(R.id.back);
        spinner=(Spinner)findViewById(R.id.spinner);

       // this.imageFiles = new ArrayList<>();
        this.foodObjectList = setFoodListFromString(getSharedList("task list"));
    }

    private ArrayList<String> getSharedList(String key) {
        ArrayList<String> sharedFoodList;

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key,null);
        Type type = new TypeToken<ArrayList<String>>(){}.getType();

        sharedFoodList = gson.fromJson(json,type);

        return sharedFoodList;
    }

    private ArrayList<Food> setFoodListFromString(ArrayList<String> nameList) {
        ArrayList<Food> foodObjectList = new ArrayList<>();

        if (nameList != null) {
            for (String name: nameList) {
                Food newFood = new Food(name);
                foodObjectList.add(newFood);
            }
        }

        return foodObjectList;
    }

    private void setActions() {
      //  setSelectButtonAction();
        setBackButtonAction();
        setSpinnerAction(getSharedList("task list"));
    }

  /*  private void setSelectButtonAction() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = search(selecteditem);
                if (index != -1) {

                    //imageIndex=imageFiles.indexOf(bitmap);
                    foodObjectList.get(index).setImage(imageFiles.get(imageIndex));

                    imageview.setImageBitmap(foodObjectList.get(index).getImage());
                    //TODO: search in foodObjectList
                } else {
                    showPictureDialog();
                }
            }
        });
    }*/

    private void setBackButtonAction() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ImageActivity.this,MainPage.class);
                intent.putExtra("imageFiles", (Serializable) imageFiles);
                startActivity(intent);

            }
        });
    }

    private void setSpinnerAction(final ArrayList<String> foodNameList) {
        // Adapter setting to spinner
        dataAdapterForFood = new ArrayAdapter<String>(ImageActivity.this, android.R.layout.simple_spinner_dropdown_item,foodNameList);
        dataAdapterForFood.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapterForFood);

        // Listener for spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selecteditem = dataAdapterForFood.getItem(position);

              /*  if (selecteditem == null) {
                    Toast.makeText(ImageActivity.this, "Please select a food!!", Toast.LENGTH_SHORT).show();
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(), "Please select a food!!", Toast.LENGTH_SHORT).show();
            }
        });


    }

   /* public int search(String selecteditem){
        int index = 0;

        for (Food f: this.foodObjectList) {
            if (f.getName().equals(selecteditem)) {
                return index;
            }

            index++;
        }

        return -1;
    }*/
    private void  requestMultiplePermissions(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    this.selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage(selectedImage);
                    Toast.makeText(ImageActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    imageview.setImageBitmap(selectedImage);
                    //foodObjectList.add(new Food(selecteditem,bitmap));

                   // imageFiles.add(selectedImage);
                    //imageIndex=imageFiles.indexOf(selectedImage);


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(ImageActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                imageview.setImageBitmap(thumbnail);
              // foodObjectList.add(new Food(selecteditem,thumbnail));
               // imageFiles.add(thumbnail);
               // imageIndex=imageFiles.indexOf(thumbnail);

            saveImage(thumbnail);
            Toast.makeText(ImageActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

}
