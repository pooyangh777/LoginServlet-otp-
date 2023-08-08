package servlets;

import RestApi.RestRequest;
import dto.DeviceType;
import dto.MyAppProperties;
import dto.otp.AccessToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Oauth2Service {
    private String serverUrl;
    private String clientId;
    private String clientSecret;
    private String apiToken;
    private String serverRedirectUrl;
    private String scope;
    private String otpSignature;
    MyAppProperties myAppProperties;

    public Oauth2Service(MyAppProperties myAppProperties) {
        this.myAppProperties = myAppProperties;
        init();
    }

    public void init() {
        serverUrl = myAppProperties.getServerUrl();
        clientId = myAppProperties.getClientId();
        clientSecret = myAppProperties.getClientSecret();
        apiToken = myAppProperties.getApiToken();
        serverRedirectUrl = myAppProperties.getServerRedirectUrl();
        scope = myAppProperties.getScope();
        otpSignature = myAppProperties.getOtpSignature();
    }


    public dto.otp.Handshake otpHandshake(String deviceUID, String deviceName, DeviceType deviceType, String deviceOS, String deviceOsVersion, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder urlBuilder;
        try {
            urlBuilder = new StringBuilder(serverUrl).append("/oauth2/clients/handshake/").append(clientId)
                    .append("?").append("device_uid=").append(deviceUID)
                    .append("&").append("algorithm=").append("rsa-sha256")
                    .append("&").append("device_os=").append(URLEncoder.encode(deviceOS, StandardCharsets.UTF_8.toString()))
                    .append("&").append("device_os_version=").append(URLEncoder.encode(deviceOsVersion, StandardCharsets.UTF_8.toString()))
                    .append("&").append("device_type=").append(URLEncoder.encode(deviceType.getValue(), StandardCharsets.UTF_8.toString()))
                    .append("&").append("device_name=").append(URLEncoder.encode(deviceName, StandardCharsets.UTF_8.toString()));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

//        if (deviceLat != 0.0 && deviceLon != 0.0) {
//            urlBuilder.append("&").append("device_lat=").append(deviceLat)
//                    .append("&").append("device_lon=").append(deviceLon);
//        }


        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "bearer " + apiToken);
        header.put("Content-Type", "application/x-www-form-urlencoded");
        header.put("Accept-Language", "fa");

        dto.otp.Handshake handshake = RestRequest.post(urlBuilder.toString(), (Map) null, header, dto.otp.Handshake.class);
        return handshake;
    }


    public dto.otp.Authorize otpAuthorize(String keyId, String identity, HttpServletRequest request) throws Exception {
        StringBuilder urlBuilder = null;
        try {
            urlBuilder = new StringBuilder(serverUrl)
                    .append("/oauth2/otp/authorize/").append(URLEncoder.encode(identity, StandardCharsets.UTF_8.toString()))
                    .append("?").append("response_type=").append("code");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        Map<String, String> header = getSignHeaderMap(keyId);

        dto.otp.Authorize authorize = RestRequest.post(urlBuilder.toString(), (Map) null, header, dto.otp.Authorize.class);
        return authorize;
    }

    public AccessToken otpVerify(String keyId, String identity, String otp) throws Exception {
        try {
            String builder = serverUrl +
                    "/oauth2/otp/verify/" + URLEncoder.encode(identity, StandardCharsets.UTF_8.toString()) +
                    "?" + "otp=" + otp;

            Map<String, String> header = getSignHeaderMap(keyId);
            dto.otp.Verify verify = RestRequest.post(builder, (Map) null, header, dto.otp.Verify.class);

            return getOtpAccessToken(verify.getCode(), keyId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public AccessToken getOtpAccessToken(String code, String keyId) throws IOException {

        String builder = serverUrl +
                "/oauth2/token/" +
                "?" + "grant_type=" + "authorization_code" +
                "&" + "code=" + code +
                "&" + "client_id=" + clientId +
                "&" + "client_secret=" + clientSecret;

        Map<String, String> header = getSignHeaderMap(keyId);

        AccessToken accessToken = RestRequest.post(builder, (Map) null, header, AccessToken.class);

        return accessToken;
    }

    public AccessToken refresh(String refreshToken) {

        String builder = "https://podtest.fanapsoft.ir" +
                "/oauth2/token/" +
                "?" + "grant_type=" + "refresh_token" +
                "&" + "refresh_token=" + refreshToken +
                "&" + "redirect_uri=" + serverRedirectUrl +
                "&" + "client_id=" + clientId +
                "&" + "client_secret=" + clientSecret;

        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/x-www-form-urlencoded");
        header.put("Accept-Language", "fa");
        AccessToken accessToken = RestRequest.post(builder, new HashMap<>(), header, AccessToken.class);
        return accessToken;
    }

    private Map<String, String> getSignHeaderMap(String keyId) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/x-www-form-urlencoded");

        header.put("Accept-Language", "fa");
        header.put("Authorization", "Signature keyId=\"" + keyId + "\", signature=\"" + otpSignature + "\", headers=\"host\"");
        header.put("host", serverUrl);
        return header;
    }
}
