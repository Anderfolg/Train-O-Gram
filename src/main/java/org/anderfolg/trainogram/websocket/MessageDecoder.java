package org.anderfolg.trainogram.websocket;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import com.google.gson.Gson;
import org.anderfolg.trainogram.entities.chatEntities.Message;

public class MessageDecoder implements Decoder.Text<Message> {

    private static Gson gson = new Gson();

    @Override
    public Message decode( String s ) throws DecodeException {
        return gson.fromJson(s, Message.class);
    }

    @Override
    public boolean willDecode( String s ) {
        return (s != null);
    }

    @Override
    public void init( EndpointConfig endpointConfig ) {

    }

    @Override
    public void destroy() {

    }
}