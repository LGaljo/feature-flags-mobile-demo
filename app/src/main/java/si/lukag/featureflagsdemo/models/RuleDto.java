package si.lukag.featureflagsdemo.models;

public class RuleDto {
    private Long id;
    private String name;
    private String description;
    private DataType dataType;
    private int value;

    public RuleDto() {
    }

    public RuleDto(Long id, String name, String description, DataType dataType, int value) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.dataType = dataType;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "RuleDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dataType=" + dataType +
                ", value=" + value +
                '}';
    }
}