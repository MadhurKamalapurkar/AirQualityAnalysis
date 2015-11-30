/**
 * 
 */
package com.cmpe274.kafka;

import java.io.IOException;

import com.cmpe274.model.ResponseDataArray;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kafka.serializer.Decoder;

/**
 * @author madhur
 *
 */
public class MessageDecoder implements Decoder<ResponseDataArray>{

	public ResponseDataArray fromBytes(byte[] arg0) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(arg0, ResponseDataArray.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
