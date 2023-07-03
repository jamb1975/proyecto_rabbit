package co.com.bancolombia.dynamodb.adapter.management;

import co.com.bancolombia.dynamodb.config.MapperEntity;
import co.com.bancolombia.dynamodb.entity.CryptoManagementDataDynamo;
import co.com.bancolombia.management.CryptoManagementEntity;
import co.com.bancolombia.management.gateway.CryptoManagementRepository;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
        import org.springframework.context.annotation.Primary;
        import org.springframework.stereotype.Service;

@Primary
@Service
public class CryptoManagementDataRepositoryDynamoAdapter implements CryptoManagementRepository {


    private final DynamoDBMapper dynamoDBMapper;
    private final DynamoDBMapperConfig dynamoDBMapperConfig;

    @Autowired
    public CryptoManagementDataRepositoryDynamoAdapter(DynamoDBMapper dynamoDBMapper,
                                                       @Value("${aws.dynamodb.tableName.Management}") String dynamoDBTableName) {
        this.dynamoDBMapper = dynamoDBMapper;
        this.dynamoDBMapperConfig = DynamoDBMapperConfig
                .builder()
                .withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(dynamoDBTableName))
                .build();

    }

    @Override
    public CryptoManagementEntity save(CryptoManagementEntity entity) {
        CryptoManagementDataDynamo cryptoManagementDynamo = MapperEntity.toCryptoManagementData(
                entity);
        dynamoDBMapper.save(cryptoManagementDynamo, dynamoDBMapperConfig);
        entity.setCryptoManagementId(cryptoManagementDynamo.getCryptoManagementId());
        return entity;
    }

    @Override
    public CryptoManagementEntity findByAid(String aid, String consumer) {
        var cryptoManagementId = aid + consumer;
        CryptoManagementDataDynamo managementData = dynamoDBMapper.load(
                CryptoManagementDataDynamo.class, cryptoManagementId,
                dynamoDBMapperConfig);
        return (managementData != null) ? MapperEntity.toCryptoManagementEntity(managementData)
                : null;
    }
}

