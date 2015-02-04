package android.geeps;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.Charset;

public class HTTPRequest extends AsyncTask<String, Void, String> {

   @Override
   protected final String doInBackground(final String... params) {
      String url =
            "http://linkDoServidor/metodoAserUtilizado";
      String line = "";
      try {

         HttpClient client = new DefaultHttpClient();
         HttpGet request = new HttpGet();
         request.setHeader("Content-Type", "application/json");
         request.setURI(new URI(url));
         HttpResponse serverAnswer = client.execute(request);

         BufferedReader br =
               new BufferedReader(new InputStreamReader(
                     serverAnswer.getEntity().getContent(),
                     Charset.defaultCharset()));
         StringBuffer sb = new StringBuffer("");

         while ((line = br.readLine()) != null) {
            sb.append(line);
         }

         br.close();

         line = sb.toString();
      } catch (Exception e) {
         Log.e("GET", e.getMessage());
      }

      return line;
   }

}
