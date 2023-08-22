package servlets;


import com.google.gson.Gson;
import dto.otp.AccessToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.MediaType;
import java.io.*;

@Slf4j
public class Verify extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        String keyId = req.getHeader("keyId");
        String identity = req.getParameter("identity");
        String otp = req.getParameter("otp");
        AccessToken result;
        try {
            log.info("receive request from " + "(" + keyId + ")" + " for verify");
            result = Oauth2Service.otpVerify(keyId, identity, otp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        log.info("Response time of verify from talkback for : " + "(" + keyId + ")" + " is " + elapsedTime + " ms");
        PrintWriter writer;
        resp.setContentType(MediaType.APPLICATION_JSON);
        writer = resp.getWriter();
        writer.write(new Gson().toJson(result));
        writer.close();
    }
}
