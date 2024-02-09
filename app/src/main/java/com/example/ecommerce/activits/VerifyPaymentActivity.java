package com.example.ecommerce.activits;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VerifyPaymentActivity extends AppCompatActivity {

    private EditText bkashNumberEditText, trxdIdEditText, amountEditText;
    private Button confirmButton;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_payment);

        databaseReference = FirebaseDatabase.getInstance().getReference("payments");

        bkashNumberEditText = findViewById(R.id.bkash_number);
        trxdIdEditText = findViewById(R.id.trxd_id);
        amountEditText = findViewById(R.id.amount);
        confirmButton = findViewById(R.id.confirm);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePaymentData();
                showNotification();
            }

            private void savePaymentData() {
                String bkashNumber = bkashNumberEditText.getText().toString().trim();
                String trxdId = trxdIdEditText.getText().toString().trim();
                String amount = amountEditText.getText().toString().trim();

                // Check if any of the fields is empty

                if (TextUtils.isEmpty(bkashNumber) || TextUtils.isEmpty(trxdId) || TextUtils.isEmpty(amount)) {
                    Toast.makeText(VerifyPaymentActivity.this, "Please fill up in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (isValidTrxdId(trxdId)) {

                    Payment payment = new Payment(bkashNumber, trxdId, amount);

                    // Push the payment data to Firebase
                    databaseReference.push().setValue(payment);

                    // Clear the input fields
                    bkashNumberEditText.setText("");
                    trxdIdEditText.setText("");
                    amountEditText.setText("");

                    Toast.makeText(VerifyPaymentActivity.this, "You will be contacted within the next 30 minutes.", Toast.LENGTH_LONG).show();

                }else {
                    Toast.makeText(VerifyPaymentActivity.this, "Invalid Transaction ID. It must be a 10-digit number", Toast.LENGTH_SHORT).show();
                }
            }

            private boolean isValidTrxdId(String trxdId) {
                return trxdId.matches(".*[a-zA-Z].*") && trxdId.matches(".*\\d.*");
            }
        });
    }

    private void showNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Create a notification channel (required for Android 8.0 and higher)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("payment_channel", "Payment Channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);

        }
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(VerifyPaymentActivity.this, "payment_channel")
                    .setContentTitle("Payment Confirmation")
                    .setContentText("Thank You! Your Payment Is Successful.")
                    .setSmallIcon(R.drawable.e_store)  // Replace with your own notification icon
                    .build();
        }

        // Display the notification
        notificationManager.notify(1, notification);  // Use a unique ID for each notification
    }

}