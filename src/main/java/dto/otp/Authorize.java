package dto.otp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Authorize {
    private Long expires_in;
    private String identity;
    private String type;
    private String user_id;
    private Long codeLength;
    private Boolean sent_before;
}
