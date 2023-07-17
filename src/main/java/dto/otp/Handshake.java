package dto.otp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Handshake {

    @JsonProperty("keyFormat")
    private String keyFormat;

    @JsonProperty("client")
    private Client client;

    @JsonProperty("keyId")
    private String keyId;

    @JsonProperty("publicKey")
    private String publicKey;

    @JsonProperty("device")
    private Device device;

    @JsonProperty("expires_in")
    private Integer expiresIn;

    @JsonProperty("algorithm")
    private String algorithm;
}
