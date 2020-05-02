package com.melanesian.param;

import java.util.List;

public class ClientAddress {

    private String detailAdress;
    private List<Address> addresses;

    public String getDetailAdress() {
        return detailAdress;
    }

    public void setDetailAdress(String detailAdress) {
        this.detailAdress = detailAdress;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public boolean containAddressName(String addressName) {
        if (addresses != null) {
            for (Address address : this.addresses){
                if (address.getAddressName().equals(addressName.toString()))
                    return true;
            }
        }
        return false;
    }

    public boolean containAddressNameAndCityName(String addressName, String cityName) {
        if (addresses != null) {
            for (Address address : this.addresses){
                if (address.getAddressName().equals(addressName.toString()) && address.getCity().getCityName().equals(cityName.toString())) {
                    return true;
                }
            }
        }
        return false;
    }
}
