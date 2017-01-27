package com.mmadapps.ava.utils;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by bhaskara.reddy on 04-01-2016.
 */
public class WebServices {

    private static String devservice="http://192.168.30.123";
    private static String testingservice="";
    private static String UATServices="";
    private static String service_type=devservice;

    // get Services
    public String GET_FLIGHT_DETAILS=service_type+"/GetAllFlightNames";
    public String GET_WEATHERDETAILS=service_type+"/GetWeatherInfoByGoeidOrCityName?goeid=&cityname=";


    // post urls
    public String ADD_FLIGHT_DETAILS=service_type+"/FlightByDateAndFlightNo";
    public  String  SAVE_USER_ALARMDETAILS=service_type+"/Alarm/";
    public  String  SAVE_USER_CABBOOKINGDETAILS=service_type+"/Booking/";
    public enum ApiType{GetAllFlightNames,GetWeatherInfoByGoeidOrCityName}

    public String CallWebHTTPBindingService(ApiType Api_Type,String MethodName, String Inparam) {
        String resultOutparam = "";
        switch (Api_Type) {

            case GetAllFlightNames:
                try {
                    String saveURL = GET_FLIGHT_DETAILS + MethodName + Inparam;
                    resultOutparam = getResponseFromService(saveURL);
                    Log.e("GetAllFlightNames", saveURL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case GetWeatherInfoByGoeidOrCityName:
                try {
                    String saveURL = GET_WEATHERDETAILS + MethodName + Inparam;
                    resultOutparam = getResponseFromService(saveURL);
                    Log.e("GetAllFlightNames", saveURL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;


        }
        return resultOutparam;
    }


    public String postData(String mUrl, String param) {
        String mData = null;
        HttpURLConnection mURLConnection = null;
        try{
            URL url = new URL(mUrl);
            mURLConnection = (HttpURLConnection) url.openConnection();
            mURLConnection.setRequestMethod("POST");
            mURLConnection.setRequestProperty("Accept","application/json");
            mURLConnection.setRequestProperty("Content-Type","application/json");
            PrintWriter writer = new PrintWriter(mURLConnection.getOutputStream());
            writer.write(param);
            writer.close();
            int statusCode = mURLConnection.getResponseCode();
            if(statusCode != 200 && statusCode != 201){
                return null;
            }
            mData = convertToString(mURLConnection.getInputStream());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(mURLConnection != null)
                mURLConnection.disconnect();
        }
        if(mData != null && mData.length() > 0)
            Log.e(mUrl, mData);
        return mData;
    }


    public String getResponseFromService(String url) {
        String finalResult = null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        HttpResponse response;
        try {
            response = httpclient.execute(httpget);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 404) {
                return null;
            }
            HttpEntity entity = response.getEntity();
            StringBuffer out = new StringBuffer();
            byte[] b = EntityUtils.toByteArray(entity);
            out.append(new String(b, 0, b.length));
            finalResult = out.toString();
        } catch (ClientProtocolException e) {
            Log.e("REST", "There was a protocol based error", e);
        } catch (IOException e) {
            Log.e("REST", "There was an IO Stream related error", e);
        }
        return finalResult;
    }



    private String convertToString(InputStream in) {
        String mData = null;
        try {
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            while ((line = br.readLine()) != null) {
                if (mData == null) {
                    mData = line;
                } else {
                    mData += line;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return mData;
    }



}
