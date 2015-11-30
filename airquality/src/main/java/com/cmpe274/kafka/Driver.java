/**
 * 
 */
package com.cmpe274.kafka;

import java.text.ParseException;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author madhur
 *
 */
public class Driver {

	/**
	 * @param args
	 * @throws ParseException 
	 * @throws SchedulerException 
	 */
	public static void main(String[] args) throws ParseException, SchedulerException {
		JobDetail job = new JobDetail();
		job.setName("dummyJobName");
    	job.setJobClass(KafkaProducer.class);
		
		CronTrigger cronTrigger = new CronTrigger();
		cronTrigger.setName("dummyTriggerName");
		cronTrigger.setCronExpression("0/30 * * * * ?");
		
		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		scheduler.start();
    	scheduler.scheduleJob(job, cronTrigger);

	}

}
