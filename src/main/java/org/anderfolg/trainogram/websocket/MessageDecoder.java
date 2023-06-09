package org.anderfolg.trainogram.websocket;

import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import com.google.gson.Gson;
import org.anderfolg.trainogram.entities.chatentities.Message;

public class MessageDecoder implements Decoder.Text<Message> {

    private static Gson gson = new Gson();

    @Override
    public Message decode( String s ) {
        return gson.fromJson(s, Message.class);
    }

    @Override
    public boolean willDecode( String s ) {
        return (s != null);
    }

    @Override
    public void init( EndpointConfig endpointConfig ) {
        //do nothing
    }

    @Override
    public void destroy() {
        //do nothing
    }
}