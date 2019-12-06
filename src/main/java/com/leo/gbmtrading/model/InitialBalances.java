package com.leo.gbmtrading.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
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
    "cash",
    "issuers"
})
public class InitialBalances implements Serializable {
    
    @JsonProperty("cash")
    private Integer cash = 0;
    @JsonProperty("issuers")
    private List<Issuer> issuers = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 3086573524075651516L;
    
    final private Lock lock = new ReentrantLock();

    public int updateCash(int cash) {
    	Integer r;
    	lock.lock();
    	r = this.cash += cash;
    	lock.unlock();
    	return r;
    }
    
    @JsonProperty("cash")
    public Integer getCash() {
    	Integer r;
    	lock.lock();
    	r = cash;
    	lock.unlock();
        return r;
    }
    
    @JsonProperty("cash")
    public void setCash(Integer cash) {
    	lock.lock();
    	this.cash = cash;
    	lock.unlock();    
    }
    
    @JsonProperty("issuers")
    public List<Issuer> getIssuers() {
        return issuers;
    }
    
    @JsonProperty("issuers")
    public void setIssuers(List<Issuer> issuers) {
        this.issuers = issuers;
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
