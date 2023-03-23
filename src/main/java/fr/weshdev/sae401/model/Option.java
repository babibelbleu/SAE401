package fr.weshdev.sae401.model;

public class Option{
    private String name;
    private String description;
    private boolean isActive;

    public Option(String name, String description, boolean isActive) {
        this.name = name;
        this.description = description;
        this.isActive = isActive;
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
    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void reset(){
        this.isActive = false;
        this.name = null;
        this.description = null;
    }
}
