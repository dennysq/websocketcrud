/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamj.websocketcrud;

import com.google.gson.Gson;
import java.io.StringWriter;
import java.util.List;
import javax.json.Json;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 *
 * @author dennys
 */
public class MessageEncoder implements Encoder.Text<List<String>> {

  /**
   * Encode the instance of MyMessage into a JSON string.
   */
  @Override
  public String encode(List<String> myMsg) throws EncodeException {

  
    return new Gson().toJson(myMsg);
  }

  @Override
  public void init(EndpointConfig config) {
  }

  @Override
  public void destroy() {
  }
    
}
