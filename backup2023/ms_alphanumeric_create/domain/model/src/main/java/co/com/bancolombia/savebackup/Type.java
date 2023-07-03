package co.com.bancolombia.savebackup;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Type {

    private final String classification;
    private final String action;
    private final String app;
    private final String process;

    public String getClassification() {
        return classification;
    }

    public String getAction() {
        return action;
    }

    public String getApp() {
        return app;
    }

    public String getProcess() {
        return process;
    }
}
