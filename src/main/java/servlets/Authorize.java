package servlets;

import application.Main;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.*;

public class Authorize extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String keyId = req.getHeader("keyId");
        String identity = req.getParameter("identity");
//        String payload = req.getParameter("payload");
        dto.otp.Authorize result = null;
        try {
            result = LoginApplication.oauth2Service.otpAuthorize(keyId, identity, req);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        PrintWriter writer;
        resp.setContentType(MediaType.APPLICATION_JSON);
        writer = resp.getWriter();
        writer.write(new Gson().toJson(result));
        writer.close();
    }
}
