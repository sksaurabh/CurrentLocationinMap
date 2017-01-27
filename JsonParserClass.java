package com.mmadapps.ava.utils;

import android.content.Context;

import com.mmadapps.ava.beans.FlightByDateAndFlightNo;
import com.mmadapps.ava.beans.FlightNames;
import com.mmadapps.ava.planyourday.beans.PlaceName;
import com.mmadapps.ava.weather.beans.WeatherDetails;
import com.mmadapps.ava.weather.beans.WeatherWeekDetails;
import com.mmadapps.ava.weather.beans.WeatherDetails;
import com.mmadapps.ava.weather.beans.WeatherWeekDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhaskara.reddy on 04-01-2016.
 */
public class JsonParserClass {

    private String getObjectvalue(JSONObject listObj, String keyOfObject) {
        try {
            try {
                String mKeyValue = listObj.getString(keyOfObject).toString();
                if (mKeyValue.equals("null")) {
                } else {
                    return mKeyValue;
                }
            } catch (NullPointerException nullExp) {
            }
        } catch (JSONException e) {
        }
        return "";
    }


    
    /*public List<Getuseralarmbean> parseGetuserAlarmListDetails(String resultParam) {
        List<Getuseralarmbean> mgetuserlistbean=new ArrayList<Getuseralarmbean>();
        try {
            JSONObject jObj = new JSONObject(resultParam);
            JSONArray ResponseObject = jObj.getJSONArray("ResponseObject");
            for (int i = 0; i < ResponseObject.length(); i++) {

                Log.e("obj", "check");
                JSONObject jResponseObj = ResponseObject.getJSONObject(i);
                Getuseralarmbean getuseralarmbean=new Getuseralarmbean();
                getuseralarmbean.setmAlarmDetailId(getObjectvalue(jResponseObj, "0"));
                getuseralarmbean.setmAlarmId(getObjectvalue(jResponseObj, "0"));
                getuseralarmbean.setmAlarmDate(getObjectvalue(jResponseObj, "166337"));
                getuseralarmbean.setmAlarmType(getObjectvalue(jResponseObj, ""));
                getuseralarmbean.setmFromDatetime(getObjectvalue(jResponseObj, "24-12-2015 03:08:00"));
                getuseralarmbean.setmToDatetime(getObjectvalue(jResponseObj, "26-12-2015 00:00:00"));
                mgetuserlistbean.add(getuseralarmbean);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mgetuserlistbean;
    }*/




