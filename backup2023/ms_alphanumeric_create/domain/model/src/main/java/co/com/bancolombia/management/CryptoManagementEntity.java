package co.com.bancolombia.management;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CryptoManagementEntity {

    private String cryptoManagementId;
    private String aid;
    private String consumer;
    private Timestamp createdAt;
    private Timestamp lastStatusDate;
    private Integer failAttempts;
    private String status;

}
