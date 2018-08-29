package br.com.ericsson.smartnotification.interfaces.repository;

public interface TokenRepository {
    
    public void put(String key, String token);

    public String get(String key);

    public void delete(String key);
}

