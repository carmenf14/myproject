package ro.cfm.servlets.requests;

import org.json.JSONArray;
import org.json.JSONObject;
import ro.cfm.connection.ConnectionHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Carmen on 5/26/2018.
 */
@WebServlet("/studenticurs")
public class ListaStudentiCursServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        Connection conn = ConnectionHelper.getInstance().getConnection();
        String str="SELECT id_student, nume, prenume FROM public.\"cursurixstudenti\" JOIN public.\"studenti\" ON cursurixstudenti.id_student = studenti.id WHERE cursurixstudenti.id_curs=?";
        try {
            PreparedStatement preparedStatement=conn.prepareStatement(str);
            preparedStatement.setLong(1,Long.valueOf(request.getParameter("id_curs")));
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.isBeforeFirst()){
                while (resultSet.next()){
                    JSONObject aux=new JSONObject();
                    aux.put("id_student", resultSet.getInt("id_student"));
                    aux.put("nume", resultSet.getString("nume"));
                    aux.put("prenume", resultSet.getString("prenume"));
                    jsonArray.put(aux);
                }
                jsonObject.put("lista_studenti_curs",jsonArray);
            }
            jsonObject.put("success", true);
            jsonObject.put("message", "Lista cu studenti incarcata cu success!");
        } catch (SQLException e) {
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("messaje", "Lista cu studenti nu a putut fi incarcata!");
        }
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(jsonObject.toString());
    }
}
