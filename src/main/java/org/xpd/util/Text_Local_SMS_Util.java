package org.xpd.util;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Text_Local_SMS_Util {
	
	public static void sendSMS(String mob, String msg) {
		try {
			boolean sts = false;
//			Checking the length of Mobile Number than adding/removing country code...
			if (mob.length() == 13) {
				mob = mob.substring(1, 13);
				sts = true;
				System.out.println("12 digit mob ==> "+mob);
			} else if(mob.length() == 10) {
				mob = "91"+mob;
				sts = true;
				System.out.println("10 digit mob ==> "+mob);
			} else {
				System.out.println("Wrong Mobile Number or No Mobile Number Found\n"+mob);
				
			}
			
			if (sts) {
				String apiKey = "apikey=" + "wpAFCPj70K8-KbOxHa2Ltnw4y9oJsSRRMxGWBclQas";
				String message = "&message=" + msg;
				String sender = "&sender=" + "XPEDIZ";
				String numbers = "&numbers=" + mob;
				
				HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
				String data = apiKey + numbers + message + sender;
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
				conn.getOutputStream().write(data.getBytes("UTF-8"));
				final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				final StringBuffer stringBuffer = new StringBuffer();
				String line;
				while ((line = rd.readLine()) != null) {
					stringBuffer.append(line);
				}
				rd.close();
				System.out.println(stringBuffer.toString());
			}

		}
		catch(Exception e) {
			
		}
		
	}
	
	public static void main(String[] args) {
		int noOfInvoices = 5;
    	double amt = 12250.43;
    	String message = "XPEDIZE IT! "+noOfInvoices+
    					 " New Invoices totalling INR "+amt+" "
    					 		+ "from Sheela Foam have been uploaded on Xpedize. Please login at https://sf.xpedize.com"
    						;
    	sendSMS("9728582174", message);
	}


}
