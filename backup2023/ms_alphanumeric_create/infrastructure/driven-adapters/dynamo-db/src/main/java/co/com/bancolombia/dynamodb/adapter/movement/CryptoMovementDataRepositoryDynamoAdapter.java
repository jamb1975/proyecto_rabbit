package co.com.bancolombia.dynamodb.adapter.movement;

import co.com.bancolombia.dynamodb.config.MapperEntity;
import co.com.bancolombia.dynamodb.entity.CryptoMovementDataDynamo;
import co.com.bancolombia.movement.CryptoMovementEntity;
import co.com.bancolombia.movement.gateway.CryptoMovementRepository;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;

@Primary
@Service
public class CryptoMovementDataRepositoryDynamoAdapter implements CryptoMovementRepository {

    private final DynamoDBMapper dynamoDBMapper;
    private final DynamoDBMapperConfig dynamoDBMapperConfig;

    @Autowired
    public CryptoMovementDataRepositoryDynamoAdapter(DynamoDBMapper dynamoDBMapper,
                                                     @Value("${aws.dynamodb.tableName.Movement}") String dynamoDBTableName) {
        this.dynamoDBMapper = dynamoDBMapper;
        this.dynamoDBMapperConfig = DynamoDBMapperConfig
                .builder()
                .withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(dynamoDBTableName))
                .build();

    }

    @Override
    public void save(CryptoMovementEntity entity) {
        CryptoMovementDataDynamo cryptoMovementDataDynamo = MapperEntity.toCryptoMovementData(
                entity);
        dynamoDBMapper.save(cryptoMovementDataDynamo, dynamoDBMapperConfig);
        entity.setCryptoMovementId(cryptoMovementDataDynamo.getCryptoMovementId());
    }

    @Override
    public int getQuantityMovementByManagementId(String idManagement) {
        List<CryptoMovementDataDynamo> resultMovement;
        Map<String, AttributeValue> eav = new HashMap();
        eav.put(":cryptoManagementId", new AttributeValue(idManagement));
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("cryptoManagementId = :cryptoManagementId")
                .withExpressionAttributeValues(eav);
        resultMovement = dynamoDBMapper
                .scan(CryptoMovementDataDynamo.class, scanExpression, dynamoDBMapperConfig);
        return resultMovement.size();
    }

    @Override
    public CryptoMovementEntity getMovementByManagementId(String idManagement) {

        List<CryptoMovementEntity> listMovementEntity = new ArrayList<>();
        Map<String, AttributeValue> eav = new HashMap();
        eav.put(":cryptoManagementId", new AttributeValue(idManagement));
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("cryptoManagementId = :cryptoManagementId")
                .withExpressionAttributeValues(eav);
        var resultMovement = dynamoDBMapper
                .scan(CryptoMovementDataDynamo.class, scanExpression);

        for (CryptoMovementDataDynamo cm : resultMovement) {
            CryptoMovementEntity c = MapperEntity.toCryptoMovementEntity(cm);
            listMovementEntity.add(c);
        }

        Collections.sort(listMovementEntity,
                Comparator.comparing(CryptoMovementEntity::getCreatedAt));

        return listMovementEntity.get(listMovementEntity.size() - 1);
    }


    @Override
    public void delete(CryptoMovementEntity entity) {
        CryptoMovementDataDynamo movementDataDynamo = dynamoDBMapper.load(
                CryptoMovementDataDynamo.class, entity.getCryptoMovementId(),
                dynamoDBMapperConfig);
        dynamoDBMapper.delete(movementDataDynamo);
    }
}