    public void parser(String dta, Context context)
    {
        try {

            Helper helper=new Helper(context);
            PlaceName placeName=new PlaceName();
            JSONObject jRoute = new JSONObject(dta);
            //String object=jRoute.getString("");
           JSONObject jRoute2=jRoute.getJSONObject("ResponseObject");
            JSONArray jRouteArray2=jRoute2.getJSONArray("predictions");
            for(int i=0;i<jRouteArray2.length();i++)
            {
                JSONObject innerObject=jRouteArray2.getJSONObject(i);
                String description=innerObject.getString("description");
                String id=innerObject.getString("id");
                String place_id=innerObject.getString("place_id");
                placeName.set_id(id);
                String[] name=description.split(",");
                placeName.set_name(name[0]);
                String adress=null;
                for(int j=1;j<name.length;j++)
                {
                    adress=adress+name[j];

                }
                placeName.set_adress(adress);

                helper.insert_place_names(placeName);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }




    }





    /*public List<Getuseralarmbean> parseGetuserAlarmListDetails(String resultParam) {
        List<Getuseralarmbean> mgetuserlistbean=new ArrayList<Getuseralarmbean>();
        try {
            JSONObject jObj = new JSONObject(resultParam);
            JSONArray ResponseObject = jObj.getJSONArray("ResponseObject");
            for (int i = 0; i < ResponseObject.length(); i++) {

                Log.e("obj", "check");
                JSONObject jResponseObj = ResponseObject.getJSONObject(i);
                Getuseralarmbean getuseralarmbean=new Getuseralarmbean();
                getuseralarmbean.setmAlarmDetailId(getObjectvalue(jResponseObj, "0"));
                getuseralarmbean.setmAlarmId(getObjectvalue(jResponseObj, "0"));
                getuseralarmbean.setmAlarmDate(getObjectvalue(jResponseObj, "166337"));
                getuseralarmbean.setmAlarmType(getObjectvalue(jResponseObj, ""));
                getuseralarmbean.setmFromDatetime(getObjectvalue(jResponseObj, "24-12-2015 03:08:00"));
                getuseralarmbean.setmToDatetime(getObjectvalue(jResponseObj, "26-12-2015 00:00:00"));
                mgetuserlistbean.add(getuseralarmbean);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mgetuserlistbean;
    }*/

    public String parseSaveAlarmDetails(String result) {
        String resultfinal = "";
        try {
            JSONObject jObj = new JSONObject(result);
            resultfinal = jObj.getString("ResponseObject");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultfinal;
    }


    public String parseSaveCarBookingDetails(String result) {
        String resultfinal = "";
        try {
            JSONObject jObj = new JSONObject(result);
            resultfinal = jObj.getString("ResponseObject");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultfinal;
    }

    public WeatherDetails parseWeatherDetails(String resultOutparam) {
        WeatherDetails mWeatherDetail=new WeatherDetails();
        List<WeatherWeekDetails> mWeatherWeekDetails=new ArrayList<WeatherWeekDetails>();
        try {
            JSONObject jObj = new JSONObject(resultOutparam);

            //JSONObject WeatherObj = jObj.getJSONObject("object");
            String IsError = jObj.getString("IsError");
            if(IsError.equalsIgnoreCase("false")){
                JSONObject responseObject = jObj.getJSONObject("ResponseObject");
                if(responseObject==null || responseObject.length()==0){
                }else{
                    JSONObject queryObject = responseObject.getJSONObject("query");
                    if(queryObject==null || queryObject.length()==0){
                    }else{

                        mWeatherDetail.setmCreated(getObjectvalue(queryObject,"created"));

                        JSONObject resultsObject = queryObject.getJSONObject("results");
                        if(resultsObject==null || resultsObject.length()==0){
                        }else{
                            JSONObject channelObject = resultsObject.getJSONObject("channel");
                            if(channelObject==null || channelObject.length()==0){
                            }else{
                                JSONObject locationObject = channelObject.getJSONObject("location");
                                if(locationObject==null ||locationObject.length()==0){
                                }else{
                                    mWeatherDetail.setmCountry(getObjectvalue(locationObject,"country"));
                                    mWeatherDetail.setmCity(getObjectvalue(locationObject, "city"));
                                    mWeatherDetail.setmRegion(getObjectvalue(locationObject, "region"));
                                }
                                JSONObject windObject = channelObject.getJSONObject("wind");
                                if(windObject==null || windObject.length()==0){
                                }else{
                                    mWeatherDetail.setmWindchill(getObjectvalue(windObject,"chill"));
                                    mWeatherDetail.setmWinddirection(getObjectvalue(windObject, "direction"));
                                    mWeatherDetail.setmWindspeed(getObjectvalue(windObject, "speed"));
                                }

                                JSONObject atmosphereObject = channelObject.getJSONObject("atmosphere");
                                if(atmosphereObject==null || atmosphereObject.length()==0){
                                }else{
                                    mWeatherDetail.setmHumidity(getObjectvalue(atmosphereObject, "humidity"));
                                }

                                JSONObject itemObject = channelObject.getJSONObject("item");
                                if(itemObject==null || itemObject.length()==0){
                                }else{
                                    JSONArray forecastArray = itemObject.getJSONArray("forecast");
                                    if(forecastArray==null || forecastArray.length()==0){
                                    }else{
                                        for (int i = 0; i < forecastArray.length(); i++) {
                                            WeatherWeekDetails weatherWeekDetail= new WeatherWeekDetails();
                                            JSONObject weatherWeekObject= forecastArray.getJSONObject(i);

                                            weatherWeekDetail.setmCode(getObjectvalue(weatherWeekObject, "code"));
                                            weatherWeekDetail.setmDate(getObjectvalue(weatherWeekObject, "date"));
                                            weatherWeekDetail.setmDay(getObjectvalue(weatherWeekObject, "day"));
                                            weatherWeekDetail.setmHigh(getObjectvalue(weatherWeekObject, "high"));
                                            weatherWeekDetail.setmLow(getObjectvalue(weatherWeekObject, "low"));
                                            weatherWeekDetail.setmStatus(getObjectvalue(weatherWeekObject, "text"));

                                            mWeatherWeekDetails.add(weatherWeekDetail);
                                        }

                                        mWeatherDetail.setmWeekDetails(mWeatherWeekDetails);
                                    }
                                }
                            }

                        }
                    }
                }

            }else{
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return mWeatherDetail;
    }


   /* public List<FlightNames> parseFlightNames(String jsonResult){
        List<FlightNames> flightNamesList=null;
        try{
            JSONObject jsonObject=new JSONObject(jsonResult);
            JSONObject flightObject=jsonObject.getJSONObject("ResponseObject");
            if(flightObject==null|flightObject.length()==0){
            }else{
                String IsError=jsonObject.getString("IsError");
                if(IsError.equalsIgnoreCase("false")){
                    flightDetailsList=new ArrayList<FlightByDateAndFlightNo>();
                    JSONArray scheduleArray=flightObject.getJSONArray("scheduledFlights");
                    if(scheduleArray==null||scheduleArray.length()==0){
                    }else{
                        for(int i=0;i<scheduleArray.length();i++){
                            JSONObject scheduleArrayObject=scheduleArray.getJSONObject(i);
                            FlightByDateAndFlightNo flightScheduleDetails=new FlightByDateAndFlightNo();
                            flightScheduleDetails.setmCarrierFsCode(getObjectvalue(scheduleArrayObject,"carrierFsCode"));
                            flightScheduleDetails.setmFlightNumber(getObjectvalue(scheduleArrayObject,"flightNumber"));
                            flightScheduleDetails.setmDepartureAirportFsCode(getObjectvalue(scheduleArrayObject,"departureAirportFsCode"));
                            flightScheduleDetails.setmArrivalAirportFsCode(getObjectvalue(scheduleArrayObject,"arrivalAirportFsCode"));
                            flightScheduleDetails.setmDepartureTime(getObjectvalue(scheduleArrayObject,"departureTime"));
                            flightScheduleDetails.setmArrivalTime(getObjectvalue(scheduleArrayObject,"arrivalTime"));
                            flightScheduleDetails.setmAirLinename("Indigo");
                            flightDetailsList.add(flightScheduleDetails);
                        }
                    }

                    JSONObject appendixObject=flightObject.getJSONObject("appendix");
                    JSONArray airlineNamesArray=appendixObject.getJSONArray("airlines");
                    for(int j=0;j<airlineNamesArray.length();j++){
                        JSONObject airlineObject=airlineNamesArray.getJSONObject(j);
                        AvaUtils.mFlightName=getObjectvalue(airlineObject,"name");
                    }


                }else{
                }

               /* JSONArray scheduleArray=flightObject.getJSONArray("scheduledFlights");
                if(scheduleArray==null||scheduleArray.length()==0){
                }else{
                    String IsError=jsonObject.getString("IsError");
                    if(IsError.equalsIgnoreCase("false")){
                        flightDetailsList=new ArrayList<FlightByDateAndFlightNo>();
                        for(int i=0;i<scheduleArray.length();i++){
                            JSONObject scheduleArrayObject=scheduleArray.getJSONObject(i);
                            FlightByDateAndFlightNo flightScheduleDetails=new FlightByDateAndFlightNo();
                            flightScheduleDetails.setmCarrierFsCode(getObjectvalue(scheduleArrayObject,"carrierFsCode"));
                            flightScheduleDetails.setmFlightNumber(getObjectvalue(scheduleArrayObject,"flightNumber"));
                            flightScheduleDetails.setmDepartureAirportFsCode(getObjectvalue(scheduleArrayObject,"departureAirportFsCode"));
                            flightScheduleDetails.setmArrivalAirportFsCode(getObjectvalue(scheduleArrayObject,"arrivalAirportFsCode"));
                            flightScheduleDetails.setmDepartureTime(getObjectvalue(scheduleArrayObject,"departureTime"));
                            flightScheduleDetails.setmArrivalTime(getObjectvalue(scheduleArrayObject,"arrivalTime"));
                            flightDetailsList.add(flightScheduleDetails);
                          }
                    }else{
                    }
                }*/
            }



