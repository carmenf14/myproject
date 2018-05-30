package ro.cfm.servlets.requests;

import org.json.JSONObject;
import ro.cfm.connection.ConnectionHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Created by Carmen on 5/26/2018.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    Logger logger = Logger.getLogger(LoginServlet.class.getName());
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject jsonObject = new JSONObject();
        String email = request.getParameter("email");
        String parola = request.getParameter("parola");
        Connection conn= ConnectionHelper.getInstance().getConnection();
        ResultSet resultSet = null;
        String str_student="SELECT * from public.\"studenti\" WHERE parola like ? and email like ?";
        String str_profesor="SELECT * from public.\"profesori\" WHERE parola like ? and email like ?";
        try {
            PreparedStatement preparedStatement=conn.prepareStatement(str_student);
            preparedStatement.setString(1,parola);
            preparedStatement.setString(2,email);
            resultSet=preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if(resultSet.isBeforeFirst()){
                resultSet.next();
                JSONObject cont = new JSONObject();
                cont.put("id_cont", resultSet.getInt("id"));
                cont.put("email", resultSet.getString("email"));
                cont.put("nume", resultSet.getString("nume"));
                cont.put("prenume", resultSet.getString("prenume"));
                cont.put("status","student");
                jsonObject.put("data", cont);
                jsonObject.put("message", "Logat cu succes!");
                jsonObject.put("success", true);
            }else
            {
                try{
                    PreparedStatement preparedStatement=conn.prepareStatement(str_profesor);
                    preparedStatement.setString(1,parola);
                    preparedStatement.setString(2,email);
                    resultSet=preparedStatement.executeQuery();
                }catch (Exception e){
                    if(resultSet.isBeforeFirst()){
                        resultSet.next();
                        JSONObject cont = new JSONObject();
                        cont.put("id_cont", resultSet.getInt("id"));
                        cont.put("email", resultSet.getString("email"));
                        cont.put("nume", resultSet.getString("nume"));
                        cont.put("prenume", resultSet.getString("prenume"));
                        cont.put("status","profesor");
                        jsonObject.put("data", cont);
                        jsonObject.put("message", "Logat cu succes!");
                        jsonObject.put("success", true);
                    }else{
                        jsonObject.put("message", "Email sau parola incorecte");
                        jsonObject.put("success", false);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(jsonObject.toString());

        String sample;

        HttpSession session = request.getSession();

        //Pt a pune un user in sesiune
        /*User user = new User(id,nume,...); // luate din baza de date

        session.setAttribute("currentuser",user);

        ----------------------------

                ;

        if(session.getAttribute("currentuser") != null) {
            User currenUser = (User) session.getAttribute("currentuser");
        }*/


        if(session.getAttribute("sample") != null) {
            sample = (String) session.getAttribute("sample");
            logger.warning(sample);
            jsonObject.put("sample",sample);
        }
        else {
            logger.warning("fara sesiune");
            jsonObject.put("sample","no session");
        }

        PrintWriter writer = response.getWriter();
        writer.write(jsonObject.toString());

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
