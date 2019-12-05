package com.leo.gbmtrading.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "initialBalances"
})
public class InitialBalance implements Serializable {
    
    @JsonProperty("initialBalances")
    private InitialBalances initialBalances;
    private List<String> businessErrors = new ArrayList<>();
    
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 8723222155785443049L;
    
    @JsonProperty("initialBalances")
    public InitialBalances getInitialBalances() {
        return initialBalances;
    }
    
    @JsonProperty("initialBalances")
    public void setInitialBalances(InitialBalances initialBalances) {
        this.initialBalances = initialBalances;
    }
    
    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }
    
    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

	public List<String> getBusinessErrors() {
		return businessErrors;
	}

	public void setBusinessErrors(List<String> businessErrors) {
		this.businessErrors = businessErrors;
	}
    
}
