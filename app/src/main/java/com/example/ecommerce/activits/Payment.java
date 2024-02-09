package com.example.ecommerce.activits;

public class Payment {
    private String bkashNumber;
    private String trxdId;
    private String amount;
    public Payment() {
    }

    public Payment(String bkashNumber, String trxdId, String amount) {
        this.bkashNumber = bkashNumber;
        this.trxdId = trxdId;
        this.amount = amount;
    }


    public String getBkashNumber() {
        return bkashNumber;
    }

    public String getTrxdId() {
        return trxdId;
    }

    public String getAmount() {
        return amount;
    }
}
