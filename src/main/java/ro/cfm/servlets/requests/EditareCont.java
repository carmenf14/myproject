package ro.cfm.servlets.requests;

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
import java.sql.SQLException;

/**
 * Created by Carmen on 5/26/2018.
 */
@WebServlet("/editareCont")
public class EditareCont extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id_cont = Long.valueOf(request.getParameter("id_cont"));
        String status = request.getParameter("status");
        String nume = request.getParameter("nume");
        String prenume = request.getParameter("prenume");

        JSONObject jsonObj = new JSONObject();

        if (id_cont == null) {
            jsonObj.put("success", false);
            jsonObj.put("message", "Id-ul contului nu a fost specificat");
            response.setStatus(400);
        }else if (nume == null || nume.compareTo("")==0 || prenume == null || prenume.compareTo("")==0) {
            jsonObj.put("success", false);
            jsonObj.put("message", "Numele si prenumele trebuie completate!");
            response.setStatus(400);
        } else {
            if(status.compareTo("profesor")==0) {
                Connection conn = ConnectionHelper.getInstance().getConnection();
                String query;
                query = "UPDATE public.\"profesori\" SET (nume, prenume)=(?,?) where id = ?;";

                try (PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setString(1, nume);
                    ps.setString(2, prenume);
                    ps.setLong(3, id_cont);
                    ps.executeUpdate();
                    jsonObj.put("success", true);
                    jsonObj.put("message", "Cont modificat cu succes!");

                } catch (SQLException e) {
                    e.printStackTrace();
                    jsonObj.put("success", false);
                    jsonObj.put("message", "Contul nu a putut fi modificat");
                }
            }
            if(status.compareTo("student")==0){
                Connection conn = ConnectionHelper.getInstance().getConnection();
                String query;
                query = "UPDATE public.\"studenti\" SET (nume, prenume)=(?,?) where id = ?;";

                try (PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setString(1, nume);
                    ps.setString(2, prenume);
                    ps.setLong(3, id_cont);
                    ps.executeUpdate();
                    jsonObj.put("success", true);
                    jsonObj.put("message", "Cont student modificat cu succes!");

                } catch (SQLException e) {
                    e.printStackTrace();
                    jsonObj.put("success", false);
                    jsonObj.put("message", "Contul nu a putut fi modificat");
                }
            }
        }
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        out.write(jsonObj.toString());

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

