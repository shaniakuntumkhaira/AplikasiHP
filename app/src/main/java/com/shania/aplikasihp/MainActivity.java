package com.shania.aplikasihp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private EditText customerNameEditText, itemCodeEditText, quantityEditText;
    private RadioGroup membershipRadioGroup;
    private View chooseItemLayout, showResultLayout;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customerNameEditText = findViewById(R.id.customerNameEditText);
        itemCodeEditText = findViewById(R.id.itemCodeEditText);
        quantityEditText = findViewById(R.id.quantityEditText);
        membershipRadioGroup = findViewById(R.id.membershipRadioGroup);
        Button nextButton = findViewById(R.id.nextButton);
        chooseItemLayout = findViewById(R.id.chooseItemLayout);
        showResultLayout = findViewById(R.id.showResultLayout);
        resultTextView = findViewById(R.id.resultTextView);
        final TextView itemPriceTextView = findViewById(R.id.itemPriceTextView);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String customerName = customerNameEditText.getText().toString().trim();
                String itemCode = itemCodeEditText.getText().toString().trim();
                int quantity = Integer.parseInt(quantityEditText.getText().toString().trim());
                int selectedMembershipId = membershipRadioGroup.getCheckedRadioButtonId();
                RadioButton selectedMembershipRadioButton = findViewById(selectedMembershipId);
                String membership = selectedMembershipRadioButton.getText().toString();

                double totalPrice = calculateTotalPrice(itemCode, quantity);
                double discount = calculateDiscount(totalPrice);
                double membershipDiscount = calculateMembershipDiscount(totalPrice, membership);
                double totalPriceAfterDiscount = totalPrice - discount - membershipDiscount;
                double unitPrice = getItemPrice(itemCode);

                DecimalFormat decimalFormat = new DecimalFormat("Rp #,###");
                String formattedUnitPrice = decimalFormat.format(unitPrice);
                String formattedDiscount = "Rp " + decimalFormat.format(discount);
                String formattedMembershipDiscount = "Rp " + decimalFormat.format(membershipDiscount);
                String formattedTotalPriceAfterDiscount = "Rp " + decimalFormat.format(totalPriceAfterDiscount);
                String formattedTotalPrice = "Rp " + decimalFormat.format(totalPrice); // Total harga dari harga per barang

                String welcomeMessage = "Selamat Datang " + customerName + "\n";
                String memberTypeMessage = "Tipe Member : " + membership + "\n\n";
                String transactionDetails = "Transaksi Hari Ini:\n" +
                        "Kode Barang: " + itemCode + "\n" +
                        "Nama Barang: " + getProductName(itemCode) + "\n" +
                        "Harga: " + formattedUnitPrice + "\n" +
                        "Total Harga: " + formattedTotalPrice + "\n" + // Menampilkan total harga yang sesuai
                        "Harga Diskon: " + formattedDiscount + "\n" +
                        "Diskon Member: " + formattedMembershipDiscount + "\n" +
                        "Jumlah Bayar: " + formattedTotalPriceAfterDiscount;

                resultTextView.setText(welcomeMessage + memberTypeMessage + transactionDetails + "\n\nTerima Kasih Telah Berbelanja Disini!");

                chooseItemLayout.setVisibility(View.GONE);
                showResultLayout.setVisibility(View.VISIBLE);
            }
        });

        itemCodeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String itemCode = itemCodeEditText.getText().toString().trim();
                    double price = getItemPrice(itemCode);
                    if (price != 0) {
                        DecimalFormat decimalFormat = new DecimalFormat("Rp #,###");
                        String formattedPrice = decimalFormat.format(price);
                        itemPriceTextView.setText("Harga Barang: " + formattedPrice);
                    } else {
                        itemPriceTextView.setText("Harga Barang: Tidak Diketahui");
                    }
                }
            }
        });
    }

    private double calculateTotalPrice(String itemCode, int quantity) {
        double unitPrice = getItemPrice(itemCode);
        double totalPrice = unitPrice * quantity; // Perhitungan total harga dari harga per barang dikali jumlah barang
        return totalPrice;
    }

    private double calculateDiscount(double totalPrice) {
        double discount = 0;
        if (totalPrice > 10000000) {
            discount += 100000; // Diskon tambahan
        }
        return discount;
    }

    private double calculateMembershipDiscount(double totalPrice, String membership) {
        double membershipDiscount = 0;
        switch (membership) {
            case "Gold":
                membershipDiscount = totalPrice * 0.1;
                break;
            case "Silver":
                membershipDiscount = totalPrice * 0.05;
                break;
            case "Biasa":
                membershipDiscount = totalPrice * 0.02;
                break;
        }
        return membershipDiscount;
    }

    private double getItemPrice(String itemCode) {
        double price = 0;
        switch (itemCode) {
            case "SGS":
                price = 12999999;
                break;
            case "O17":
                price = 2500999;
                break;
            case "AV4":
                price = 9150999;
                break;
        }
        return price;
    }

    private String getProductName(String itemCode) {
        String productName = "";
        switch (itemCode) {
            case "SGS":
                productName = "Samsung Galaxy S";
                break;
            case "O17":
                productName = "Oppo A17";
                break;
            case "AV4":
                productName = "Asus Vivobook 14";
                break;
        }
        return productName;
    }

    public void shareTransaction(View view) {
        String transactionDetails = resultTextView.getText().toString();

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, transactionDetails);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }
}
