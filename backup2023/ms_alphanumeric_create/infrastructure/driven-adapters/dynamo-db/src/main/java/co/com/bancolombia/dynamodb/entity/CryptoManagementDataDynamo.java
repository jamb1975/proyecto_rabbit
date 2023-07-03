package co.com.bancolombia.dynamodb.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@DynamoDBTable(tableName = "clavealfanumerica-dev-cryptomanagement")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class CryptoManagementDataDynamo {

    @DynamoDBHashKey(attributeName = "cryptoManagementId")
    private String cryptoManagementId;

    @DynamoDBAttribute(attributeName = "aid")
    private String aid;

    @DynamoDBAttribute(attributeName = "consumer")
    private String consumer;

    @DynamoDBAttribute(attributeName = "createdAt")
    private String createdAt;

    @DynamoDBAttribute(attributeName = "lastStatusDate")
    private String lastStatusDate;

    @DynamoDBAttribute(attributeName = "failAttempts")
    private int failAttempts;

    @DynamoDBAttribute(attributeName = "status")
    private String status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CryptoManagementDataDynamo that = (CryptoManagementDataDynamo) o;
        return failAttempts == that.failAttempts && Objects.equals(cryptoManagementId, that.cryptoManagementId) && Objects.equals(aid, that.aid) && Objects.equals(consumer, that.consumer) && Objects.equals(createdAt, that.createdAt) && Objects.equals(lastStatusDate, that.lastStatusDate) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cryptoManagementId, aid, consumer, createdAt, lastStatusDate, failAttempts, status);
    }
}
