/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliveryoptimiser;

/**
 * Stores item information
 * 
 * @author Steven McGuckin
 */
public class Item {
    private String name;
    private int length;
    private int width;
    private int height;
    private int weight;
    private String type;
    private Customer from;
    private Customer to;
    
    public Item(String name, int length, int width, int height, int weight, String type, Customer from, Customer to){
        this.name = name;
        this.length = length;
        this.width = width;
        this.height = height;
        this.weight = weight;
        this.type = type;
        this.from = from;
        this.to = to; 
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the length
     */
    public int getLength() {
        return length;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return the weight
     */
    public int getWeight() {
        return weight;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the from
     */
    public Customer getFrom() {
        return from;
    }

    /**
     * @return the to
     */
    public Customer getTo() {
        return to;
    }


    

    
    
}
