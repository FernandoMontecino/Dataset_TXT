package com.example.dataset_txt;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Datos {
    int timestamp;
    String timeStamp = new SimpleDateFormat("dd/MM/yyyy_HH:mm:ss:ms").format(new Date());

    Datos TimeSTAMP= new Datos(timeStamp);

    public Datos(String timeStamp) {

    }
}

