package co.com.bancolombia.savebackup;

public class SaveBackupRequest {

    private LogDataRequest[] data;

    public SaveBackupRequest(LogDataRequest data) {
        this.data = new LogDataRequest[]{data};
    }

    public LogDataRequest[] getData() {
        return data;
    }
}
