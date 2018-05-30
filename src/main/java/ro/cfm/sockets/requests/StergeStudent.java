package ro.cfm.sockets.requests;

import org.json.JSONObject;
import ro.cfm.connection.ConnectionHelper;
import ro.cfm.model.RequestType;
import ro.cfm.model.SocketMapper;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;

/**
 * Created by Carmen on 5/26/2018.
 */
public class StergeStudent {
    @SocketMapper(name = "stergestudent")
    public static void stergeStudent(JSONObject messageJson, HashMap<String, OutputStream> clientsOutputStream) {
        Connection conn = ConnectionHelper.getInstance().getConnection();
        Long id_user = Long.valueOf(messageJson.getInt("id_cont"));
        String usernameAdmin = messageJson.getString("id_profesor");
        Long id_curs = Long.valueOf(messageJson.getInt("id_curs"));
        Long id_student = Long.valueOf(messageJson.getString("id_student"));
        String motiv=messageJson.getString("motiv");
        JSONObject messageInsert = new JSONObject();
        String str = "DELETE FROM public.\"cursurixstundenti\" WHERE id_student=? and id_curs=?";

        try (
                PreparedStatement preparedStatement1 = conn.prepareStatement(str);
        ) {
            preparedStatement1.setLong(1, id_student);
            preparedStatement1.setLong(2, id_curs);
            preparedStatement1.executeUpdate();


            if (clientsOutputStream.containsKey(id_student)) {
                new ObjectOutputStream(clientsOutputStream.get(id_student)).writeObject(messageJson.toString());
            }


            messageInsert.put("type", RequestType.STERGE_STUDENT);
            messageInsert.put("success", true);
            messageInsert.put("message", "Stergere realizata cu success");
            messageInsert.put("motiv", motiv);

            new ObjectOutputStream(clientsOutputStream.get(usernameAdmin)).writeObject(messageInsert.toString());

        } catch (Exception e) {
            e.printStackTrace();
            messageInsert.put("success", false);
            messageInsert.put("message", "Nu s-a putut efectua stergerea!");

            try {
                new ObjectOutputStream(clientsOutputStream.get(usernameAdmin)).writeObject(messageInsert.toString());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

}
