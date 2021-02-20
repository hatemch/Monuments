package com.example.pfamonument;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class Login extends AppCompatActivity {

    Button loginButton;
    ProgressBar loginProgressBar;
    EditText editTextEmail;
    EditText editTextPassword;
    TextView createCompte;
    ImageView compteImage;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    FirebaseDatabase database;
    DatabaseReference users;
    DatabaseReference user_actuel;

    private ProgressDialog progressDialog;

    FirebaseStorage storage;
    StorageReference storageReference;

    private String downloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        loginButton = (Button) findViewById(R.id.loginButton);
        loginProgressBar = (ProgressBar) findViewById(R.id.login_progressBar);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        createCompte = (TextView) findViewById(R.id.createCompte);
        loginProgressBar.setVisibility(View.INVISIBLE);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null) {

                    startActivity(new Intent(Login.this, Accueil.class));
                }
            }
        };

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLogin();
            }
        });

        //***************** Creer Compte *****************
        progressDialog = new ProgressDialog(this);

        createCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Login.this);
                final View mView = getLayoutInflater().inflate(R.layout.dialog_compte, null);

                final EditText NewName = (EditText) mView.findViewById(R.id.SaisirName);
                final EditText NewEmail = (EditText) mView.findViewById(R.id.SaisirEmail);
                final EditText NewPassword = (EditText) mView.findViewById(R.id.SaisirPassword);
                compteImage = (ImageView) mView.findViewById(R.id.imageCompte);

                Button mBtnCompte = (Button) mView.findViewById(R.id.compteButton);
                //Firebase
                final FirebaseAuth CompteAuth = FirebaseAuth.getInstance();
                final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

                //Firebase Init
                storage = FirebaseStorage.getInstance();
                storageReference = storage.getReference();

                compteImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ChooseImage();
                    }
                });

                //click button
                mBtnCompte.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String name = NewName.getText().toString().trim();
                        String email = NewEmail.getText().toString().trim();
                        String password = NewPassword.getText().toString().trim();

                        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

                            progressDialog.setMessage("s'enregistrer...");
                            progressDialog.show();

                            CompteAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {

                                        String user_id = CompteAuth.getCurrentUser().getUid();

                                        user_actuel = mDatabase.child(user_id);
                                        user_actuel.child("name").setValue(name);

                                        progressDialog.dismiss();
                                        imageUpload(user_id);

                                        return;
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(Login.this, "SVP remplir les champs vides !!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });
    }

    private void imageUpload(String userID) {
        if(filePath != null)
        {
            StorageReference ref = storageReference.child("images/"+ userID);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            /*downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                            Toast.makeText(Login.this, downloadUrl, Toast.LENGTH_LONG).show();
                            user_actuel.child("profileImage").setValue(downloadUrl);*/


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Login.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void ChooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selectionner image"),PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null)
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                compteImage.setImageBitmap(bitmap);

            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void startLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)) {
            Toast.makeText(this, "SVP entrer l'email", Toast.LENGTH_SHORT).show();
            return;
        } else if(TextUtils.isEmpty(password)) {
            Toast.makeText(this, "SVP entrer le password", Toast.LENGTH_SHORT).show();
            return;
        } else {
            loginProgressBar.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.INVISIBLE);

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(Login.this, "Vérifier email et mot de passe", Toast.LENGTH_LONG).show();
                        loginProgressBar.setVisibility(View.INVISIBLE);
                        loginButton.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }
}
