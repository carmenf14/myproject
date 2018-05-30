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
@WebServlet("/cursuristudent")
public class ListaCursuriStudenti extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        Connection conn = ConnectionHelper.getInstance().getConnection();
        String str="SELECT * FROM public.\"cursuri\" JOIN cursurixstudenti ON cursuri.id = cursurixstudenti.id_curs WHERE id_student=?";
        try {
            PreparedStatement preparedStatement=conn.prepareStatement(str);
            preparedStatement.setLong(1,Long.valueOf(request.getParameter("id_student")));
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.isBeforeFirst()){
                while (resultSet.next()){
                    JSONObject aux=new JSONObject();
                    aux.put("id_curs", resultSet.getInt("id"));
                    aux.put("denumire", resultSet.getString("denumire"));
                    aux.put("descriere", resultSet.getString("descriere"));
                    jsonArray.put(aux);
                }
                jsonObject.put("lista_cursuri_student",jsonArray);
            }
            jsonObject.put("success", true);
            jsonObject.put("message", "Cursuri incarcate cu success!");
        } catch (SQLException e) {
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("messaje", "Cursuri nu au putut fi incarcate!");
        }
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(jsonObject.toString());
    }
}
