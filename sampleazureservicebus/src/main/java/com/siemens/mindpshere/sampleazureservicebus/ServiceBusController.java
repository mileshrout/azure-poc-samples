package com.siemens.mindpshere.sampleazureservicebus;

import com.microsoft.azure.servicebus.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;
import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.core.Builder;
import com.microsoft.windowsazure.services.servicebus.ServiceBusConfiguration;
import com.microsoft.windowsazure.services.servicebus.ServiceBusContract;
import com.microsoft.windowsazure.services.servicebus.ServiceBusService;
import com.microsoft.windowsazure.services.servicebus.models.CreateTopicResult;
import com.microsoft.windowsazure.services.servicebus.models.TopicInfo;

import java.util.Collection;

/**
 * Created by n0qnvq on 7/13/2017.
 */
@RestController
@RequestMapping("/servicebus")
public class ServiceBusController {
	private static final int NUMBER_OF_MESSAGES = 1;
	private QueueClient queueClient;

	@RequestMapping(method = RequestMethod.GET)
    public String send(){
        System.out.println("Begining send sample.");
        
        StringBuilder response = new StringBuilder("Successfully sent");

        //Sample URL : sb://namespace_DNS_Name;EntityPath=EVENT_HUB_NAME;SharedAccessKeyName=SHARED_ACCESS_KEY_NAME;SharedAccessKey=SHARED_ACCESS_KEY
        //String connectionString = "Endpoint=sb://formindspherepoc.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=hrJiwSAuA98jdbELaMaGo59rs9enZd4UfA1uzaHgacY=";
        // String connectionString = "Endpoint=sb://mindspherepoc.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=sT0SFEPwI4WZViBF6xiRr4cQ/p9HsDCqGB38zuQDEkM=";

        try{
        	
            /*QueueClient queueClient = new QueueClient(connectionString, ReceiveMode.PeekLock);
            

            sendMessages(NUMBER_OF_MESSAGES);
            queueClient.close();*/
        	
        	
        	

			/*Configuration configuration = ServiceBusConfiguration.configureWithWrapAuthentication("mindspherepoc",
					"owner", "sT0SFEPwI4WZViBF6xiRr4cQ/p9HsDCqGB38zuQDEkM=",
					"https://mindspherepoc.servicebus.windows.net:443/", "-sb.accesscontrol.windows.net/WRAPv0.9");
			ServiceBusContract service = ServiceBusService.create(configuration);
			TopicInfo topicInfo = new TopicInfo("TestTopicMilesh");
			CreateTopicResult result = service.createTopic(topicInfo);
			response = result.getValue().getPath();*/

            //https://MindspherePoc.servicebus.windows.net/testtopic
			
        	
        	//Sending the message to Topic
			ConnectionStringBuilder conStrToTopic = new ConnectionStringBuilder("mindspherepoc",
                    "topicforpoc", "RootManageSharedAccessKey",
                    "sT0SFEPwI4WZViBF6xiRr4cQ/p9HsDCqGB38zuQDEkM=");
			TopicClient topicClient = new TopicClient(conStrToTopic.toString());

	        for(int i = 1; i <= 2; i ++) {
	            Message message = new Message("Test");
	            message.setMessageId(Integer.toString(i*2));
	            message.setSessionId(Integer.toString(i*2));
	            topicClient.send(message);
                response.append("{ MessageToTopic[SessionId:["+ message.getSessionId() +"] MessageId:[" + message.getMessageId()+"] },");
	        }
	        response.append("{ Message sent to Topic client id["+topicClient.getClientId()+"] entity path["+topicClient.getEntityPath()+"]},");
	        topicClient.close();

	        //Sending message to Queue
	        ConnectionStringBuilder conStrSend = new ConnectionStringBuilder("mindspherepoc",
                    "testqueue", "RootManageSharedAccessKey",
                    "sT0SFEPwI4WZViBF6xiRr4cQ/p9HsDCqGB38zuQDEkM=");
	        QueueClient queueClient = new QueueClient(conStrSend.toString(), ReceiveMode.PeekLock);
	        for(int i = 1; i <= 2; i ++) {
	            Message message = new Message("TestOnQueue");
	            message.setMessageId(Integer.toString(i*2));
	            message.setSessionId(Integer.toString(i*2));
	            queueClient.send(message);
				response.append("{ MessageToQueue[SessionId:[" + message.getSessionId() + "] MessageId:[ " + message.getMessageId()+"]},");
	        }
	        response.append("{ Message sent to Queue client id["+queueClient.getClientId()+"] entity path["+queueClient.getEntityPath()+"]},");
	        queueClient.close();

	        // Receiving Message from Subscription
            /*ConnectionStringBuilder conStrReceive = new ConnectionStringBuilder("mindspherepoc",
                    "topicforpoc", "RootManageSharedAccessKey",
                    "sT0SFEPwI4WZViBF6xiRr4cQ/p9HsDCqGB38zuQDEkM=");
            SubscriptionClient subscriptionClient = new SubscriptionClient(conStrReceive.toString(), ReceiveMode.PeekLock);
            Collection<IMessageSession> sess= subscriptionClient.getMessageSessions();
            response.append("{Message received Sessions: [" + sess.size()+"]}");
			subscriptionClient.close();*/

        } catch (Exception e) {
            response.append("{ Exception : "+e.getMessage()+"}");
            System.err.println("Exception Caught : "+e);
        }
		return response.toString();
    }


    private  void sendMessages(int numMessages) throws InterruptedException, ServiceBusException {
        for(int i = 0; i < numMessages; i ++) {
            String messageBody = "MessageNumberForMindsphere: " + i;
            Message message = new Message(messageBody.getBytes());
            queueClient.send(message);
            System.out.println("Sending message " + message);
        }
    }
}
