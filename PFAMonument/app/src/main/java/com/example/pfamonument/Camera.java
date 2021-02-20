package com.example.pfamonument;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pfamonument.Model.Images;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class Camera extends Fragment implements LocationListener {

    private LocationManager locationManager;
    TextView latitudeTxt;
    TextView longitudeTxt;

    private String titre, descrip, downloadImageUrl;
    private double latit, longit;
    EditText mTitleEt, mDescrEt;
    ImageView imageView;
    Button btnUpload;

    String test = "false";

    //dossier path de firebase storage
    String mStoragePath = "pictures/";

    //Root nom database de firebase database
    String mDatabasePath = "Data";

    //créer URI
    Uri mFilePathUri;

    //créer StorageReference et Database reference
    private StorageReference mStorageReference;
    private DatabaseReference mDatabaseReference;
    private StorageTask mUploadTask;

    //ProgressDialog
    ProgressDialog mProgressDialog;

    //Image request code pour choisir l'image
    private static final int IMAGE_REQUEST_CODE = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.camera, container, false);

        latitudeTxt = (TextView) rootView.findViewById(R.id.latitude);
        longitudeTxt = (TextView) rootView.findViewById(R.id.longitude);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
        Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
        onLocationChanged(location);
        //*********************************************

        mTitleEt = (EditText) rootView.findViewById(R.id.pNomPlace);
        mDescrEt = (EditText) rootView.findViewById(R.id.pDescriptionPlace);
        imageView = (ImageView) rootView.findViewById(R.id.pImageView);
        btnUpload = (Button) rootView.findViewById(R.id.uploadBtn);

        //image click to choose image
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //startActivityForResult(intent,1);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                //startActivityForResult(Intent.createChooser(intent, "Select Image"), IMAGE_REQUEST_CODE);
                startActivityForResult(intent, IMAGE_REQUEST_CODE);
            }
        });

        //button click to upload data to firebase
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //methode to upload donnée à firebase
                uploadDataToFirebase();
            }
        });

        //assign FirebaseStorage instance to storage reference object
         mStorageReference = FirebaseStorage.getInstance().getReference().child("pictures");
        //assign FirebaseDatabase instance with root database name
         mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Data");

        //progress dialog
        mProgressDialog = new ProgressDialog(getActivity());

        return rootView;
    }

    private void uploadDataToFirebase() {

        titre = mTitleEt.getText().toString();
        descrip = mDescrEt.getText().toString();
        latit = Double.parseDouble(latitudeTxt.getText().toString());
        longit = Double.parseDouble(longitudeTxt.getText().toString());

        if(mFilePathUri == null)
        {
            Toast.makeText(getActivity(), "Svp prendre une photo avant !!", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(titre)){
            Toast.makeText(getActivity(), "Svp ... Entrer un titre !!", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(descrip)){
            Toast.makeText(getActivity(), "Svp ... Entrer une description !!", Toast.LENGTH_SHORT).show();
        }else {
            //afficher progress bar
            mProgressDialog.setTitle("Uploading ...");
            mProgressDialog.show();

            final StorageReference filePath = mStorageReference.child(mFilePathUri.getLastPathSegment()+ System.currentTimeMillis() + "." + getFileExtension(mFilePathUri));

            final UploadTask uploadTask = filePath.putFile(mFilePathUri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String message = e.toString();
                    Toast.makeText(getActivity(), "Erreur ..."+message, Toast.LENGTH_SHORT).show();

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getActivity(), "Uploaded ...", Toast.LENGTH_SHORT).show();
                    Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if(!task.isSuccessful())
                            {
                                throw task.getException();
                            }
                            downloadImageUrl = filePath.getDownloadUrl().toString();
                            return filePath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task)
                        {
                            if(task.isSuccessful())
                            {
                                downloadImageUrl = task.getResult().toString();

                                Toast.makeText(getActivity(), downloadImageUrl, Toast.LENGTH_LONG).show();
                                Toast.makeText(getActivity(), "récuperer image url ...", Toast.LENGTH_SHORT).show();

                                Images imageUploadedInfo = new Images(titre, downloadImageUrl, descrip, test, latit, longit);

                                //getting image upload id
                                String imageUploadId = mDatabaseReference.push().getKey();
                                //ajouter l'id de l'image telecharger à databaseReference
                                mDatabaseReference.child(imageUploadId).setValue(imageUploadedInfo);

                               mProgressDialog.dismiss();
                            }
                        }
                    });
                }
            });
        }
    }

    //methode pour récuperer l'image capturer depuis le file path uri
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        //return the file extension
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            mFilePathUri = data.getData();
            imageView.setImageURI(mFilePathUri);
            /*try {
                //Bitmap bitmap = (Bitmap)data.getExtras().get("data");
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mFilePathUri);
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }*/
        } else {
            Toast.makeText(getActivity(), "erreuuuur", Toast.LENGTH_SHORT).show();
        }
    }
    //**************************Curent Location*******************************
    @Override
    public void onLocationChanged(Location location) {
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        latitudeTxt.setText(""+latitude);
        longitudeTxt.setText(""+longitude);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}