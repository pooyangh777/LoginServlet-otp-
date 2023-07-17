package dto;

public enum DeviceType {
    MOBILE_PHONE("Mobile Phone"),
    DESKTOP("Desktop"),
    TABLET("Tablet"),
    CONSOLE("Console"),
    TV_DEVICE("TV Device"),
    LEGACY_DEVICE("Legacy Device");

    private String value;

    public String getValue() {
        return value;
    }

    DeviceType(String value) {
        this.value = value;
    }
}

