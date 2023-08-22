package servlets;

import com.google.gson.Gson;
import dto.DeviceType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.MediaType;
import java.io.*;

@Slf4j
public class Handshake extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        String deviceUID = req.getParameter("deviceUID");
        String deviceName = req.getParameter("deviceName");
        String deviceOs = req.getParameter("deviceOs");
        String deviceOsVersion = req.getParameter("deviceOsVersion");
        DeviceType deviceType = DeviceType.valueOf(req.getParameter("deviceType"));
//        Double deviceLat = Double.valueOf(req.getParameter("deviceLat"));
//        Double deviceLon = Double.valueOf(req.getParameter("deviceLon"));

        if (deviceUID == null || deviceName == null || deviceOs == null || deviceOsVersion == null || deviceType == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("The 'requiredParam1' and 'requiredParam2' parameters are required.");
        } else {
            dto.otp.Handshake result = null;
            log.info("receive request from : " + "(" + deviceUID + ")" + " for handshake");
            result = Oauth2Service.otpHandshake(deviceUID, deviceName, deviceType, deviceOs, deviceOsVersion, req, resp);
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            log.info("Response time of handshake from talkback for : " + "(" + deviceUID + ")" + " is " + elapsedTime + " ms");
            PrintWriter writer;
            resp.setContentType(MediaType.APPLICATION_JSON);
            writer = resp.getWriter();
            writer.write(new Gson().toJson(result));
            writer.close();
        }
    }
}
