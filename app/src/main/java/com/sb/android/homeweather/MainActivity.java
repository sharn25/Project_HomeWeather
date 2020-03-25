package com.sb.android.homeweather;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sb.android.homeweather.Utils.Utils;
import com.sb.android.homeweather.logger.log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

  private static String API_WEATHER = "94f936a07de01954465169c3d03a95b9";
  private String CITY_WEATHER="6619347";
  private String[] month=null;
  private String[] day=null;
  private boolean pause=false;
  private boolean isWeatherRunning=false;
  private int olddate=0;

  private weatherinfo weatherInfo;
  private JSONObject weatherdata;

  private RelativeLayout main_rl;
  private TextView datetv;
  private TextView citytv;
  private TextView temptv;
  private TextView humlbtv;
  private TextView humtv;
  private TextView cloudlbtv;
  private TextView cloudtv;
  private TextView windlbtv;
  private TextView windtv;
  ImageView iconiv;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    main_rl = (RelativeLayout) findViewById(R.id.main_rl);
    datetv = (TextView) findViewById(R.id.datetv);
    citytv = (TextView) findViewById(R.id.citytv);
    temptv = (TextView) findViewById(R.id.temptv);
    humlbtv = (TextView) findViewById(R.id.humlbtv);
    humtv = (TextView) findViewById(R.id.humtv);
    cloudlbtv = (TextView) findViewById(R.id.cloudlbtv);
    cloudtv = (TextView) findViewById(R.id.cloudtv);
    windlbtv = (TextView) findViewById(R.id.windlbtv);
    windtv = (TextView) findViewById(R.id.windtv);
    iconiv = (ImageView) findViewById(R.id.iconiv);

    log.d("MainActivity","Enter Weather Oncreate");
    this.month = new String[] {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
    day = new String[] {"SUNDAY","MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY"};
    weatherInfo = new weatherinfo();

    log.d("MainActivity","Starting Updater");
    updater();
  }

  private void updater(){
    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        if(!pause) {
          weatherup();
        }
      }
    },0,150000);//Update text every second
  }

  private void weatherup(){
    pause = true;
    log.d("MainActivity","Entered weatherup");
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        Calendar c = Calendar.getInstance();
        int date = c.get(Calendar.DATE);
        if(olddate!=date){
          log.d("Mainactivity","EnterOlddate");
          olddate=date;
          String days = day[c.get(Calendar.DAY_OF_WEEK)-1];
          String months = month[c.get(Calendar.MONTH)-1];
          int year = c.get(Calendar.YEAR);
          datetv.setText(days + ", " + date + " " + months + " " + year);
        }
        if(!isWeatherRunning){
          AsyncTaskRunner runner = new AsyncTaskRunner();
          runner.execute();
        }
        pause=false;
      }
    });
  }

  private class AsyncTaskRunner extends AsyncTask<String, String, String> {
    @Override
    protected String doInBackground(String... params) {

      weatherdata= Utils.getJSON("http://api.openweathermap.org/data/2.5/weather?id=" + CITY_WEATHER + "&APPID=" + API_WEATHER + "&units=metric");
      return null;
    }


    @Override
    protected void onPostExecute(String result) {
      if(weatherdata!=null){
        log.d("MainActivity", "Output(Weatehr) : " +weatherdata.toString());
        try{

          JSONObject weather = weatherdata.getJSONArray("weather").getJSONObject(weatherdata.getJSONArray("weather").length()-1);
          JSONObject main = weatherdata.getJSONObject("main");
          JSONObject wind = weatherdata.getJSONObject("wind");
          JSONObject clouds = weatherdata.getJSONObject("clouds");
          weatherInfo.setCity(weatherdata.getString("name"));
          weatherInfo.setDescription(weather.getString("description"));
          weatherInfo.setIcon(weather.getString("icon"));
          weatherInfo.setTemp(main.getInt("temp"));
          weatherInfo.setHumidity(main.getInt("humidity"));
          weatherInfo.setWinddeg(wind.getInt("deg"));
          weatherInfo.setWindspeed(wind.getDouble("speed"));
          weatherInfo.setClounds(clouds.getInt("all"));
          log.d("MainActivity","City : " + weatherInfo.getCity()+" Des. ; "+weatherInfo.getDescription()+" Icon : "+weatherInfo.getIcon()+" Temp : "+weatherInfo.getTemp()+" Wind Speed : " +weatherInfo.getWindspeed() );

        }catch(Exception e){
          log.d("MainActivity","errorPost : " +e);
        }
        String bgcolor;
        String textcolor;
        if(weatherInfo.getIcon().indexOf("n")!=-1){
          bgcolor = "#1d1756";
          textcolor = "#FFFFFFFF";
        }else{
          bgcolor = "#55c6ee";
          textcolor = "#FF000000";
        }
        main_rl.setBackgroundColor(Color.parseColor(bgcolor));
        citytv.setText(weatherInfo.getCity());
        citytv.setTextColor(Color.parseColor(textcolor));
        temptv.setText(weatherInfo.getTemp()+"\u00B0");
        temptv.setTextColor(Color.parseColor(textcolor));
        humlbtv.setTextColor(Color.parseColor(textcolor));
        humtv.setText(weatherInfo.getHumidity()+"%");
        cloudlbtv.setTextColor(Color.parseColor(textcolor));
        cloudtv.setText(weatherInfo.getClounds()+"%");
        cloudtv.setTextColor(Color.parseColor(textcolor));
        windlbtv.setTextColor(Color.parseColor(textcolor));
        windtv.setText("N " + weatherInfo.getWindspeed() + " km/h");
        windtv.setTextColor(Color.parseColor(textcolor));
        setimageview(weatherInfo.getIcon());
      }
      isWeatherRunning=false;
     }


    @Override
    protected void onPreExecute() {
      isWeatherRunning=true;
    }


    @Override
    protected void onProgressUpdate(String... text) {

    }
  }

  private  void setimageview(String id){
    switch(id){
      case "01d":
        iconiv.setImageResource(R.drawable.weather_01d_clear_day);
        break;
      case "01n":
        iconiv.setImageResource(R.drawable.weather_01n_clear_night);
        break;
      case "02d":
        iconiv.setImageResource(R.drawable.weather_02d_partly_cloud_day);
        break;
      case "02n":
        iconiv.setImageResource(R.drawable.weather_02n_partly_cloud_night);//replace with night few clouds
        break;
      case "03d":
        iconiv.setImageResource(R.drawable.weather_03dn_04dn_cloudy);
        break;
      case "03n":
        iconiv.setImageResource(R.drawable.weather_03dn_04dn_cloudy);
        break;
      case "04d":
        iconiv.setImageResource(R.drawable.weather_03dn_04dn_cloudy);//repalce with double clouds
        break;
      case "04n":
        iconiv.setImageResource(R.drawable.weather_03dn_04dn_cloudy);//repalce with double clouds
        break;
      case "09d":
        iconiv.setImageResource(R.drawable.weather_09dn_shower_rain);
        break;
      case "09n":
        iconiv.setImageResource(R.drawable.weather_09dn_shower_rain);
        break;
      case "10d":
        iconiv.setImageResource(R.drawable.weather_10d_rainy_day);
        break;
      case "10n":
        iconiv.setImageResource(R.drawable.weather_10n_rainy_night);//raplce  with night rain
        break;
      case "11d":
        iconiv.setImageResource(R.drawable.weather_11dn_tunderstrom);
        break;
      case "11n":
        iconiv.setImageResource(R.drawable.weather_11dn_tunderstrom);
        break;
      case "13d":
        iconiv.setImageResource(R.drawable.weather_13dn_snow);
        break;
      case "13n":
        iconiv.setImageResource(R.drawable.weather_13dn_snow);
        break;
      case "50d":
        iconiv.setImageResource(R.drawable.weather_50d_mist);
        break;
      case "50n":
        iconiv.setImageResource(R.drawable.weather_50d_mist);
        break;
      default:
        iconiv.setImageDrawable(null);
        break;

    }
  }

}