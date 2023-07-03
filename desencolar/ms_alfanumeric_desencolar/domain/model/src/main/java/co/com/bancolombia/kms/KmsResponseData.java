package co.com.bancolombia.kms;


import co.com.bancolombia.reponse.Meta;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class KmsResponseData {

    private Meta meta;
    private DataKMSResponse[] data;
    private Error[] errors;
}