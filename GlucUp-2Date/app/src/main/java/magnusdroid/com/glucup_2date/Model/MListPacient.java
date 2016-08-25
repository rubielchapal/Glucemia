package magnusdroid.com.glucup_2date.Model;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import magnusdroid.com.glucup_2date.Controller.PrefManager;

/**
 * Created by Dell on 19/07/2016.
 */
public class MListPacient {

    private PrefManager prefManager;
    private String ipServer;
    private JSONObject jsonObject;
    // Shared preferences file name
    private static final String DECODE = "UTF-8";

    public JSONObject getList(String document, Context context) throws JSONException {

        prefManager = new PrefManager(context);

        ipServer = prefManager.IpServer();
        //String urlServer = "http://"+ipServer+":8084/FHIRTest/ListGlucose";
        //String urlServer = "http://"+ipServer+":8084/FHIRTest/DateFilterGlucose";
        String urlServer = "http://"+ipServer+":8084/FHIRTest/PacientListServlet";
        Map<String,Object> map = new LinkedHashMap<>();

        try {
            map.put("user",document);

            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String,Object> param : map.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                //postData.append(URLEncoder.encode(param.getKey(), "ISO-8859-1"));
                postData.append(URLEncoder.encode(param.getKey(), DECODE));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), DECODE));
            }
            byte[] postDataBytes = postData.toString().getBytes(DECODE);


            URL url = new URL(urlServer);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(7000 /* milliseconds */);
            conn.setConnectTimeout(10000 /* milliseconds */);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);

            Log.w("url",""+urlServer);

            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "ISO-8859-1"));
            StringBuilder sb = new StringBuilder();
            for (int c; (c = in.read()) >= 0;)
                sb.append((char)c);
            String response = sb.toString();
            Log.i("rta",""+response);
            jsonObject = new JSONObject(response);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SocketTimeoutException e){
            jsonObject = new JSONObject("{'status':3}");
        } catch (ConnectException e) {
            jsonObject = new JSONObject("{'status':3}");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}