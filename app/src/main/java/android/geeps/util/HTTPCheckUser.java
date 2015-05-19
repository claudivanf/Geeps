package android.geeps.util;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.GsonBuilder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class HTTPCheckUser extends AsyncTask<String, Void, String> {

   private static final String HEADERVALUE = "application/json";
   private static final String URL = "http://geeps2.herokuapp.com/check_user";

   public String getResponse(String phoneUser) {
      try {
         return this.execute(phoneUser).get();
      } catch (Exception ex) {
         return null;
      }
   }

   @Override
   protected final String doInBackground(final String... params) {
      String phone = params[0];
      Map<String, String> comment = new HashMap<String, String>();
      comment.put("phone", phone);

      String json = new GsonBuilder().create().toJson(comment, Map.class);
      String response = "";
      try {
         HttpPost httpPost = new HttpPost(URL);
         httpPost.setEntity(new StringEntity(json));
         httpPost.setHeader("Accept", HEADERVALUE);
         httpPost.setHeader("Content-type", HEADERVALUE);
         HttpClient client = new DefaultHttpClient();

         HttpResponse serverAnswer = client.execute(httpPost);

         BufferedReader br =
               new BufferedReader(new InputStreamReader(
                     serverAnswer.getEntity().getContent(),
                     Charset.defaultCharset()));
         StringBuffer sb = new StringBuffer("");

         while ((response = br.readLine()) != null) {
            sb.append(response);
         }

         br.close();

         response = sb.toString();
      } catch (Exception e) {
         Log.e("POST", e.getMessage());
      }

      return response;
   }

   private ProgressDialog pdia;


}
