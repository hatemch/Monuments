package com.example.pfamonument;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pfamonument.Model.Images;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;

public class Home extends Fragment {

    RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home, container, false);

        //RecyclerView
        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        //set layout as LinearLayout
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //send Query to firebase database
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("Data");

        return rootView;
    }

    //load data into recycler view onStart
    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Images,ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Images, ViewHolder>(Images.class, R.layout.row, ViewHolder.class, mRef) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Images model, int position) {
                        viewHolder.setDetails(getActivity().getApplicationContext(), model.getTitle(), model.getDescription(), model.getImage(), model.getTeste(), model.getLatitude(), model.getLongitude());
                    }

                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                        ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                //Views
                                TextView mTitle = view.findViewById(R.id.rTitle);
                                TextView mDescription = view.findViewById(R.id.rDescription);
                                ImageView mImageView = view.findViewById(R.id.rImageView);
                                TextView mTest = view.findViewById(R.id.rTest);
                                TextView mLatitude = view.findViewById(R.id.rLatitude);
                                TextView mLongitude = view.findViewById(R.id.rLongitude);
                                TextView mImage = view.findViewById(R.id.rImage);

                                //get data from views
                                String mTitre = mTitle.getText().toString();
                                String mDesc = mDescription.getText().toString();
                                Drawable mDrawable = mImageView.getDrawable();
                                Bitmap mBitmap = ((BitmapDrawable)mDrawable).getBitmap();
                                String mImageV = mImage.getText().toString();
                                String mTestFav = mTest.getText().toString();
                                String mLatid = mLatitude.getText().toString();
                                String mLong = mLongitude.getText().toString();

                                //passer les données vers une nouvelle activity
                                Intent intent = new Intent(view.getContext(), Description.class);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                byte[] bytes = stream.toByteArray();
                                intent.putExtra("image", bytes); //put bitmap image as array of bytes
                                intent.putExtra("imageV", mImageV);
                                intent.putExtra("title", mTitre); //put titre
                                intent.putExtra("description", mDesc); //put description
                                intent.putExtra("teste", mTestFav);
                                intent.putExtra("latitude", mLatid);
                                intent.putExtra("longitude", mLong);
                                startActivity(intent); //start activity

                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
                                //TODO do your own impementation
                            }
                        });
                        return viewHolder;
                    }
                };
        //set adapter to recyclerView
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

}
