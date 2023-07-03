package co.com.bancolombia.management.gateway;

import co.com.bancolombia.management.CryptoManagementEntity;

public interface CryptoManagementRepository {

    CryptoManagementEntity save(CryptoManagementEntity entity);

    CryptoManagementEntity findByAid(String aid, String consumer);
}
