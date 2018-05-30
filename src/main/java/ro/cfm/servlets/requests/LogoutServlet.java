package ro.cfm.servlets.requests;

import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Carmen on 5/26/2018.
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("user_id");

        JSONObject jsonObj = new JSONObject();

        if (userId == null) {
            jsonObj.put("success", false);
            jsonObj.put("message", "User id-ul nu a fost specificat");
            response.setStatus(400);
        }
        else {
                jsonObj.put("success",true);
                jsonObj.put("message","User has logged out");
        }

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        out.write(jsonObj.toString());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
