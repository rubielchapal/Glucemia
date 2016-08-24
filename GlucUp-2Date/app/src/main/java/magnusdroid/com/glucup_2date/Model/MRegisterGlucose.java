package magnusdroid.com.glucup_2date.Model;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import magnusdroid.com.glucup_2date.Controller.PrefManager;

/**
 * Created by Dell on 22/06/2016.
 */
public class MRegisterGlucose {

    private PrefManager prefManager;
    private String ipServer;
    private JSONObject jsonObject;

    public JSONObject sendGluc(String document, String value, String state, String fecha, String personal, Context context) throws JSONException {


        prefManager = new PrefManager(context);

        ipServer = prefManager.IpServer();
        String urlServer = "http://"+ipServer+":8084/FHIRTest/RegisterGlucose";
        Map<String,Object> map = new LinkedHashMap<>();

        try {
            map.put("pacient",document);
            map.put("data",value);
            map.put("date",fecha);
            map.put("state",state);
            map.put("personal",personal);

            Log.i("Datos",""+map);

            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String,Object> param : map.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "ISO-8859-1"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "ISO-8859-1"));
            }
            byte[] postDataBytes = postData.toString().getBytes("ISO-8859-1");


            URL url = new URL(urlServer);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
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
        }catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}
