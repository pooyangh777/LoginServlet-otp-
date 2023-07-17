package servlets;


import com.google.gson.Gson;
import dataBase.RefreshDTO;
import dto.otp.AccessToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.*;

public class Verify extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String keyId = req.getHeader("keyId");
        String identity = req.getParameter("identity");
        String otp = req.getParameter("otp");
        AccessToken result;
        RefreshDTO refreshDTO = new RefreshDTO();
        try {
            result = LoginApplication.oauth2Service.otpVerify(keyId, identity, otp);
            refreshDTO.setRefreshToken(result.getRefreshToken());
            refreshDTO.setKeyId(keyId);
            LoginApplication.provider.create(refreshDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        PrintWriter writer;
        resp.setContentType(MediaType.APPLICATION_JSON);
        writer = resp.getWriter();
        writer.write(new Gson().toJson(result.getAccessToken()));
        writer.close();
    }
}
