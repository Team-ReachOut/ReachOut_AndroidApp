package com.example.ishaandhamija.reachout.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.desmond.squarecamera.CameraActivity;
import com.example.ishaandhamija.reachout.R;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class SignUpActivity extends AppCompatActivity {

    EditText name, age, sex, bloodgroup, address, contactno, email, password;
    Button sign_up_button, btn_login;
    FloatingActionButton getPic;
    RadioGroup radioGroup;
    RadioButton radiobtnSex;
    ImageView userImage;
    int selectedSexId;
    String encodedImage = null;
    CoordinatorLayout coordinatorLayout;
    ScrollView scrollView;
    ProgressDialog progressDialog;

    public static final String TAG = "volleyError";
    public static final Integer REQ_CODE = 101;
    public static final Integer INTENT_REQUEST_GET_IMAGES = 1001;
    public static final Integer REQUEST_CAMERA = 10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        name = (EditText) findViewById(R.id.name);
        age = (EditText) findViewById(R.id.age);

        radioGroup = (RadioGroup) findViewById(R.id.radioSex);
        bloodgroup = (EditText) findViewById(R.id.bloodgroup);
        address = (EditText) findViewById(R.id.address);
        contactno = (EditText) findViewById(R.id.contactno);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        sign_up_button = (Button) findViewById(R.id.sign_up_button);
        btn_login = (Button) findViewById(R.id.sign_in_button);

        getPic = (FloatingActionButton) findViewById(R.id.getPic);
        userImage = (ImageView) findViewById(R.id.userImage);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        progressDialog = new ProgressDialog(this);

        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validateFields()) {
                    progressDialog.setMessage("Registering...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    fetchJson();
                }
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });

        getPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImages();
            }
        });

    }

    private void fetchJson() {

        JSONObject json = new JSONObject();
        try {
            json.put("name", name.getText().toString());
            json.put("bloodgroup", bloodgroup.getText().toString());
            json.put("email", email.getText().toString());
            json.put("phonenumber", contactno.getText().toString());
            json.put("address", address.getText().toString());
            json.put("age", age.getText().toString());
            json.put("sex",radiobtnSex.getText().toString());
            json.put("password", password.getText().toString());
            json.put("imageString", encodedImage);
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://192.168.43.202:5199/api/addone",
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://reach-out-server.herokuapp.com/api/addone",
                json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Toast.makeText(SignUpActivity.this, "Signed Up", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onResponse: ");
                            progressDialog.dismiss();
                            Intent i = new Intent(SignUpActivity.this,LoginActivity.class);
                            startActivity(i);
                            Toast.makeText(SignUpActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(SignUpActivity.this, "Dikkat", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(SignUpActivity.this, "Failed to Register!!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onErrorResponse: " + error);
                        error.printStackTrace();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(SignUpActivity.this);
        requestQueue.add(jsonObjectRequest);
    }

    private void getImages() {

        CharSequence cameraOptions[] = new CharSequence[] {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an option");
        builder.setItems(cameraOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    int cameraPerm = ContextCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.CAMERA);
                    if (cameraPerm != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(SignUpActivity.this, new String[]{
                                Manifest.permission.CAMERA
                        }, REQ_CODE);
                    }
                    else {
                        takeFromCamera();
                    }
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

    private boolean validateFields() {

        selectedSexId = radioGroup.getCheckedRadioButtonId();
        radiobtnSex = (RadioButton) findViewById(selectedSexId);

        String blood = bloodgroup.getText().toString();
        String pwd = password.getText().toString();
        String number = contactno.getText().toString();
        String emailId = email.getText().toString();

        if(TextUtils.isEmpty(name.getText().toString())){

            Snackbar snackbar  = Snackbar.make(coordinatorLayout,"Please enter the name!",Snackbar.LENGTH_LONG);
            name.requestFocus();
            snackbar.show();
            return false;
        }
        if(TextUtils.isEmpty(age.getText().toString())){

            Snackbar snackbar  = Snackbar.make(coordinatorLayout,"Please enter the age!",Snackbar.LENGTH_LONG);
            age.requestFocus();
            snackbar.show();
            return false;
        }

        if(TextUtils.isEmpty(bloodgroup.getText().toString())){
            Snackbar snackbar  = Snackbar.make(coordinatorLayout,"Please enter the bloodgroup!",Snackbar.LENGTH_LONG);
            bloodgroup.requestFocus();
            snackbar.show();
            return false;
        }

        if(!(blood.equals("A+") || blood.equals("A-") || blood.equals("B+") || blood.equals("B-") || blood.equals("AB+")
                || blood.equals("AB-") || blood.equals("O+") || blood.equals("O-") )){
            bloodgroup.requestFocus();
            bloodgroup.setError("Enter valid bloodgroup");
            return false;
        }

        if (radiobtnSex == null){
            Snackbar snackbar  = Snackbar.make(coordinatorLayout,"Please enter the sex!",Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }

        if(TextUtils.isEmpty(address.getText().toString())){
            Snackbar snackbar  = Snackbar.make(coordinatorLayout,"Please enter the address!",Snackbar.LENGTH_LONG);
            address.requestFocus();
            snackbar.show();
            return false;
        }
        if(TextUtils.isEmpty(contactno.getText().toString())){
            Snackbar snackbar  = Snackbar.make(coordinatorLayout,"Please enter the Contact Number!",Snackbar.LENGTH_LONG);
            contactno.requestFocus();
            snackbar.show();
            return false;
        }
        if (number.length() != 10){
            contactno.requestFocus();
            contactno.setError("Please enter a valid 10 digit no.");
            return false;

        }
        if(TextUtils.isEmpty(email.getText().toString())){
            Snackbar snackbar  = Snackbar.make(coordinatorLayout,"Please enter the email!",Snackbar.LENGTH_LONG);
            email.requestFocus();
            snackbar.show();
            return false;
        }
        if(emailId.indexOf('@') == -1){
            email.requestFocus();
            email.setError("Please enter a valid email");
            return false;
        }
        if(TextUtils.isEmpty(password.getText().toString())){
            Snackbar snackbar  = Snackbar.make(coordinatorLayout,"Please enter the password!",Snackbar.LENGTH_LONG);
            password.requestFocus();
            snackbar.show();
            return false;
        }
        if(pwd.length() < 6 ){
            password.requestFocus();
            password.setError("Password too short");
            return  false;
        }

        if (encodedImage == null){
            Snackbar snackbar  = Snackbar.make(coordinatorLayout,"Please upload your photograph!",Snackbar.LENGTH_LONG);
            snackbar.show();
            scrollView.fullScroll(ScrollView.FOCUS_UP);
            return false;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resuleCode, Intent intent) {

        if(requestCode==INTENT_REQUEST_GET_IMAGES && resuleCode==RESULT_OK)
        {
            Uri image = intent.getData();
            CropImage.activity(image)
                    .setGuidelines(com.theartofdev.edmodo.cropper.CropImageView.Guidelines.ON)
                    .setAspectRatio(100,100)
                    .start(SignUpActivity.this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(intent);
            if (resuleCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                userImage.setImageURI(resultUri);
                InputStream imageStream = null;
                try {
                    imageStream = getContentResolver().openInputStream(resultUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                encodedImage = encodeImage(selectedImage);
                Log.d("base64String", "onActivityResult: " + encodedImage);
            } else if (resuleCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

        if (resuleCode != RESULT_OK) return;

        if (requestCode == REQUEST_CAMERA) {
            Uri photoUri = intent.getData();
            CropImage.activity(photoUri)
                    .setGuidelines(com.theartofdev.edmodo.cropper.CropImageView.Guidelines.ON)
                    .setAspectRatio(100,100)
                    .start(SignUpActivity.this);
        }

        super.onActivityResult(requestCode, resuleCode, intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQ_CODE) {
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, "Permission Not Given", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
            }
            takeFromCamera();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void takeFromCamera(){
        Intent startCustomCameraIntent = new Intent(this, CameraActivity.class);
        startActivityForResult(startCustomCameraIntent, REQUEST_CAMERA);
    }

    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }
}
