package android.geeps.util;

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
import java.util.concurrent.ExecutionException;

public class HTTPPostUser extends AsyncTask<String, Void, String> {

   private static final String HEADERVALUE = "application/json";
    private static final String URL = "http://geeps2.herokuapp.com/usuario/cadastro";

   @Override
   protected final String doInBackground(final String... params) {

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

   public String registryUser(String name, String phone, String countryCode, String regId){
       try {
           return this.execute(name, phone, countryCode, regId).get();
       } catch (Exception e) {
           return null;
       }
   }

}
