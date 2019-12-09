package com.example.music;

import android.renderscript.ScriptGroup;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Geci {
    public static void test() throws IOException {
        URL url = new URL("http://s.gecimi.com/lrc/344/34435/3443588.lrc");
        HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
        httpURLConnection.setRequestMethod("get");
        //connection.setDoOutput(true);
        httpURLConnection.setReadTimeout(15*1000);
        httpURLConnection.connect();

        InputStream inputStream = httpURLConnection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        Map<String, List<String>> responseHeader = httpURLConnection.getHeaderFields();
        int fileSize = Integer.parseInt(responseHeader.get("Content-Length").get(0));
        byte[] fileContent = new byte[fileSize];
        int res = inputStream.read(fileContent, 0, fileSize);
        if (res == fileSize) {

        }


        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
    }

    public static void requstLrcData(final String name,okhttp3.Callback callback) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Log.i("tag", "requstLrcData: "+name);
        String name1 = name.trim();//去掉空格
        Log.i("TAG", "requstLrcData:"+name1);
        Request request = new Request.Builder()
                .url("http://geci.me/api/lyric/"+name1)
                .build();
        Call call = client.newCall(request);
        Response response = call.execute();
        String lengthStr = response.header("Content-Length");
        int length = Integer.parseInt(lengthStr);
        byte[] fileContent = response.body().bytes();

    }
    public static String parseJOSNWithGSON(Response response , int c){
        try{
            String ResponsData = response.body().string();
            JSONObject jsonObject = new JSONObject(ResponsData);
            int count = Integer.parseInt(jsonObject.getString("count"));
            Log.i("TAG", "parseJOSNWithGSONCOUNT:"+count);
            if (count>=c){
                String result = jsonObject.getString("result");
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject1 = jsonArray.getJSONObject(c-1);
                String url = jsonObject1.getString("lrc");
                Log.i("TAG", "parseJOSNWithGSON:1 "+url);
                return url;
            }else {
                Log.i("TAG", "parseJOSNWithGSON: "+c);
                return "";
            }
        }catch (Exception e){

        }
        return "";
    }

    public static String getLrcFromAssets(String Url){
        Log.i("first","getLrcFromAssets: "+Url);
        if (Url.equals("")){
            return "";
        }
        try {
            URL url=new URL(Url);
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            InputStream input=conn.getInputStream();
            BufferedReader in=new BufferedReader(new InputStreamReader(input));
            String line = "" ;
            String result = "";
            while ((line = in.readLine() )!= null){//逐行读取
                if (line.trim().equals(""))
                    continue;
                result += line + "\r\n";
                Log.i("first","getLrcFromAssets: "+result);
            }
            Log.i("total","getLrcFromAssets: "+result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


}
