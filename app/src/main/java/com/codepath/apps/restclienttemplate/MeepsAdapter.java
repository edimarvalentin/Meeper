package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Meep;

import java.util.List;

public class MeepsAdapter extends RecyclerView.Adapter<MeepsAdapter.ViewHolder> {

    Context context;
    List<Meep> meeps;

    public MeepsAdapter(Context context, List<Meep> meeps){
        this.context = context;
        this.meeps = meeps;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_meep, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       Meep meep = meeps.get(position);
       holder.bind(meep);
    }

    @Override
    public int getItemCount() {
        return meeps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        TextView tvRemeeps;
        TextView tvLikes;
        TextView tvUsername;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            tvRemeeps = itemView.findViewById(R.id.tvRemeeps);
            tvUsername = itemView.findViewById(R.id.tvUsername);

        }

        public void bind(Meep meep) {
            tvBody.setText(meep.body);
            tvRemeeps.setText(String.valueOf(meep.remeeps));
            tvLikes.setText(String.valueOf(meep.likes));
            Glide.with(context).load(meep.user.getProfileUrl()).into(ivProfileImage);
            tvScreenName.setText(meep.user.getUsername());
            tvUsername.setText("@" + meep.user.getName());
        }
    }

}
