package com.idevelopstudio.poshopping.Extra;

import android.util.Log;

import com.idevelopstudio.poshopping.Database.Product;

import java.util.List;

import co.nedim.maildroidx.MaildroidX;
import co.nedim.maildroidx.MaildroidXType;

public class SendRecieptEmail {

    private static final String TAG = SendRecieptEmail.class.getSimpleName();

    public static void sendEmail(List<Product> products, float totalPrice, String email){
        StringBuilder stringBuilder = new StringBuilder("Thank You for shopping at <b>Tropic Shopper</b>\n");
        stringBuilder.append("<table><tr><th>Your Receipt</th><th></th><th></th></tr><tr><td>No</td><td>Name</td><td>    </td><td>Price</td></tr>");
        //stringBuilder.append("<tr><td>1</td><td>Swimming</td><td>1:30</td></tr>");

        for(int i = 0; i < products.size(); i++){
            int num = i+1;
            stringBuilder.append("<tr><td>"+num+"</td><td>"+products.get(i).getName()+"</td><td>    </td><td> "+products.get(i).getPrice()+" PKR</td></tr>");
        }

        stringBuilder.append("<tr><td></td><td>    </td><td>Total</td><td>"+totalPrice+" PKR</td></tr>");
        stringBuilder.append("</table>");

       MaildroidX.Builder maildroidX =  new MaildroidX.Builder();
       maildroidX.smtp("smtp.gmail.com")
               .smtpUsername("info.oculusseo@gmail.com")
               .smtpPassword("Pakistan@123")
               .smtpAuthentication(true)
               .port("465")
               .type(MaildroidXType.HTML)
               .to(email)
               .from("test@digitronics.us")
               .subject("Test Email")
               .body(stringBuilder.toString())
               .onCompleteCallback(new MaildroidX.onCompleteCallback() {
                   @Override
                   public void onSuccess() {
                       Log.d(TAG, "Email Sent!");
                   }

                   @Override
                   public void onFail(String s) {
                       Log.d(TAG, "Email Failed: " + s);
                   }

                   @Override
                   public long getTimeout() {
                       Log.d(TAG, "Email Timeout!");
                       return 0;
                   }
               });
       maildroidX.mail();
    }



}
