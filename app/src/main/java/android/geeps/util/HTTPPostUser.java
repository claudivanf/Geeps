package android.geeps.util;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.GsonBuilder;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.HashMap;
import java.util.Map;

public class HTTPPostUser extends AsyncTask<String, Void, Void> {

   private static final String HEADERVALUE = "application/json";

   @Override
   protected final Void doInBackground(final String... params) {
      String url =
            "http://geeps2.herokuapp.com/usuario/cadastro";
      String name = params[0];
      String phone = params[1];
      String countryCode = params[2];
      String regId = params[3];

      Map<String, String> comment = new HashMap<String, String>();
      comment.put("name", name);
      comment.put("phone", phone);
      comment.put("countryCode", countryCode);
      comment.put("regId", regId);

      String json = new GsonBuilder().create().toJson(comment, Map.class);

      try {
         HttpPost httpPost = new HttpPost(url);
         httpPost.setEntity(new StringEntity(json));
         httpPost.setHeader("Accept", HEADERVALUE);
         httpPost.setHeader("Content-type", HEADERVALUE);
         new DefaultHttpClient().execute(httpPost);
      } catch (Exception e) {
         Log.e("POST", e.getMessage());
      }
      return null;
   }

   public void registryUser(String name, String phone, String countryCode, String regId){
      this.execute(name, phone, countryCode, regId);
   }

}
