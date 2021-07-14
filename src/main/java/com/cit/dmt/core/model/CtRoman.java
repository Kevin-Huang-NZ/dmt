package com.cit.dmt.core.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;

public class CtRoman {
    
    @ExcelIgnore
    private Long id;

    
    @ExcelIgnore
    private String countryCode;

    
    @ExcelIgnore
    private String languageCode;

    
    @ExcelProperty(index = 0)
    private String originalAlpha;

    
    @ExcelProperty(index = 1)
    private String romanAlpha;

    
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

    
    public String getLanguageCode() {
        return languageCode;
    }

    
    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode == null ? null : languageCode.trim();
    }

    
    public String getOriginalAlpha() {
        return originalAlpha;
    }

    
    public void setOriginalAlpha(String originalAlpha) {
        this.originalAlpha = originalAlpha == null ? null : originalAlpha.trim();
    }

    
    public String getRomanAlpha() {
        return romanAlpha;
    }

    
    public void setRomanAlpha(String romanAlpha) {
        this.romanAlpha = romanAlpha == null ? null : romanAlpha.trim();
    }
}
