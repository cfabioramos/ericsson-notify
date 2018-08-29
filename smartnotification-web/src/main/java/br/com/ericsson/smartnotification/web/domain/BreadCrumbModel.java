package br.com.ericsson.smartnotification.web.domain;

import java.util.Arrays;
import java.util.List;

public class BreadCrumbModel {

    private List<BreadCrumbItem> items;

    public BreadCrumbModel(BreadCrumbItem[] items) {
        this.items = Arrays.asList(items);
    }

    public List<BreadCrumbItem> getItems() {
        return items;
    }
    
}
