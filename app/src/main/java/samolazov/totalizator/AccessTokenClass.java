package samolazov.totalizator;

public class AccessTokenClass {
    String access_token;
    String token_type;
    String expires_in;
    String refresh_token;
    String userName;
    String roles;
    String issued;

    @Override
    public String toString() {
        return "AccessTokenClass{" +
                "access_token='" + access_token + '\'' +
                '}';
    }
}
