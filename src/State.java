import java.util.List;

public class State {
    private String name;
    private List<County> counties;

    public State(String name, List<County> counties) {
        this.name = name;
        this.counties = counties;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<County> getCounties() {
        return counties;
    }

    public void setCounties(List<County> counties) {
        this.counties = counties;
    }
}
