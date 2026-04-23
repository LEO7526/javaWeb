package ict.bean;

public class ClinicService {
    private final int id;
    private final String clinicName;
    private final String serviceName;
    private final int dailyQuota;

    public ClinicService(int id, String clinicName, String serviceName, int dailyQuota) {
        this.id = id;
        this.clinicName = clinicName;
        this.serviceName = serviceName;
        this.dailyQuota = dailyQuota;
    }

    public int getId() {
        return id;
    }

    public String getClinicName() {
        return clinicName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public int getDailyQuota() {
        return dailyQuota;
    }
}


