package com.cs180;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import javax.lang.model.type.NullType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cs180.api.Request;
import com.cs180.api.Response;
import com.cs180.api.Request.EMethod;
import com.cs180.resolvers.ResolverTools;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Worker implements Runnable {
    private static final Logger logger = LogManager.getLogger(Worker.class);

    private Selector selector;

    private static final GsonBuilder builder = new GsonBuilder();
    private static final Gson gson = builder.create();

    public Worker(Selector selector) {
        this.selector = selector;
    }

    private void handleRead(SelectionKey key) {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        try {
            int bytesRead = clientChannel.read(buffer);
            if (bytesRead == -1) {
                logger.info("Client disconnected: " + clientChannel);
                clientChannel.close();
            } else {
                buffer.flip();
                String json = new String(buffer.array()).trim();
                try {
                    @SuppressWarnings("unchecked")
                    Request<NullType> request = gson.fromJson(json, Request.class);

                    logger.info(String.format("%s -> %s -> %s", clientChannel.getRemoteAddress(), request.getMethod(),
                            request.getEndpoint()));

                    String response = ResolverTools.resolve(request, json);
                    buffer.clear().put(response.getBytes());
                    buffer.flip();

                    clientChannel.write(buffer);
                } catch (Exception e) {
                    logger.error("Unknown Exception", e);

                    Response<String> response = new Response<>(EMethod.UNKNOWN, "unknown", "Unknown Exception",
                            Response.EStatus.SERVER_ERROR);
                    String responseJSON = gson.toJson(response);
                    buffer.clear().put(responseJSON.getBytes());
                    buffer.flip();

                    clientChannel.write(buffer);
                }
            }
        } catch (IOException e) {
            logger.error(e);
            try {
                clientChannel.close();
            } catch (IOException ex) {
                logger.error("Error Closing Client Channel", ex);
            }
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                this.selector.select();
                Iterator<SelectionKey> keyIterator = this.selector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();

                    // Technically unnecessary since selector only registered
                    // to accept READ events, but it's good practice to check
                    if (!key.isReadable())
                        continue;

                    handleRead(key);
                }
            }
        } catch (IOException e) {
            logger.error(e);
        }
    }
}
