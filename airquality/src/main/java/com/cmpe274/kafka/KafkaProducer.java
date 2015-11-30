/**
 * 
 */
package com.cmpe274.kafka;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.cmpe274.model.ResponseData;
import com.cmpe274.model.ResponseDataArray;
import com.google.gson.Gson;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

/**
 * @author madhur
 *
 */
public class KafkaProducer implements Job{
	SimpleDateFormat dateFormatUtc = new SimpleDateFormat("yyyy-MM-dd'T'HH");
	SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String baseURI = "http://www.airnowapi.org/aq/data/?";
	String[] parameterArray = {"O3", "PM25", "PM10", "CO", "NO2", "SO2"};
	String bbox = "-124.594714,32.516415,-114.487292,41.809959";
	String format = "application/json";
	String API_KEY = "86B610AE-A994-4CF1-95E3-4D1D06E674DF";
	String basePath = "/Users/madhur/Documents/cmpe274/data/";
	String TOPIC = "myTopic";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		KafkaProducer kafkaProducer = new KafkaProducer();
		kafkaProducer.deleteFiles();


	}

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		downloadFiles();
		extractFilesAndSend();
		deleteFiles();
	}

	public void downloadFiles() {
		String startDate = "";
		String endDate = "";
		String uri = "";
		dateFormatUtc.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date localTime = new Date();
		endDate = dateFormatUtc.format(localTime);
		System.out.println(dateFormatUtc.format(localTime));
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(localTime);
		calendar.add(Calendar.HOUR, -1);
		startDate = dateFormatUtc.format(calendar.getTime());
		System.out.println(dateFormatUtc.format(calendar.getTime()));
		for(int i=0;i<parameterArray.length;i++) {
			try {
				uri = baseURI + "startDate="+startDate + "&endDate=" + endDate 
						+ "&parameters=" + parameterArray[i] + "&BBOX=" + bbox + "&dataType=B&format="
						+ format + "&verbose=1&API_KEY=" + API_KEY;
				System.out.println(uri);
				FileUtils.copyURLToFile(new URL(uri), new File( basePath + startDate + "-" + endDate + "-" +parameterArray[i] + ".json"));
			}catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
	}

	public void extractFilesAndSend() {
		BufferedReader br = null;
		Gson gson = new Gson();
		File dir = new File(basePath);
		if(dir.isDirectory()) {
			File[] files = dir.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return !name.equals(".DS_Store");
				}
			});
			for(int i=0;i<files.length;i++) {
				try {
					br = new BufferedReader(new FileReader(files[i].getAbsolutePath()));
					ResponseData[] res = gson.fromJson(br, ResponseData[].class);
					ResponseDataArray array = new ResponseDataArray();
					array.setData(res);
					Properties properties = new Properties();
					properties.put("metadata.broker.list","localhost:9092");
					properties.put("serializer.class","kafka.serializer.DefaultEncoder");
					ProducerConfig producerConfig = new ProducerConfig(properties);
					Producer<String,byte[]> producer = new Producer<String, byte[]>(producerConfig);
					MessageEncoder encoder = new MessageEncoder();
					byte[] bytes = encoder.toBytes(array);
					KeyedMessage<String, byte[]> message =new KeyedMessage<String, byte[]>(TOPIC, bytes);
					System.out.println("before sending message");
					producer.send(message);
			        producer.close();
					System.out.println(res[0].getParameter());
					if(br != null) {
						br.close();
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if(br != null) {
						try {
							br.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						br = null;
					}
				}
			}
		}
	}

	public void deleteFiles() {
		try {
			FileUtils.cleanDirectory(new File(basePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
