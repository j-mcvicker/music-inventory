/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicinventory;

import javafx.scene.text.Text;
import java.util.Objects;

/**
 *
 * @author Justin
 */
public class Release {
    private String artist;
    private String title;
    private int quantity;
    private float price;
        
    public Release(String a, String t, int q, float p) {
        artist = a;
        title = t;
        quantity = q;
        price = p;
    }
    
    
    public void setArtist(String s) {
        artist = s;
    }
    
    public void setTitle(String s) {
        title = s;
    }
    
    public void setQuantity(int q) {
        quantity = q;
    }
    
    public void setPrice(float p) {
        price = p;
    }
    
    public String getArtist() {
        return artist;
    }
    
    public String getTitle() {
        return title;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public float getPrice() {
        return price;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Release) {
            Release rel = (Release) obj;
            if (rel.getArtist().equals(this.getArtist()) && rel.getTitle().equals(this.getTitle())) {
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        //return Objects.hash(artist, title);
        int hash = 0;
        int p = 31;
        int m = 10^9;
        
        for (int i = 0; i < artist.length(); i++) {
            hash += (int) artist.charAt(i);
        }
        
        for (int i = 0; i < title.length(); i++) {
            hash += (int) title.charAt(i) ;
        }
        return hash;
    }
   
}
