package team2.sofa.sofa.model;

public enum BusinessSector {
    AGRARISCH("Agrarisch"),
    BOUW("Bouw"),
    FINANCIEEL("Financiele dienstverlening"),
    ZORG("Zorg"),
    INDUSTRIE("Industie"),
    OVERHEID_EN_ONDERWIJS("Overheid & Onderwijs"),
    VASTGOED("Vastgoed"),
    RETAIL("Retail"),
    TECH_EN_TELECOM("Tech & Telecom"),
    TRANSPORT("Transport sector"),
    ZAKELIJKE_DIENSTVERLENING("Zakelijke Dienstverlening");

    private final String displayValue;

    private BusinessSector(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
