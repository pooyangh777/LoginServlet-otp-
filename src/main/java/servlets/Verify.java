package servlets;


import com.google.gson.Gson;
import dto.otp.AccessToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.ws.rs.core.MediaType;
import java.io.*;

public class Verify extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String keyId = req.getHeader("keyId");
        String identity = req.getParameter("identity");
        String otp = req.getParameter("otp");
        AccessToken result;
        try {
            result = EmbeddedHttpServer.oauth2Service.otpVerify(keyId, identity, otp);
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
