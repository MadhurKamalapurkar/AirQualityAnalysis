/**
 * 
 */
package com.cmpe274.kafka;

import java.io.IOException;

import com.cmpe274.model.ResponseDataArray;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kafka.serializer.Encoder;

/**
 * @author madhur
 *
 */
public class MessageEncoder implements Encoder<ResponseDataArray>{

	public byte[] toBytes(ResponseDataArray arg0) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.writeValueAsString(arg0).getBytes();
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "".getBytes();
	}

}
