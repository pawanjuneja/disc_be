package org.xpd.util;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.SubscribeRequest;


public class AWS_SMS_Util {
	
	public static void sendSMSMessage(String message, String Arn) {
		String KEY = null;
	    String val = null;
	    AmazonSNSClient snsClient = new AmazonSNSClient(new  BasicAWSCredentials(KEY, val));
	    snsClient.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_1));
	    
	    Map<String, MessageAttributeValue> smsAttributes =
                new HashMap<String, MessageAttributeValue>();
        smsAttributes.put("AWS.SNS.SMS.SenderID", new MessageAttributeValue()
                .withStringValue("XPEDIZ") //The sender ID shown on the device.
                .withDataType("String"));
        smsAttributes.put("AWS.SNS.SMS.MaxPrice", new MessageAttributeValue()
                .withStringValue("10.0") //Sets the max price to 10 USD.
                .withDataType("Number"));
        smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue()
                .withStringValue("Transactional") //Sets the type to promotional.
                .withDataType("String"));
		PublishResult result = snsClient.publish(new PublishRequest()
                .withMessage(message)
                .withTopicArn(Arn)
                .withMessageAttributes(smsAttributes));
System.out.println("Message Sent Successfully With Message ID: "+result.getMessageId());
				}
	
	

	public static void main(String[] args) {
        String message = "Subscribed To Xpedize SMS Notifications";
        String topicARN = "arn:aws:sns:ap-southeast-1:561503721240:XPD-vishalahlaw";
        sendSMSMessage(message, topicARN);
	}

}
