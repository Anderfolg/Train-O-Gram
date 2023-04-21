package org.anderfolg.trainogram.websocket;

import com.google.gson.Gson;
import org.anderfolg.trainogram.entities.chatentities.Message;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<Message> {

    private static Gson gson = new Gson();

    @Override
    public String encode(Message message) {
        return gson.toJson(message.getText());
    }

    @Override
    public void init( EndpointConfig endpointConfig) {
        // Custom initialization logic
    }

    @Override
    public void destroy() {
        // Close resources
    }
}
