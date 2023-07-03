package co.com.bancolombia.movement.gateway;

import co.com.bancolombia.movement.CryptoMovementEntity;

public interface CryptoMovementRepository {

    void save(CryptoMovementEntity entity);

    int getQuantityMovementByManagementId(String idManagement);

    CryptoMovementEntity getMovementByManagementId(String idManagement);

    void delete(CryptoMovementEntity entity);
}
