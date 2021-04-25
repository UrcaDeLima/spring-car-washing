package com.boots.entity;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table (name = "car_services")
public class CarServices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "execution_time")
    private int execution_time;

    @Column(name = "price")
    private float price;

    @OneToMany (mappedBy="carServices", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    private Collection<Reservation> reservations;

    public CarServices() {
    }

    public CarServices(String title, int execution_time, float price) {
        this.title = title;
        this.execution_time = execution_time;
        this.price = price;
    }

    public void updateCarServices(final CarServices newCarServices) {
        this.title = newCarServices.title;
        this.execution_time = newCarServices.execution_time;
        this.price = newCarServices.price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getExecution_time() {
        return execution_time;
    }

    public void setExecution_time(int execution_time) {
        this.execution_time = execution_time;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "CarServices{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", execution_time=" + execution_time +
                ", price=" + price +
                '}';
    }
}