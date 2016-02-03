package org.ACMSviet.SchedulerAMa.Services;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;



import java.net.URL;

import org.ACMSviet.SchedulerAMa.Models.Content;

import com.google.gson.Gson;

public class Post2Gcm {
	public static String post(String apiKey, Content content) {
		try {
			HttpURLConnection connect = (HttpURLConnection) new URL("https://android.googleapis.com/gcm/send").openConnection();
			connect.setRequestMethod("POST");
			connect.setRequestProperty("Content-Type", "application/json");
			connect.setRequestProperty("Authorization", "key="+apiKey);
			connect.setDoOutput(true);
			connect.getOutputStream().write(new Gson().toJson(content).getBytes("UTF-8"));
			
			if(connect.getResponseCode()==200) {
				return new Gson().toJson(content) ;
			}
			else {
				return "status FAILED";
			}
			
		}catch(MalformedURLException e) {
			return e.getMessage();
		}catch(IOException eio) {
			return eio.getMessage();
		}
	}
	
}
