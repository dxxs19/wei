package com.wei.ipc.http;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by WEI on 2016/8/9.
 */
public class HttpAccess
{
    OkHttpClient mOkHttpClient = new OkHttpClient();
    final static String url = "https://raw.github.com/square/okhttp/master/README.md";
    String result = "";

    public String get(String url)
    {
        Request request = new Request.Builder().url(url).build();
        Call call = mOkHttpClient.newCall(request);
        try {
            Response response = call.execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                result = e.getMessage();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                result = response.body().string();
//            }
//        });
        return result;
    }

    public static void main(String[] args) {
        HttpAccess httpAccess = new HttpAccess();
        String response = httpAccess.get("https://www.duba.com/");
        System.out.print(response);
    }
}
