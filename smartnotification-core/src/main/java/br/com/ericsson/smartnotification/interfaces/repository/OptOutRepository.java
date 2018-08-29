package br.com.ericsson.smartnotification.interfaces.repository;

public interface OptOutRepository {

    void add(String msisdn);

    boolean hasOptedOut(String msisdn);

    void remove(String msisdn);

}