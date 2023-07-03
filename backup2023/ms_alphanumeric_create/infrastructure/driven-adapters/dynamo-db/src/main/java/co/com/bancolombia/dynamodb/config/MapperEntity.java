package co.com.bancolombia.dynamodb.config;

import co.com.bancolombia.dynamodb.entity.CryptoManagementDataDynamo;
import co.com.bancolombia.dynamodb.entity.CryptoMovementDataDynamo;
import co.com.bancolombia.management.CryptoManagementEntity;
import co.com.bancolombia.movement.CryptoMovementEntity;

import java.sql.Timestamp;
import java.util.UUID;

public class MapperEntity {

    private MapperEntity() {
    }

    public static CryptoManagementDataDynamo toCryptoManagementData(
            CryptoManagementEntity cryptoManagementEntity) {
        String id = cryptoManagementEntity.getAid() + cryptoManagementEntity.getConsumer();
        CryptoManagementDataDynamo cryptoManagementDynamo = new CryptoManagementDataDynamo();
        cryptoManagementDynamo.setCryptoManagementId(id);
        cryptoManagementDynamo.setAid(cryptoManagementEntity.getAid());
        cryptoManagementDynamo.setConsumer(cryptoManagementEntity.getConsumer());
        cryptoManagementDynamo.setCreatedAt(cryptoManagementEntity.getCreatedAt().toString());
        cryptoManagementDynamo.setLastStatusDate(cryptoManagementEntity.getLastStatusDate().toString());
        cryptoManagementDynamo.setFailAttempts(cryptoManagementEntity.getFailAttempts());
        cryptoManagementDynamo.setStatus(cryptoManagementEntity.getStatus());
        return cryptoManagementDynamo;
    }

    public static CryptoManagementEntity toCryptoManagementEntity(
            CryptoManagementDataDynamo cryptoManagementDataDynamo) {
        CryptoManagementEntity cryptoManagementEntity = new CryptoManagementEntity();
        cryptoManagementEntity.setCryptoManagementId(cryptoManagementDataDynamo.getCryptoManagementId());
        cryptoManagementEntity.setAid(cryptoManagementDataDynamo.getAid());
        cryptoManagementEntity.setConsumer(cryptoManagementDataDynamo.getConsumer());
        cryptoManagementEntity.setCreatedAt(Timestamp.valueOf(cryptoManagementDataDynamo.getCreatedAt()));
        cryptoManagementEntity.setLastStatusDate(Timestamp.valueOf(cryptoManagementDataDynamo.getLastStatusDate()));
        cryptoManagementEntity.setFailAttempts(cryptoManagementDataDynamo.getFailAttempts());
        cryptoManagementEntity.setStatus(cryptoManagementDataDynamo.getStatus());
        return cryptoManagementEntity;
    }

    public static CryptoMovementDataDynamo toCryptoMovementData(
            CryptoMovementEntity cryptoMovementEntity) {
        var id = "MOV" + generateRandomUUIDWithoutDash();
        CryptoMovementDataDynamo cryptoMovementDataDynamo = new CryptoMovementDataDynamo();
        cryptoMovementDataDynamo.setCryptoMovementId(id);
        cryptoMovementDataDynamo.setAid(cryptoMovementEntity.getAid());
        cryptoMovementDataDynamo.setMovement(cryptoMovementEntity.getMovement());
        cryptoMovementDataDynamo.setCreatedAt(cryptoMovementEntity.getCreatedAt().toString());
        cryptoMovementDataDynamo.setCryptoManagementId(
                cryptoMovementEntity.getCryptoManagementId());
        return cryptoMovementDataDynamo;
    }

    public static CryptoMovementEntity toCryptoMovementEntity(
            CryptoMovementDataDynamo cryptoMovementDataDynamo) {
        CryptoMovementEntity cryptoMovementEntity = new CryptoMovementEntity();
        cryptoMovementEntity.setCryptoMovementId(cryptoMovementDataDynamo.getCryptoMovementId());
        cryptoMovementEntity.setAid(cryptoMovementDataDynamo.getAid());
        cryptoMovementEntity.setMovement(cryptoMovementDataDynamo.getMovement());
        cryptoMovementEntity.setCreatedAt(
                Timestamp.valueOf(cryptoMovementDataDynamo.getCreatedAt()));
        cryptoMovementEntity.setCryptoManagementId(
                cryptoMovementDataDynamo.getCryptoManagementId());
        return cryptoMovementEntity;
    }



    private static String generateRandomUUIDWithoutDash() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}

