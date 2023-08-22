package servlets;

import com.google.gson.Gson;
import dto.otp.AccessToken;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.MediaType;
import java.io.*;

@Slf4j
public class Refresh extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        long startTime = System.currentTimeMillis();
        String refreshToken = req.getParameter("refreshToken");
        log.info("receive request from " + "(" + refreshToken + ")" + " for refreshToken");
        AccessToken result = Oauth2Service.refresh(refreshToken);
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        log.info("Response time of refresh from talkback for : " + "(" + refreshToken + ")" + " is " + elapsedTime + " ms");
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
