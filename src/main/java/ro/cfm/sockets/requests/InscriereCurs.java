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
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Carmen on 5/26/2018.
 */
public class InscriereCurs {
    @SocketMapper(name = "inscriere_curs")
    public static void inscriereChatroom(JSONObject messageJson, OutputStream clientOutputStream) {
        Connection conn = ConnectionHelper.getInstance().getConnection();
        Long id_curs = Long.valueOf(messageJson.getInt("id_curs"));
        Long id_student = Long.valueOf(messageJson.getInt("id_student"));
        JSONObject messageInsert = new JSONObject();
        String str = "INSERT INTO public.\"cursurixstudenti\"(id_student, id_curs) VALUES (?, ?)";
        String verifica = "SELECT * from public.\"cursurixstudenti\" where id_student=? and id_curs=?";
        ResultSet resultSet = null;
        Boolean ok = false;
        try (
                PreparedStatement preparedStatement = conn.prepareStatement(verifica);
        ) {
            preparedStatement.setLong(1, id_student);
            preparedStatement.setLong(2, id_curs);
            resultSet = preparedStatement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                ok = true;
                resultSet = null;
            }
            else{
                resultSet.close();
                messageInsert.put("success", false);
                messageInsert.put("message", "Studentul este deja inscris la acest curs!");
                try {
                    new ObjectOutputStream(clientOutputStream).writeObject(messageInsert.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    messageInsert.put("success", false);
                    messageInsert.put("message", "Inscrierea nu s-a putut realiza");
                    try {
                        new ObjectOutputStream(clientOutputStream).writeObject(messageInsert.toString());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (ok == true) {
            try (
                    PreparedStatement preparedStatement = conn.prepareStatement(str);
            ) {

                messageInsert.put("type", RequestType.INSCRIERE_CURS.toString());
                    preparedStatement.setLong(1, id_student);
                    preparedStatement.setLong(2, id_curs);
                    preparedStatement.executeUpdate();
                    messageInsert.put("success", true);
                    messageInsert.put("message", "Inscriere cu success");
                    messageInsert.put("id_curs", id_curs);
            } catch (SQLException e) {
                e.printStackTrace();
                messageInsert.put("success", false);
                messageInsert.put("message", "Inscrierea nu s-a putut realiza");
                try {
                    new ObjectOutputStream(clientOutputStream).writeObject(messageInsert.toString());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } finally {
                try {
                    if (resultSet != null)
                        resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

