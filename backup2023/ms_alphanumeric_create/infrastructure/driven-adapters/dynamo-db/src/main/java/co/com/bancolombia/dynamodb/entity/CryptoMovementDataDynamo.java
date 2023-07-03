package co.com.bancolombia.dynamodb.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@DynamoDBTable(tableName = "clavealfanumerica-dev-cryptomovement")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class CryptoMovementDataDynamo {

    @DynamoDBHashKey
    @DynamoDBAttribute(attributeName = "cryptoMovementId")
    private String cryptoMovementId;

    @DynamoDBAttribute(attributeName = "aid")
    private String aid;

    @DynamoDBAttribute(attributeName = "movement")
    private String movement;

    @DynamoDBAttribute(attributeName = "createdAt")
    private String createdAt;

    @DynamoDBAttribute(attributeName = "cryptoManagementId")
    private String cryptoManagementId;

}
