package com.cit.dmt.core.model;

public class Country {
    
    private Long id;

    
    private String countryCode;

    
    private String countryName;

    
    public Long getId() {
        return id;
    }

    
    public void setId(Long id) {
        this.id = id;
    }

    
    public String getCountryCode() {
        return countryCode;
    }

    
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode == null ? null : countryCode.trim();
    }

    
    public String getCountryName() {
        return countryName;
    }

    
    public void setCountryName(String countryName) {
        this.countryName = countryName == null ? null : countryName.trim();
    }
}
