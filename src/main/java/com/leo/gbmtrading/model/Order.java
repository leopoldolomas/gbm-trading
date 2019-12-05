package com.leo.gbmtrading.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "timestamp",
    "operation",
    "IssuerName",
    "TotalShares",
    "SharePrice"
})
public class Order implements Serializable
{
    public enum OrderType { BUY, SELL };
    
    @JsonProperty("timestamp")
    private Integer timestamp;
    @JsonProperty("operation")
    private String operation;
    @JsonProperty("IssuerName")
    private String issuerName;
    @JsonProperty("TotalShares")
    private Integer totalShares;
    @JsonProperty("SharePrice")
    private Integer sharePrice;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -4402736768091087120L;

    private OrderType orderType;
    
    @Override
    public int hashCode() {
    	return getIssuerName().hashCode() + getOrderType().hashCode() + getTotalShares() * 7;
    }
    
    @Override
    public boolean equals(Object obj) {
    	if (obj == null || !(obj instanceof Order)) {
    		return false;
    	}

    	Order other = (Order)obj;
    	return other.getIssuerName().equals(getIssuerName()) &&
    			other.getOrderType().equals(getOrderType()) &&
    			other.getTotalShares() == getTotalShares();
    }
    
    @JsonProperty("timestamp")
    public Integer getTimestamp() {
        return timestamp;
    }
    
    @JsonProperty("timestamp")
    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }
    
    @JsonProperty("operation")
    public String getOperation() {
        return operation;
    }
    
    @JsonProperty("operation")
    public void setOperation(String operation) {
        Objects.requireNonNull(operation);
        this.operation = operation;
        orderType = (operation.hashCode() == "BUY".hashCode() ? OrderType.BUY : OrderType.SELL);
    }
    
    @JsonProperty("IssuerName")
    public String getIssuerName() {
        return issuerName;
    }
    
    @JsonProperty("IssuerName")
    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }
    
    @JsonProperty("TotalShares")
    public Integer getTotalShares() {
        return totalShares;
    }
    
    @JsonProperty("TotalShares")
    public void setTotalShares(Integer totalShares) {
        this.totalShares = totalShares;
    }
    
    @JsonProperty("SharePrice")
    public Integer getSharePrice() {
        return sharePrice;
    }
    
    @JsonProperty("SharePrice")
    public void setSharePrice(Integer sharePrice) {
        this.sharePrice = sharePrice;
    }
    
    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }
    
    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public OrderType getOrderType() {
        return orderType;
    }
    
}
