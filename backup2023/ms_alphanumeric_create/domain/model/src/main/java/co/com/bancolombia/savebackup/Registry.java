package co.com.bancolombia.savebackup;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Registry {

    private final String sourceId;
    private final String actor;
    private final String documentType;
    private final String document;
    private final String transactionalId;

    public String getSourceId() {
        return sourceId;
    }

    public String getActor() {
        return actor;
    }

    public String getTransactionalId() {
        return transactionalId;
    }

    public String getDocumentType() {
        return documentType;
    }

    public String getDocument() {
        return document;
    }
}
