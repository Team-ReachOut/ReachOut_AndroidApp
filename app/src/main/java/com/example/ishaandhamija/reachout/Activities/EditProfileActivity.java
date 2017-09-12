package com.example.ishaandhamija.reachout.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.desmond.squarecamera.CameraActivity;
import com.example.ishaandhamija.reachout.R;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener  {

    EditText name, age, bloodgroup, address, contactno, email, password;
    Button btn_save;
    CircleImageView profilePic;
    RadioGroup radioGroup;
    RadioButton radioButton;
    LinearLayout ppLayout;
    String encodedImage = null;

    public static final Integer INTENT_REQUEST_GET_IMAGES = 1001;
    public static final Integer REQUEST_CAMERA = 10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profilePic = (CircleImageView) findViewById(R.id.profilePic);
        name = (EditText) findViewById(R.id.name);
        age = (EditText) findViewById(R.id.age);
        radioGroup = (RadioGroup) findViewById(R.id.radioSex);
        bloodgroup = (EditText) findViewById(R.id.bloodgroup);
        address = (EditText) findViewById(R.id.address);
        contactno = (EditText) findViewById(R.id.contactno);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        btn_save = (Button) findViewById(R.id.save);
        ppLayout = (LinearLayout) findViewById(R.id.ppLayout);

        btn_save.setOnClickListener(this);
        ppLayout.setOnClickListener(this);

        showProfile();

    }

    private void showProfile() {

        Intent i = getIntent();
        String uname = i.getStringExtra("name");
        String uage = i.getStringExtra("age");
        String ubloodgroup = i.getStringExtra("bloodgroup");
        String uaddress = i.getStringExtra("address");
        String ucontactno = i.getStringExtra("contactno");
        String uemail = i.getStringExtra("email");
        String upassword = i.getStringExtra("password");
        String usex = i.getStringExtra("sex");
        String imageUrl = "http://192.168.43.202:5199/api/uploads/"+uemail+".jpg";

        Picasso
                .with(this)
                .load(imageUrl)
                .placeholder(R.drawable.nopicc)
                .error(R.mipmap.ic_launcher)
                .into(profilePic);

        name.setText(uname);
        name.requestFocus();
        age.setText(uage);
        bloodgroup.setText(ubloodgroup);
        address.setText(uaddress);
        contactno.setText(ucontactno);
        email.setText(uemail);
        email.setEnabled(false);
        email.setTextColor(Color.GRAY);
        password.setText(upassword);

        if(usex.equals("Male")){
            radioGroup.check(R.id.male);
        }
        else if(usex.equals("Female")){
            radioGroup.check(R.id.female);
        }
        else{
            radioGroup.check(R.id.other);
        }


    }

    @Override
    public void onClick(View view) {

        if(view == btn_save){
            Toast.makeText(this, "Clicked!", Toast.LENGTH_SHORT).show();
            saveChangedInfo();
        }
        if(view == ppLayout){
            getImages();
        }

    }

    private void getImages() {

        CharSequence cameraOptions[] = new CharSequence[] {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an option");
        builder.setItems(cameraOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                        takeFromCamera();
                    }
                else if (which == 1){
                    Intent i=new Intent(Intent.ACTION_PICK);
                    i.setType("image/*");
                    startActivityForResult(i,INTENT_REQUEST_GET_IMAGES);
                }
            }
        });
        builder.show();
    }

    private void takeFromCamera(){
        Intent startCustomCameraIntent = new Intent(this, CameraActivity.class);
        startActivityForResult(startCustomCameraIntent, REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if(requestCode==INTENT_REQUEST_GET_IMAGES && resultCode==RESULT_OK)
        {
            Uri image = intent.getData();
            CropImage.activity(image)
                    .setGuidelines(com.theartofdev.edmodo.cropper.CropImageView.Guidelines.ON)
                    .setAspectRatio(100,100)
                    .start(EditProfileActivity.this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(intent);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                profilePic.setImageURI(resultUri);
                InputStream imageStream = null;
                try {
                    imageStream = getContentResolver().openInputStream(resultUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                encodedImage = encodeImage(selectedImage);
                Log.d("base64String", "onActivityResult: " + encodedImage);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

        if (resultCode != RESULT_OK) return;

        if (requestCode == REQUEST_CAMERA) {
            Uri photoUri = intent.getData();
            CropImage.activity(photoUri)
                    .setGuidelines(com.theartofdev.edmodo.cropper.CropImageView.Guidelines.ON)
                    .setAspectRatio(100,100)
                    .start(EditProfileActivity.this);
            saveChangedInfo();
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }

    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }


    private void saveChangedInfo() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());

        JSONObject json = new JSONObject();
        try {
            json.put("name", name.getText().toString());
            json.put("bloodgroup", bloodgroup.getText().toString());
            json.put("email", email.getText().toString());
            json.put("phonenumber", contactno.getText().toString());
            json.put("address", address.getText().toString());
            json.put("age", age.getText().toString());
            json.put("sex",radioButton.getText().toString());
            json.put("password", password.getText().toString());
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://192.168.43.202:5199/api/update",
//        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://harshgoyal.xyz:5199/api/update",
//        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://reach-out-server.herokuapp.com/api/addone",
                json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(EditProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        requestQueue.add(jsonObjectRequest);

    }
}
