package dto.otp;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Verify {

    private String state;
    private String code;
    private String access_token;
    private String token_type;
    private Long expires_in;
    private String scope;
    private String refresh_token;
    private String id_token;
    private String device_uid;
}