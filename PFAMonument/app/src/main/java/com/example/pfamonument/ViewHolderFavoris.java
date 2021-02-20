package com.example.pfamonument;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ViewHolderFavoris extends RecyclerView.ViewHolder  {

    View mView;

    public ViewHolderFavoris (View itemView) {
        super(itemView);

        mView = itemView;

        //item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });
    }

    //set details to recycler View row
    public void setDetails (final Context context, String title, String description, String image)
    {
        TextView mTitleView = mView.findViewById(R.id.favTitle);
        TextView mDetailView = mView.findViewById(R.id.favDescription);
        ImageView mImageView = mView.findViewById(R.id.favImageView);
        //set data to views
        mTitleView.setText(title);
        mDetailView.setText(description);
        Picasso.get().load(image).into(mImageView);

    }

    private ViewHolderFavoris.ClickListener mClickListener;

    //interface to send callback
    public interface ClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnClickListener(ViewHolderFavoris.ClickListener clickListener) {
        mClickListener = clickListener;
    }
}
