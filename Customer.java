/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliveryoptimiser;

/**
 * Stores customer (pickup and recipient) information
 * @author Highway62
 */
public class Customer {
    private String name;
    private String addr;
    private String city;
    private String postCode;
    private String tel;
    private int custNo;
    
    public Customer(String name, String addr, String city, String postCode, String tel, int custNo){
        this.name = name;
        this.addr = addr;
        this.city = city;
        this.postCode = postCode;
        this.tel = tel;
        this.custNo = custNo;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the addr
     */
    public String getAddr() {
        return addr;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @return the postCode
     */
    public String getPostCode() {
        return postCode;
    }

    /**
     * @return the tel
     */
    public String getTel() {
        return tel;
    }

    /**
     * @return the custNo
     */
    public int getCustNo() {
        return custNo;
    }
    
    
    
    
}
