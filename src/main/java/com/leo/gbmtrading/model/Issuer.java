package com.leo.gbmtrading.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "issuerName",
    "totalShares",
    "sharePrice"
})
public class Issuer implements Serializable {    
    @JsonProperty("issuerName")
    private String issuerName;
    @JsonProperty("totalShares")
    private Integer totalShares = 0;
    @JsonProperty("sharePrice")
    private Integer sharePrice = 0;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -2636222230300443413L;
    
    private final Lock lock = new ReentrantLock();
    
    @JsonProperty("issuerName")
    public String getIssuerName() {
        return issuerName;
    }
    
    @JsonProperty("issuerName")
    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }
    
    @JsonProperty("totalShares")
    public Integer getTotalShares() {
    	Integer r;
    	lock.lock();
        r = totalShares;
        lock.unlock();
        return r;
    }
    
    @JsonProperty("totalShares")
    public void setTotalShares(Integer totalShares) {    	
    	lock.lock();
    	this.totalShares = totalShares;
        lock.unlock();    
    }
    
    @JsonProperty("sharePrice")
    public Integer getSharePrice() {
        return sharePrice;
    }
    
    @JsonProperty("sharePrice")
    public void setSharePrice(Integer sharePrice) {
    	this.sharePrice = sharePrice;
    }

    public void updateNoOfShares(int shares) {
    	lock.lock();
    	this.totalShares += shares;
        lock.unlock();  
    }
    
    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }
    
    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }    
}
