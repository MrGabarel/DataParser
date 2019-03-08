public class Employment2016 {
    private int totalLaborForce;
    private int employedLaborForce;
    private int unemplotedLaborForce;
    private double unemployedPercent;

    public Employment2016(int totalLaborForce, int employedLaborForce, int unemplotedLaborForce, double unemployedPercent) {
        this.totalLaborForce = totalLaborForce;
        this.employedLaborForce = employedLaborForce;
        this.unemplotedLaborForce = unemplotedLaborForce;
        this.unemployedPercent = unemployedPercent;
    }

    public int getTotalLaborForce() {
        return totalLaborForce;
    }

    public void setTotalLaborForce(int totalLaborForce) {
        this.totalLaborForce = totalLaborForce;
    }

    public int getEmployedLaborForce() {
        return employedLaborForce;
    }

    public void setEmployedLaborForce(int employedLaborForce) {
        this.employedLaborForce = employedLaborForce;
    }

    public int getUnemplotedLaborForce() {
        return unemplotedLaborForce;
    }

    public void setUnemplotedLaborForce(int unemplotedLaborForce) {
        this.unemplotedLaborForce = unemplotedLaborForce;
    }

    public double getUnemployedPercent() {
        return unemployedPercent;
    }

    public void setUnemployedPercent(double unemployedPercent) {
        this.unemployedPercent = unemployedPercent;
    }
}
