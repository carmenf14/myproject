package ro.cfm.sockets.connection;

import org.json.JSONObject;
import ro.cfm.model.RequestType;
import ro.cfm.sockets.requests.InscriereCurs;
import ro.cfm.sockets.requests.StergeStudent;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by Carmen on 5/26/2018.
 */
@WebListener
public class SocketHandler implements ServletContextListener {

    Logger logger = Logger.getLogger(SocketHandler.class.getName());

    private ServerSocket serverSocket;
    private HashMap<String,OutputStream> clientsOutputStream = new HashMap<String, OutputStream>();
    private HashMap<String,InputStream> clientsInputStream = new HashMap<String, InputStream>();

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            serverSocket = new ServerSocket(8088);
            logger.warning("Sockets initialized");

            listenForConnections();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }

    private void listenForConnections() {
        new Thread(new Runnable() {
            public void run() {
                while(true) {
                    try {
                        Socket client = serverSocket.accept();
                        InputStream clientInputStream = client.getInputStream();
                        OutputStream clientOutputStream = client.getOutputStream();

                        listenForInput(clientInputStream,clientOutputStream);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void listenForInput(final InputStream clientInputStream, final OutputStream clientOutputStream) {
        new Thread(new Runnable() {
            public void run() {
                while(true) {
                    try {

                        String userJoined = (String) new ObjectInputStream(clientInputStream).readObject();
                        JSONObject messageJson = new JSONObject(userJoined);
                        RequestType type = RequestType.valueOf(messageJson.getString("type"));

                        switch (type) {
                            case INSCRIERE_CURS:
                                InscriereCurs.inscriereChatroom(messageJson, clientOutputStream);
                                break;
                            case STERGE_STUDENT:
                                StergeStudent.stergeStudent(messageJson,clientsOutputStream);
                                break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        removeUserConnectionFromServer(clientInputStream,clientOutputStream);
                        break; //EOF Exception cand user-ul se deconecteaza
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }).start();


    }

    public void removeUserConnectionFromServer(InputStream clientInputStream, OutputStream clientOuputStream) {

        String email = null;

        for(Map.Entry<String,InputStream> entry : clientsInputStream.entrySet()) {
            if(entry.getValue().equals(clientInputStream)) {
                email = entry.getKey();
                clientsInputStream.remove(entry.getKey());
                break;
            }
        }

        for(Map.Entry<String,OutputStream> entry : clientsOutputStream.entrySet()) {
            if(entry.getValue().equals(clientOuputStream)) {
                clientsOutputStream.remove(entry.getKey());
                break;
            }
        }
    }


}
