package com.example.ecommerce.activits;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ecommerce.R;
import com.example.ecommerce.models.NewProductsModel;
import com.example.ecommerce.models.PopuralProductsModel;
import com.example.ecommerce.models.ShowAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DetailedActivity extends AppCompatActivity {

    ImageView detailedImg;
    TextView name,description,price,quantity;

    RatingBar rating;
    Button addToCart,buyNow;
    ImageView addItems,removeItems;

   Toolbar toolbar;
    int totalQuantity = 1;
    int totalPrice = 0;
//    New Products
    NewProductsModel newProductsModel = null;

//    PopularProducts
    PopuralProductsModel popuralProductsModel = null;

//    Show All
    ShowAllModel showAllModel = null;

    FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        toolbar = findViewById(R.id.detailed_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        final Object obj = getIntent().getSerializableExtra("detailed");

        if (obj instanceof NewProductsModel){

            newProductsModel = (NewProductsModel) obj;
        }

        else if (obj instanceof PopuralProductsModel){
            popuralProductsModel = (PopuralProductsModel) obj;
        }

        else if (obj instanceof ShowAllModel){
            showAllModel = (ShowAllModel) obj;
        }

        detailedImg = findViewById(R.id.detailed_img);

        detailedImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to launch the new activity
                Intent intent = new Intent(DetailedActivity.this, ZoomActivity.class);

                // Pass the image URL to the new activity
                if (newProductsModel != null) {
                    intent.putExtra("imageUrl", newProductsModel.getImg_url());
                } else if (popuralProductsModel != null) {
                    intent.putExtra("imageUrl", popuralProductsModel.getImg_url());
                } else if (showAllModel != null) {
                    intent.putExtra("imageUrl", showAllModel.getImg_url());
                }

                // Start the new activity
                startActivity(intent);
            }
        });

        quantity = findViewById(R.id.quantity);
        name = findViewById(R.id.detailed_name);
        rating =  findViewById(R.id.rating);
        description = findViewById(R.id.detailed_desc);
        price = findViewById(R.id.detailed_price);

        addToCart = findViewById(R.id.add_to_cart);
        buyNow = findViewById(R.id.buy_now);

        addItems = findViewById(R.id.add_item);
        removeItems = findViewById(R.id.remove_item);

//       New Products

        if (newProductsModel != null){
            Glide.with(getApplicationContext()).load(newProductsModel.getImg_url()).into(detailedImg);
            name.setText(newProductsModel.getName());
            rating.setRating(Float.parseFloat(newProductsModel.getRating()));
            description.setText(newProductsModel.getDescription());
            price.setText(String.valueOf(newProductsModel.getPrice()));
            name.setText(newProductsModel.getName());

           totalPrice = newProductsModel.getPrice() * totalQuantity;
        }

        // Popular Products

        if (popuralProductsModel != null) {
            Glide.with(getApplicationContext()).load(popuralProductsModel.getImg_url()).into(detailedImg);
            name.setText(popuralProductsModel.getName());
            rating.setRating(Float.parseFloat(popuralProductsModel.getRating()));
            description.setText(popuralProductsModel.getDescription());
            price.setText(String.valueOf(popuralProductsModel.getPrice()));
            name.setText(popuralProductsModel.getName());

            totalPrice = popuralProductsModel.getPrice() * totalQuantity;

        }

        //Show All Products
        if (showAllModel != null) {
            Glide.with(getApplicationContext()).load(showAllModel.getImg_url()).into(detailedImg);
            name.setText(showAllModel.getName());
            rating.setRating(Float.parseFloat(showAllModel.getRating()));
            description.setText(showAllModel.getDescription());
            price.setText(String.valueOf(showAllModel.getPrice()));
            name.setText(showAllModel.getName());

            totalPrice = showAllModel.getPrice() * totalQuantity;

        }
//        Buy Now

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailedActivity.this,AddressActivity.class);

                if (newProductsModel != null){
                    intent.putExtra("item",newProductsModel);
                }
                if (popuralProductsModel != null){
                    intent.putExtra("item",popuralProductsModel);
                }
                if (showAllModel != null){
                    intent.putExtra("item",showAllModel);
                }
                startActivity(intent);
            }
        });

//Add To cart
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });

        addItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if (totalQuantity < 10){
                   totalQuantity ++;
                   quantity.setText(String.valueOf(totalQuantity));

                   if (newProductsModel != null){
                       totalPrice = newProductsModel.getPrice() * totalQuantity;
                   }
                   if (popuralProductsModel != null){
                       totalPrice = popuralProductsModel.getPrice() * totalQuantity;
                   }
                   if (showAllModel != null){
                       totalPrice = showAllModel.getPrice() * totalQuantity;

                   }
               }

            }
        });

        removeItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalQuantity > 1){
                    totalQuantity --;
                    quantity.setText(String.valueOf(totalQuantity));
                }

            }
        });

    }

    private void addToCart() {

        String saveCurrentTime,saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String,Object> cartMap = new HashMap<>();

        cartMap.put("productName",name.getText().toString());
        cartMap.put("productPrice",price.getText().toString());
        cartMap.put("currentTime",saveCurrentTime);
        cartMap.put("currentDate",saveCurrentDate);
        cartMap.put("totalQuantity",quantity.getText().toString());
        cartMap.put("totalPrice",totalPrice);

        firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("User").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(DetailedActivity.this, "Added To A Cart", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });


    }
}