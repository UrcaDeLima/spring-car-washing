package com.boots.entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table (name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    @Column(name = "reservation_time")
    private Date reservation_time;

    @ManyToOne (optional=false, cascade=CascadeType.ALL)
    @JoinColumn (name="users_id")
    private User user;

    @ManyToOne (optional=false, cascade=CascadeType.ALL)
    @JoinColumn (name="car_services_id")
    private CarServices carServices;

    public Reservation() {
    }

    public Reservation(Date reservation_time, User user, CarServices carServices) {
        this.reservation_time = reservation_time;
        this.user = user;
        this.carServices = carServices;
    }

    public void updateReservation(final Reservation newReservation) {
        this.reservation_time = newReservation.reservation_time;
        this.user = newReservation.user;
        this.carServices = newReservation.carServices;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getReservation_time() {
        return reservation_time;
    }

    public void setReservation_time(Date reservation_time) {
        this.reservation_time = reservation_time;
    }

    public User getUsers() {
        return user;
    }

    public void setUsers(User user) {
        this.user = user;
    }

    public CarServices getCarServices() {
        return carServices;
    }

    public void setCarServices(CarServices carServices) {
        this.carServices = carServices;
    }

    @Override
    public String toString() {
        return "Reservations{" +
                "id=" + id +
                ", reservation_time=" + reservation_time +
                ", users=" + user +
                ", carServices=" + carServices +
                '}';
    }
}