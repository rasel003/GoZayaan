package com.rasel.androidbaseapp.ui.dialog;

import com.google.gson.annotations.SerializedName;

public class BankData {

    @SerializedName("bank_title")
    private String bankTitle;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("bank_logo")
    private String bankLogo;

    @SerializedName("bank_id")
    private int bankId;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("id")
    private int id;

    @SerializedName("status")
    private String status;

    public BankData(int id, String bankTitle) {
        this.id = id;
        this.bankTitle = bankTitle;
    }

    public String getBankTitle() {
        return bankTitle;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public int getBankId() {
        return bankId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setBankLogo(String bankLogo) {
        this.bankLogo = bankLogo;
    }
}
