package com.example.ambit.mytrafficcontrolbee;

public class ModelClassForGridView {
    String locationName;

    public ModelClassForGridView() {
    }

    public ModelClassForGridView(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
