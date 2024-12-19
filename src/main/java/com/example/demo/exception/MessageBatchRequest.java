package com.example.demo.exception;

import java.util.List;
// Kelas ini digunakan untuk merepresentasikan permintaan batch pesan
public class MessageBatchRequest {
    private List<String> promoMessages;
    private List<String> notificationMessages;
    private List<String> alertMessages;


    public List<String> getPromoMessages() {
        return promoMessages;
    }

    public void setPromoMessages(List<String> promoMessages) {
        this.promoMessages = promoMessages;
    }

    public List<String> getNotificationMessages() {
        return notificationMessages;
    }

    public void setNotificationMessages(List<String> notificationMessages) {
        this.notificationMessages = notificationMessages;
    }

    public List<String> getAlertMessages() {
        return alertMessages;
    }

    public void setAlertMessages(List<String> alertMessages) {
        this.alertMessages = alertMessages;
    }
}

//        Kode ini mendefinisikan kelas MessageBatchRequest yang digunakan untuk mengelompokkan beberapa jenis pesan dalam satu permintaan.