package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.Managers;
import taskManager.TaskManager;

import java.io.IOException;

public class HttpTaskServer implements HttpHandler {


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        TaskManager taskManager = Managers.getDefault();

    }

}
