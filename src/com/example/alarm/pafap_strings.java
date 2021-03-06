package com.example.alarm;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class pafap_strings {
	String error;
	public pafap_strings(){}
	
	public String replaceString(String str, String from, String to){
		String tmp = str.replace(from, to);
		return tmp;
	}
	
	public String getLatFromSms(String str){
		String lat = "";
		if (str.indexOf("Latitude = ") != -1){
			int latind = str.indexOf("Latitude = ");
			lat = str.substring(latind + 11, str.indexOf("N "));
			double tmp = cnvDegree2Decimal(lat);
			DecimalFormat df = new DecimalFormat("##.######");
			lat = df.format(tmp);
			lat = clearString(lat);
		}
		else if (str.indexOf("Latitude=") != -1){
			int latind = str.indexOf("Latitude=");
			lat = str.substring(latind + 9, str.indexOf("N L"));
			//double tmp = cnvDegree2Decimal(lat);
			//DecimalFormat df = new DecimalFormat("##.######");
			//lat = df.format(tmp);
			lat = clearString(lat);
		}
		else if (str.indexOf("lat:") != -1){
			int latidx = str.indexOf("lat:");
			lat = str.substring(latidx + 4, latidx + 4 + 9);
			lat = clearString(lat);
		}
		else if(str.indexOf("&q=") != -1){
			int latind = str.indexOf("&q=");
			lat = str.substring(latind + 3, str.indexOf(","));
			lat = clearString(lat);
		}
		else if(str.indexOf("?q=") != -1){
			int latind = str.indexOf("?q=");
			lat = str.substring(latind + 3, str.indexOf(","));
			lat = clearString(lat);
		}
		return lat;
	}
	
	public double cnvDegree2Decimal(String str){
		double cnv = 0.00;
		String[] string = str.split("\\s+");
		cnv = Double.parseDouble(string[1]) * 60 + Double.parseDouble(string[2]);
		cnv = cnv / 3600 + Double.parseDouble(string[0]);
		return cnv;
	}
	
	public String getLngFromSms(String str){
		String lng = "";
		if(str.indexOf("Longitude = ") != -1){
			int lngind = str.indexOf("Longitude = ");
			lng = str.substring(lngind + 12, str.indexOf("E,S"));
			double tmp = cnvDegree2Decimal(lng);
			DecimalFormat df = new DecimalFormat("##.######");
			lng = df.format(tmp);
			lng = clearString(lng);
		}
		else if(str.indexOf("Longitude=") != -1){
			int lngind = str.indexOf("Longitude=");
			lng = str.substring(lngind + 10, str.indexOf("E, s"));
			//double tmp = cnvDegree2Decimal(lng);
			//DecimalFormat df = new DecimalFormat("##.######");
			//lng = df.format(tmp);
			lng = clearString(lng);
		}
		else if (str.indexOf("long:") != -1){
			int lngidx = str.indexOf("long:");
			lng = str.substring(lngidx + 5, lngidx + 5 + 9);
			lng = clearString(lng);
		}
		else if (str.indexOf(",") != -1){
			int lngind = str.indexOf(",");
			lng = str.substring(lngind + 1, str.indexOf("&ie="));
			lng = clearString(lng);
		}
		return lng;
	}
	
	public String clearString(String str){
		str = str.replaceAll("\\s", "");
		if (str.startsWith("0")){
			str = str.substring(1);
		}
		return str;
	}
	
	public String getSender(String str){
		String tmp = "";
		if (str.indexOf("from:") != -1){
			int snd = str.indexOf("from:");
			tmp = str.substring(snd + 5, snd + 5 + 13);
		}
		return tmp;
	}
	
	public double str2double(String str){
		double tmp = 00.000000;
		try{
			tmp = Double.parseDouble(str);
			return tmp;
		}catch (NumberFormatException ex){
			error = ex.getMessage();
			return tmp;
		}
	}
	
	public String getError(){
		return this.error;
	}
	
	public String md5(String in) {
	    MessageDigest digest;
	    try {
	        digest = MessageDigest.getInstance("MD5");
	        digest.reset();
	        digest.update(in.getBytes());
	        byte[] a = digest.digest();
	        int len = a.length;
	        StringBuilder sb = new StringBuilder(len << 1);
	        for (int i = 0; i < len; i++) {
	            sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
	            sb.append(Character.forDigit(a[i] & 0x0f, 16));
	        }
	        return sb.toString();
	    } catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
	    return null;
	}
	
	public boolean checkSMS(String sms){
		ArrayList<String> keys = new ArrayList<String>();
		boolean founded = false;
		keys.add("POWER FAIL");
		keys.add("POWER OK");
		keys.add("PANEL DISARMED");
		keys.add("FIRE ALARM");
		keys.add("PANEL ARMED");
		keys.add("PANIC ALARM");
		for (int i=0;i<keys.size();i++){
			if (sms.indexOf(keys.get(i)) != -1){
				founded = true;
			}
		}
		return founded;
	}
	
	//check sms content for color
	public String checkSMS4Color(String sms){
		String color = "#909090";
		ArrayList<String> keys = new ArrayList<String>();

		keys.add("PANEL ARMED");
		keys.add("PANEL DISARMED");
		keys.add("PANIC ALARM");
		keys.add("POWER FAIL");
		keys.add("POWER OK");

		for (int i=0;i<keys.size();i++){
			if (sms.indexOf(keys.get(i)) != -1){
				if (keys.get(i).equalsIgnoreCase("PANEL ARMED")){
					color = "#FB032C";
					break;
				}
				else if (keys.get(i).equalsIgnoreCase("PANEL DISARMED")){
					color = "#37E219";
					break;
				}
				else if (keys.get(i).equalsIgnoreCase("PANIC ALARM")){
					color = "#FB7F03";
					break;
				}
				else if (keys.get(i).equalsIgnoreCase("POWER FAIL")){
					color = "#7C3131";
					break;
				}
				else if (keys.get(i).equalsIgnoreCase("POWER OK")){
					color = "#317C4B";
					break;
				}
			}
		}
		return color;
	}
	
	//check sms content for command
	public String checkSMS4Commad(String sms){
		String cmd = "default";
		ArrayList<String> keys = new ArrayList<String>();

		keys.add("PANEL ARMED");
		keys.add("PANEL DISARMED");
		keys.add("PANIC ALARM");
		keys.add("POWER FAIL");
		keys.add("POWER OK");

		for (int i=0;i<keys.size();i++){
			if (sms.indexOf(keys.get(i)) != -1){
				if (keys.get(i).equalsIgnoreCase("PANEL ARMED")){
					cmd = "Arm";
					break;
				}
				else if (keys.get(i).equalsIgnoreCase("PANEL DISARMED")){
					cmd = "Disarm";
					break;
				}
				else if (keys.get(i).equalsIgnoreCase("PANIC ALARM")){
					cmd = "Panic";
					break;
				}
				else if (keys.get(i).equalsIgnoreCase("POWER FAIL")){
					cmd = "Power";
					break;
				}
				else if (keys.get(i).equalsIgnoreCase("POWER OK")){
					cmd = "Power";
					break;
				}
			}
		}
		return cmd;
	}
	
	public String checkPrefix(String sender){
		ArrayList<String> prefx = new ArrayList<String>();
		String founded = "";
		prefx.add("+355");
		prefx.add("+39");
		prefx.add("+30");
		prefx.add("+389");
		for (int i=0;i<prefx.size();i++){
			if (sender.indexOf(prefx.get(i)) != -1){
				founded = prefx.get(i).toString();
			}
		}
		return founded;
	}
	
	public boolean isEmail(String email){
		Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
		Matcher matcher = pattern.matcher(email);
		boolean matchFound = matcher.matches();
		return matchFound;
	}
}
