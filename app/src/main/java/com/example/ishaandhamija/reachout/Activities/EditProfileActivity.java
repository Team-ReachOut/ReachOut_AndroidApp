package com.example.ishaandhamija.reachout.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.ishaandhamija.reachout.Activities.SignUpActivity.REQ_CODE;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener  {

    public static final String TAG = "EditProfileActivity";

    EditText name, age, bloodgroup, address, contactno, email, password;
    Button btn_save;
    CircleImageView profilePic;
    RadioGroup radioGroup;
    RadioButton radiobtnSex;
    int selectedSexId;
    FloatingActionButton fab;
    String encodedImage = null;
    ProgressDialog progressDialog;
    CoordinatorLayout coordinatorLayout;
    ScrollView scrollView;

    public static final Integer INTENT_REQUEST_GET_IMAGES = 1001;
    public static final Integer REQUEST_CAMERA = 10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
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
        fab = (FloatingActionButton) findViewById(R.id.fab);
        progressDialog = new ProgressDialog(this);
        scrollView = (ScrollView) findViewById(R.id.scrollView);



        btn_save.setOnClickListener(this);
        fab.setOnClickListener(this);

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
                .error(R.drawable.nopicc)
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
            selectedSexId = radioGroup.getCheckedRadioButtonId();
            radiobtnSex = (RadioButton) findViewById(selectedSexId);


            if(validateFields()){
                progressDialog.setMessage("Saving Changes...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                saveChangedInfo();
            }

        }
        if (view == fab){
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
                    int cameraPerm = ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.CAMERA);
                    if (cameraPerm != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{
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

        }

        super.onActivityResult(requestCode, resultCode, intent);
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



        JSONObject json = new JSONObject();
        try {
            json.put("name", name.getText().toString());
            json.put("bloodgroup", bloodgroup.getText().toString());
            json.put("email", email.getText().toString());
            json.put("phonenumber", contactno.getText().toString());
            json.put("address", address.getText().toString());
            json.put("age", age.getText().toString());
            json.put("sex",((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString());
            json.put("password", password.getText().toString());
            json.put("imageString", encodedImage);
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
                            progressDialog.dismiss();
                            Toast.makeText(EditProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(EditProfileActivity.this,DashboardActivity.class);
                            startActivity(i);
                        } catch (Exception e) {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Log.d(TAG, "onErrorResponse: " + error);
                        Toast.makeText(EditProfileActivity.this, "Failed to make changes!!", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                });
        requestQueue.add(jsonObjectRequest);

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

        if (profilePic.getDrawable().getConstantState() == getResources().getDrawable( R.drawable.nopicc).getConstantState()){
            Snackbar snackbar  = Snackbar.make(coordinatorLayout,"Please upload your photograph!",Snackbar.LENGTH_LONG);
            snackbar.show();
            scrollView.fullScroll(ScrollView.FOCUS_UP);
            return false;
        }

        return true;
    }
}
