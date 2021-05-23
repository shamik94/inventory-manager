package com.meaty.seller.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.meaty.seller.R;
import com.meaty.seller.model.firebase.SellerProfile;


import java.io.ByteArrayOutputStream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SellerProfileActivity extends AppCompatActivity {

    private static final int PHOTO_REQUEST_CODE = 1;
    private static final int GOVT_ID_REQUEST_CODE = 2;
    private ImageView ivPhoto;
    private ImageView ivGovtId;
    private Button bSubmit;
    private Bitmap photoImage;
    private Bitmap govtIdImage;
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etAddress;
    private EditText etPhoneNumber;
    private EditText etGovtId;
    private FirebaseFirestore database;
    private FirebaseStorage storage;
    private static final String USER_UID = "userUID";
    private static final String USER_SHARED_PREFERENCES = "userPreferences";
    private static final String UID_SHARED_PREFERENCES = "uid";
    private static final String PHONE_SHARED_PREFERENCES = "phone";
    private static final String SELLER_COLLECTION_NAME = "seller";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_profile);
        setup();
    }

    private String getSellerId() {
        Bundle bundle = getIntent().getExtras();
        return bundle.getString(USER_UID);
    }

    private void askForCameraPermission(Integer REQUEST_CODE) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE);
        } else {
            openCamera(REQUEST_CODE);
        }
    }

    private void openCamera(Integer REQUEST_CODE) {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, REQUEST_CODE);
    }

    private void setup() {
        storage = FirebaseStorage.getInstance();
        database = FirebaseFirestore.getInstance();

        ivPhoto = findViewById(R.id.ivPhoto);
        ivGovtId = findViewById(R.id.ivGovtId);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etAddress = findViewById(R.id.etAddress);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etAddress = findViewById(R.id.etAddress);
        etGovtId = findViewById(R.id.etGovtIdNo);
        bSubmit = findViewById(R.id.bSubmit);

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });

        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askForCameraPermission(PHOTO_REQUEST_CODE);
            }
        });

        ivGovtId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askForCameraPermission(GOVT_ID_REQUEST_CODE);
            }
        });
    }

    public void submit() {
        // Upload to server on submit

        String firstName, lastName, address, phoneNumber, govtId;
        firstName = etFirstName.getText().toString();
        lastName = etLastName.getText().toString();
        address = etAddress.getText().toString();
        phoneNumber = etPhoneNumber.getText().toString();
        govtId = etGovtId.getText().toString();

        if (firstName.isEmpty()
                || lastName.isEmpty()
                || address.isEmpty()
                || phoneNumber.isEmpty()
                || govtId.isEmpty()
                || photoImage == null
                || govtIdImage == null) {
            showMessage("Fill all the fields");
            return;
        }

        saveDataToServer(firstName, lastName, address, phoneNumber, govtId, photoImage, govtIdImage);
    }

    private void saveDataToServer(String firstName, String lastName, String address, String phoneNumber, String govtId, Bitmap photoImage, Bitmap govtIdImage) {
        String photoName = firstName + "." + lastName + "_" + phoneNumber  + ".jpg";

        StorageReference storageRef = storage.getReference();
        StorageReference sellerPhotoReference = storageRef.child("seller_photo/" + photoName);
        StorageReference sellerGovtIdProofReference = storageRef.child("seller_id_proof/" + photoName);

        ByteArrayOutputStream photoBaos = new ByteArrayOutputStream();
        ByteArrayOutputStream govtIdBaos = new ByteArrayOutputStream();
        photoImage.compress(Bitmap.CompressFormat.JPEG, 100, photoBaos);
        govtIdImage.compress(Bitmap.CompressFormat.JPEG, 100, govtIdBaos);
        byte[] photoImageByteArray = photoBaos.toByteArray();
        byte[] govtIdImageByteArray = govtIdBaos.toByteArray();

        uploadPhoto(sellerPhotoReference, photoImageByteArray);
        uploadPhoto(sellerGovtIdProofReference, govtIdImageByteArray);
        SellerProfile sellerProfile = new SellerProfile(
                firstName,
                lastName,
                address,
                phoneNumber,
                govtId,
                "seller_photo/" + photoName,
                "seller_id_proof/" + photoName
        );
        updateDataToDB(sellerProfile);
    }

    private void updateDataToDB(final SellerProfile sellerProfile) {
        database.collection("seller")
                .document(getSellerId())
                .set(sellerProfile)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showMessage("Username Created Successfully");
                        Intent intent = new Intent(SellerProfileActivity.this, LoginActivity.class);
                        SellerProfile seller = new SellerProfile();
                        seller.setSellerId(getSellerId());
                        seller.setPhoneNumber(sellerProfile.getPhoneNumber());
                        saveUserPreferences(seller);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage("Registration Failed");
                    }
                });
    }

    private void uploadPhoto(StorageReference reference, byte[] imageArray) {
        UploadTask uploadTask = reference.putBytes(imageArray);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                showMessage("Registration failed, please try again");
                exception.printStackTrace();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_REQUEST_CODE) {
            photoImage = (Bitmap) data.getExtras().get("data");
            ivPhoto.setImageBitmap(photoImage);
        }
        if (requestCode == GOVT_ID_REQUEST_CODE) {
            govtIdImage = (Bitmap) data.getExtras().get("data");
            ivGovtId.setImageBitmap(govtIdImage);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PHOTO_REQUEST_CODE || requestCode == GOVT_ID_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //openCamera();
            } else {
                showMessage("Camera Permission is Required to upload image");
            }
        }
    }

    private void showMessage(String message) {
        Toast.makeText(SellerProfileActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void saveUserPreferences(SellerProfile seller) {
        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(USER_SHARED_PREFERENCES, Context.MODE_PRIVATE).edit();
        editor.putString(UID_SHARED_PREFERENCES, seller.getSellerId());
        editor.putString(PHONE_SHARED_PREFERENCES, seller.getPhoneNumber());
        editor.apply();
    }

}
