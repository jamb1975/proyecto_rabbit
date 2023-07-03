package co.com.bancolombia.cognito.signup.usecase.management;

import co.com.bancolombia.cognito.signin.model.Constants;
import co.com.bancolombia.cognito.signup.usecase.movement.CryptoMovementUseCase;
import co.com.bancolombia.common.util.ConstantsMovements;
import co.com.bancolombia.dto.ConsumerAidDTO;
import co.com.bancolombia.management.CryptoManagementEntity;
import co.com.bancolombia.management.gateway.CryptoManagementRepository;
import co.com.bancolombia.movement.CryptoMovementEntity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class CryptoManagementUseCase {

    private final CryptoManagementRepository managementRepository;
    private final CryptoMovementUseCase cryptoMovementUseCase;


    public CryptoManagementUseCase(
            CryptoManagementRepository managementRepository,
            CryptoMovementUseCase cryptoMovementUseCase){
        this.managementRepository = managementRepository;
        this.cryptoMovementUseCase = cryptoMovementUseCase;
    }

    public boolean createCryptoManagement(CryptoManagementEntity request) {
        ConsumerAidDTO consumerAidDTO = ConsumerAidDTO.builder().consumer(request.getConsumer())
                .aid(request.getAid()).build();
        if (!isConsumerWithAid(consumerAidDTO)) {
            LocalDateTime date = LocalDateTime.now(ZoneOffset.of("-05:00"));
            String dateString = date.format(
                    DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_TIME_ZONE));
            Timestamp timestamp = Timestamp.valueOf(dateString);
            request.setStatus(Constants.ACTIVE);
            request.setCreatedAt(timestamp);
            request.setLastStatusDate(timestamp);
            request.setFailAttempts(Constants.ONE);
            var managementEntity = saveManagement(request);

            CryptoMovementEntity movementEntity = setCryptoMovementEntity(managementEntity);
            movementEntity.setMovement(ConstantsMovements.KEY_CREATED);
            cryptoMovementUseCase.createCryptoMovement(movementEntity,
                    consumerAidDTO.getConsumer());
            return true;
        } else {
            return false;
        }
    }

    public CryptoManagementEntity saveManagement(CryptoManagementEntity request) {
        return managementRepository.save(request);
    }

    public boolean isConsumerWithAid(ConsumerAidDTO consumerAidDTO) {
        return findByAid(consumerAidDTO) != null;
    }

    public CryptoManagementEntity findByAid(ConsumerAidDTO consumerAidDTO) {
        return managementRepository.findByAid(consumerAidDTO.getAid(),
                consumerAidDTO.getConsumer());
    }


    private CryptoMovementEntity setCryptoMovementEntity(CryptoManagementEntity request) {
        return CryptoMovementEntity.builder().
                aid(request.getAid()).
                createdAt(request.getCreatedAt()).
                cryptoManagementId(request.getCryptoManagementId()).
                build();
    }
}
