package com.example.ecommerce.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce.R;
import com.example.ecommerce.activits.DetailedActivity;
import com.example.ecommerce.models.PopuralProductsModel;

import java.util.List;


public class PopuralProductsAdapter extends RecyclerView.Adapter<PopuralProductsAdapter.ViewHolder> {

    private Context context;
    private List<PopuralProductsModel> popuralProductsModelList;

    public PopuralProductsAdapter(Context context, List<PopuralProductsModel> popuralProductsModelList) {
        this.context = context;
        this.popuralProductsModelList = popuralProductsModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.popural_iteams,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(popuralProductsModelList.get(position).getImg_url()).into(holder.imageView);
        holder.name.setText(popuralProductsModelList.get(position).getName());
        holder.price.setText(String.valueOf(popuralProductsModelList.get(position).getPrice()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailedActivity.class);
                intent.putExtra("detailed",popuralProductsModelList.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return popuralProductsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

       ImageView imageView;
       TextView name,price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.all_img);
            name = itemView.findViewById(R.id.all_product_name);
            price = itemView.findViewById(R.id.all_price);
        }
    }
}
