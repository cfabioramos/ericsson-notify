package br.com.ericsson.smartnotification.web.domain;

public class BreadCrumbItem {
    
    private String link;
    
    private String name;

    public BreadCrumbItem(String link, String name) {
        this.link = link;
        this.name = name;
    }
    
    public BreadCrumbItem(String name) {
        this.link = "#!";
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public String getName() {
        return name;
    }
    
    
}
