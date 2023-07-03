package co.com.bancolombia.userregister.model;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder(toBuilder = true)
public class UserRegister {
  private String  userName;
  private String password;
}
