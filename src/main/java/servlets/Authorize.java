package servlets;


import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.MediaType;
import java.io.*;

@Slf4j
public class Authorize extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        String keyId = req.getHeader("keyId");
        String identity = req.getParameter("identity");
//        String payload = req.getParameter("payload");
        dto.otp.Authorize result = null;
        try {
            log.info("receive request from : " + "(" + keyId + ")" + " for authorize");
            result = Oauth2Service.otpAuthorize(keyId, identity, req);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        log.info("Response time of authorize from talkback for : " + "(" + keyId + ")" + " is " + elapsedTime + " ms");
        PrintWriter writer;
        resp.setContentType(MediaType.APPLICATION_JSON);
        writer = resp.getWriter();
        writer.write(new Gson().toJson(result));
        writer.close();
    }
}
