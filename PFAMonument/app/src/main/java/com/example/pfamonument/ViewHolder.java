package com.example.pfamonument;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class ViewHolder extends RecyclerView.ViewHolder {

    View mView;

    public ViewHolder(View itemView) {
        super(itemView);

        mView = itemView;

        //item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });
        //item long click
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view, getAdapterPosition());
                return true;
            }
        });
    }

    //set details to recycler View row
    public void setDetails (final Context context, String title, String description, String image, String teste, double latitude, double longitude)
    {
        TextView mTitleView = mView.findViewById(R.id.rTitle);
        TextView mDetailView = mView.findViewById(R.id.rDescription);
        ImageView mImageView = mView.findViewById(R.id.rImageView);
        TextView mTestView = mView.findViewById(R.id.rTest);
        TextView mLatitudeView = mView.findViewById(R.id.rLatitude);
        TextView mLongitudeView = mView.findViewById(R.id.rLongitude);
        TextView mImageVView = mView.findViewById(R.id.rImage);

        //set data to views
        mTitleView.setText(title);
        mDetailView.setText(description);
        Picasso.get().load(image).into(mImageView);
        mTestView.setText(teste);
        mLatitudeView.setText(Double.toString(latitude));
        mLongitudeView.setText(Double.toString(longitude));
        mImageVView.setText(image);
    }

    private ViewHolder.ClickListener mClickListener;

    //interface to send callback
    public interface ClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(ViewHolder.ClickListener clickListener) {
        mClickListener = clickListener;
    }
}
