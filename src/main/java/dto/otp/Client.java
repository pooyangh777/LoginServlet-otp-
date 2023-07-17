package dto.otp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Client {
    @JsonProperty("accessTokenExpiryTime")
    private Long accessTokenExpiryTime;

    @JsonProperty("allowedRedirectUris")
    private List<String> allowedRedirectUris;

    @JsonProperty("icon")
    private String icon;

    @JsonProperty("userId")
    private Integer userId;

    @JsonProperty("allowedScopes")
    private List<String> allowedScopes;

    @JsonProperty("allowedUserIPs")
    private List<String> allowedUserIPs;

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("url")
    private String url;

    @JsonProperty("allowedGrantTypes")
    private List<String> allowedGrantTypes;

    @JsonProperty("signupEnabled")
    private Boolean signupEnabled;

    @JsonProperty("captchaEnabled")
    private Boolean captchaEnabled;

    @JsonProperty("loginUrl")
    private String loginUrl;

    @JsonProperty("name")
    private String name;

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("twoFAEnabled")
    private Boolean twoFAEnabled;

    @JsonProperty("roles")
    private List<String> roles;

    @JsonProperty("refreshTokenExpiryTime")
    private Long refreshTokenExpiryTime;

    @JsonProperty("pkceEnabled")
    private Boolean pkceEnabled;

    @JsonProperty("loginAsDepositEnabled")
    @JsonIgnore
    private Boolean loginAsDepositEnabled;

    @JsonProperty("cssEnabled")
    private Boolean cssEnabled;

    @JsonProperty("active")
    private Boolean active;
}
