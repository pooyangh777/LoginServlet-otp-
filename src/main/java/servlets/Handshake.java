package servlets;
import com.google.gson.Gson;
import dto.DeviceType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.ws.rs.core.MediaType;
import java.io.*;


public class Handshake extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
            result = EmbeddedHttpServer.oauth2Service.otpHandshake(deviceUID, deviceName, deviceType, deviceOs, deviceOsVersion, req, resp);
            PrintWriter writer;
            resp.setContentType(MediaType.APPLICATION_JSON);
            writer = resp.getWriter();
            writer.write(new Gson().toJson(result));
            writer.close();
        }
    }
}
