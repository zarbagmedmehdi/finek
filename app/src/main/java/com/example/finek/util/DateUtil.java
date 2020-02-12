package com.example.finek.util;

import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

   static public Timestamp toTimstamp(int[] tabDate,Boolean cas) throws ParseException {
        String sDate="";
        if(cas){
            tabDate[2]=1;
            tabDate[1]=1;
            tabDate[0]=1970;
            Date d=new Date();
            sDate = tabDate[2]+"-"+tabDate[1]+"-"+tabDate[0]+" "+tabDate[3]+":"+tabDate[4]+":"+tabDate[5];
            System.out.println(sDate);
        }
        else {
            sDate = tabDate[2]+"-"+tabDate[1]+"-"+tabDate[0]+" "+tabDate[3]+":"+tabDate[4]+":"+tabDate[5];
            System.out.println(sDate);

        }
         sDate = tabDate[2]+"-"+tabDate[1]+"-"+tabDate[0]+" "+tabDate[3]+":"+tabDate[4]+":"+tabDate[5];
        SimpleDateFormat formatter6=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date6=formatter6.parse(sDate);
       System.out.println(date6);
        Timestamp ts=new Timestamp(date6);
        return ts;
    }
}

