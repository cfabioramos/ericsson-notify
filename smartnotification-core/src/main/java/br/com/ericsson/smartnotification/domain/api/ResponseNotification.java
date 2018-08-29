package br.com.ericsson.smartnotification.domain.api;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ResponseNotification implements Serializable {
   
    private static final long serialVersionUID = 1L;
    
    private static final int SUCESS_RESULT = 1;

    @SerializedName("multicast_id") 
    private long multicastId;
    
    private int success;
    
    private int failure;
    
    @SerializedName("canonical_ids") 
    private int canonicalIds;
    
    private List<ResultNotification> results;

    public ResponseNotification(long multicastId, int success, int failure, int canonicalIds, List<ResultNotification> results) {
        super();
        this.multicastId = multicastId;
        this.success = success;
        this.failure = failure;
        this.canonicalIds = canonicalIds;
        this.results = results;
    }

    public long getMulticastId() {
        return multicastId;
    }

    public void setMulticastId(long multicastId) {
        this.multicastId = multicastId;
    }

    public int getFailure() {
        return failure;
    }

    public void setFailure(int failure) {
        this.failure = failure;
    }

    public int getCanonicalIds() {
        return canonicalIds;
    }

    public void setCanonicalIds(int canonicalIds) {
        this.canonicalIds = canonicalIds;
    }

    public List<ResultNotification> getResults() {
        return results;
    }

    public void setResults(List<ResultNotification> results) {
        this.results = results;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
    
    public boolean isSucess() {
        return SUCESS_RESULT == this.success;
    }
}
