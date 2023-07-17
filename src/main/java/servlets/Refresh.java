package servlets;

import com.google.gson.Gson;
import dto.otp.AccessToken;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.*;

public class Refresh extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String refreshToken = req.getParameter("refreshToken");
        AccessToken result = LoginApplication.oauth2Service.refresh(refreshToken);
        PrintWriter writer;
        resp.setContentType(MediaType.APPLICATION_JSON);
        try {
            writer = resp.getWriter();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        writer.write(new Gson().toJson(result));
        writer.close();
    }
}
