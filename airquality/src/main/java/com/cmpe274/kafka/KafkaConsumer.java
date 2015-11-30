/**
 * 
 */
package com.cmpe274.kafka;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.cmpe274.db.DbManager;
import com.cmpe274.model.ResponseData;
import com.cmpe274.model.ResponseDataArray;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

/**
 * @author madhur
 *
 */
public class KafkaConsumer extends Thread{

	final static String clientId = "SimpleConsumerDemoClient";
    final static String TOPIC = "myTopic";
    ConsumerConnector consumerConnector;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public KafkaConsumer() {	
		Properties properties = new Properties();
        properties.put("zookeeper.connect","localhost:2181");
        properties.put("group.id","test-group");
        ConsumerConfig consumerConfig = new ConsumerConfig(properties);
        consumerConnector = Consumer.createJavaConsumerConnector(consumerConfig);
        this.start();
	}
    
    public void run() {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(TOPIC, new Integer(1));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumerConnector.createMessageStreams(topicCountMap);
        System.out.println(consumerMap.isEmpty());
        KafkaStream<byte[], byte[]> stream =  consumerMap.get(TOPIC).get(0);
        System.out.println(stream.isEmpty());
        ConsumerIterator<byte[], byte[]> it = stream.iterator();
        while(it.hasNext()) {
        	MessageDecoder decoder = new MessageDecoder();
            ResponseDataArray array = decoder.fromBytes(it.next().message());
            insertIntoDb(array);
        }
    }
    
    public void insertIntoDb(ResponseDataArray array) {
    	ResponseData[] datas = array.getData();
    	for(int i=0;i<datas.length;i++) {
    		transformAndInsert(datas[i]);
    	}
    }
    
    public void transformAndInsert(ResponseData data) {
    	DbManager dbManager = DbManager.getDbCon();
    	Calendar calendar = Calendar.getInstance();
    	try {
    		double latitude = Double.parseDouble(data.getLatitude());
        	double longitude = Double.parseDouble(data.getLongitude());
			calendar.setTime(formatter.parse(data.getUTC()));
			calendar.add(Calendar.HOUR, -8);
			Date date = calendar.getTime();
			String parameter = data.getParameter();
			String unit = data.getUnit();
			double value = Double.parseDouble(data.getValue());
			double aqi = Double.parseDouble(data.getAQI());
			int category = Integer.parseInt(data.getCategory());
			String query = "insert into analysis(Latitude,Longitude,observedTime,parameter,unit,value,aqi,category) values("+latitude+","+longitude+",'"
			+formatter1.format(date)+"','"+parameter+"','"+unit+"',"+value+","+aqi+","+category+")";
			dbManager.insert(query);
			System.out.println(query);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public static void main(String[] args) {
		KafkaConsumer helloKafkaConsumer = new KafkaConsumer();
	}
}
