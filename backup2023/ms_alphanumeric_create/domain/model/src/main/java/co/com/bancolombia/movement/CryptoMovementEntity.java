package co.com.bancolombia.movement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CryptoMovementEntity {

    private String cryptoMovementId;
    private String cryptoManagementId;
    private String aid;
    private String movement;
    private Timestamp createdAt;
}
