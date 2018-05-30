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
@WebServlet("/adugarecurs")
public class AdaugareCursServlet extends HttpServlet{
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id_profesor = Long.valueOf(request.getParameter("cont_id"));
        String denumire = request.getParameter("denumire");
        String descriere = request.getParameter("descriere");

        JSONObject jsonObj = new JSONObject();

        if (id_profesor == null) {
            jsonObj.put("success", false);
            jsonObj.put("message", "Id-ul contului nu a fost specificat");
            response.setStatus(400);
        } else if (denumire == null) {
            jsonObj.put("success", false);
            jsonObj.put("message", "Denumirea curs nu a fost specificat");
            response.setStatus(400);
        } else {
            Connection conn = ConnectionHelper.getInstance().getConnection();
            String query;
            query = "INSERT INTO public.\"cursuri\" (id,denumire,id_profesor,descriere) values(nextval('cursuri_seq'),?,?,?);";

            try (PreparedStatement ps = conn.prepareStatement(query)){
                ps.setString(1, denumire);
                ps.setLong(2, id_profesor);
                ps.setString(3, descriere);
                ps.executeUpdate();

                jsonObj.put("success", true);
                jsonObj.put("message", "Curs creat cu succes!");

            } catch (SQLException e) {
                e.printStackTrace();
                jsonObj.put("success", false);
                jsonObj.put("message", "Cursul nu a putut fi creat");
            }
        }
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        out.write(jsonObj.toString());

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
