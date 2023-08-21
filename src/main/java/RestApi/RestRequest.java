package RestApi;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
@Slf4j
public class RestRequest {
    public static <T> T post(String url, Map<String, ?> body, Map<String, String> header, Class<T> aClass) {
        try {


            URL urlObj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
            conn.setRequestMethod("POST");

            if (header != null && !header.isEmpty()) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            os.write(new ObjectMapper().writeValueAsString(body).getBytes());
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();

            if (responseCode != HttpURLConnection.HTTP_OK) {
                log.info("response is not 200 " + responseCode);
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                responseBuilder.append(line);
            }
            in.close();

            return new ObjectMapper().readValue(responseBuilder.toString(), aClass);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
