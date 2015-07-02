package android.geeps.http;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.GsonBuilder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class HTTPPedidos extends AsyncTask<String, Void, String> {

   private static final String HEADER_VALUE = "application/json";
    private static final String URL = "http://geeps2.herokuapp.com/usuario/pedidos";

   @Override
   protected final String doInBackground(final String... params) {

      Map<String, String> comment = new HashMap<String, String>();
      comment.put("phone", params[0]);

      String json = new GsonBuilder().create().toJson(comment, Map.class);
      String response = "";
      try {
          HttpPost httpPost = new HttpPost(URL);
          httpPost.setEntity(new StringEntity(json));
          httpPost.setHeader("Accept", HEADER_VALUE);
          httpPost.setHeader("Content-type", HEADER_VALUE);
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

   public JSONArray getPedidos(String phone){
       try {
           String response = this.execute(phone).get();
           JSONArray arrayPedidos = new JSONArray(response);
           return arrayPedidos;
       } catch (Exception e) {
           return null;
       }
   }

}
