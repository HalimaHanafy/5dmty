package models;

/**
 * Created by Asmaa on 22/12/2015.
 */
public class market {
    private  String market_id ;
    private  String market_name ;
    private  String market_url ;
    private  String market_details ;
    private  String market_other ;
    private  double market_long ;
    private  double market_latt ;
    private  String place_name ;

    public void setMarket_id(String market_id) {
        this.market_id = market_id;
    }

    public void setMarket_name(String market_name) {
        this.market_name = market_name;
    }

    public void setMarket_url(String market_url) {
        this.market_url = market_url;
    }

    public void setMarket_details(String market_details) {
        this.market_details = market_details;
    }

    public void setMarket_other(String market_other) {
        this.market_other = market_other;
    }

    public void setMarket_long(double market_long) {
        this.market_long = market_long;
    }

    public void setMarket_latt(double market_latt) {
        this.market_latt = market_latt;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public String getMarket_id() {
        return market_id;
    }

    public String getMarket_name() {
        return market_name;
    }

    public String getMarket_url() {
        return market_url;
    }

    public String getMarket_details() {
        return market_details;
    }

    public String getMarket_other() {
        return market_other;
    }

    public double getMarket_long() {
        return market_long;
    }

    public double getMarket_latt() {
        return market_latt;
    }

    public String getPlace_name() {
        return place_name;
    }
}
