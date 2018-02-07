/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamj.websocketcrud;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author dennys
 */
@ServerEndpoint(value = "/directorio", encoders = {MessageEncoder.class}, decoders = {MessageDecoder.class})
public class PersonasEndPoint {

    private static final Logger logger = Logger.getLogger("PersonasEndPoint");
    /* Queue for all open WebSocket sessions */
    static Queue<Session> queue = new ConcurrentLinkedQueue<>();
    static List<Persona> personas = new ArrayList<>();

    /* PriceVolumeBean calls this method to send updates */
    public static void send(String msg) {
        try {
            /* Send updates to all open WebSocket sessions */
            for (Session session : queue) {
                session.getBasicRemote().sendText(msg);
                //logger.log(Level.INFO, "Sent: {0}", msg);
            }
        } catch (IOException e) {
            logger.log(Level.INFO, e.toString());
        }
    }

    @OnOpen
    public void openConnection(Session session) {
        /* Register this connection in the queue */
        queue.add(session);
        // personas.add(new Persona("Dennys","dennysaurio@gmail.com"));
        logger.log(Level.INFO, "Connection opened.");
    }

    @OnClose
    public void closedConnection(Session session) {
        /* Remove this connection from the queue */
        queue.remove(session);
        logger.log(Level.INFO, "Connection closed.");
    }

    @OnError
    public void error(Session session, Throwable t) {
        /* Remove this connection from the queue */
        queue.remove(session);
        logger.log(Level.INFO, t.toString());
        logger.log(Level.INFO, "Connection error.");
    }

    @OnMessage
    public void onMessage(Session client, String text) {

        logger.log(Level.INFO, text);
        String[] comando = text.split(";");
        if (comando.length > 0) {
            switch (comando[0]) {
                case "getAll": {
                    try {
                        client.getBasicRemote().sendObject(personas);

                    } catch (IOException | EncodeException ex) {
                        Logger.getLogger(PersonasEndPoint.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
                case "add": {
                    try {
                        personas.add(new Gson().fromJson(comando[1], Persona.class));
                        client.getBasicRemote().sendObject(personas);

                    } catch (IOException | EncodeException ex) {
                        Logger.getLogger(PersonasEndPoint.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
                 case "addAll": {
                    try {
                        personas.clear();
                        List<Persona> personasRecibidas = new Gson().fromJson(comando[1], new TypeToken<List<Persona>>(){}.getType());

                        personas.addAll(personasRecibidas);
                        client.getBasicRemote().sendObject(personas);

                    } catch (IOException | EncodeException ex) {
                        Logger.getLogger(PersonasEndPoint.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
                 case "edit": {
                    try {
                        personas.set(Integer.valueOf(comando[1]),new Gson().fromJson(comando[2], Persona.class));
                        client.getBasicRemote().sendObject(personas);

                    } catch (IOException | EncodeException ex) {
                        Logger.getLogger(PersonasEndPoint.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
                 case "delete": {
                    try {
                        personas.remove(Integer.valueOf(comando[1]).intValue());
                        client.getBasicRemote().sendObject(personas);

                    } catch (IOException | EncodeException ex) {
                        Logger.getLogger(PersonasEndPoint.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
            }

        } else {
            logger.log(Level.INFO, "no se han enviado comandos");
        }

    }

}
