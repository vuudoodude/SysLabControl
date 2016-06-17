/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package syslabcontrol;

/**
 *
 * @author USB_Room
 */
public class Channel implements Comparable{
    String channelName, address, type, group;
    transient sortBy currentSort;
    
    public enum sortBy { NAME, ADDRESS, TYPE, GROUP };
    
    
    public Channel(String name, String address, String type)
    {
        this.channelName = name;
        this.address = address;
        this.type = type;
        this.group = "";
        currentSort = sortBy.ADDRESS;
    }
    
    
    public void setSort(sortBy sort)
    {
        currentSort = sort;
    }

    @Override
    public int compareTo(Object o) {
        
        if (currentSort == sortBy.NAME)
        {
            return this.channelName.compareTo(((Channel)o).getName());
        }
        if (currentSort == sortBy.TYPE)
        {
            return this.type.compareTo(((Channel)o).getType());
        }
        if (currentSort == sortBy.GROUP)
        {
            return this.group.compareTo(((Channel)o).getGroup());
        }
        //default case: sort by address;
        return this.address.compareTo(((Channel)o).getAddress());
    }
    
    
    public String getGroup() {
        return group;
    }

    /* Getters and Setters here:
     */
    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return channelName;
    }

    public void setName(String name) {
        this.channelName = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
}
