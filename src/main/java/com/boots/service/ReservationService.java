package com.boots.service;

import com.boots.entity.Reservation;
import com.boots.entity.Role;
import com.boots.entity.User;
import com.boots.repository.ReservationRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static java.lang.Integer.parseInt;

@Service
public class ReservationService {
    @PersistenceContext
    private EntityManager em;
    private final ReservationRepository reservationRepository;
    private final CarServicesService carServicesService;
    private final UserService userService;

    public ReservationService(
            ReservationRepository reservationRepository,
            CarServicesService carServicesService,
            UserService userService
    ) {
        this.reservationRepository = reservationRepository;
        this.carServicesService = carServicesService;
        this.userService = userService;
    }

    public Reservation findReservationById(int reservationId) {
        User user = validateUserRole();

        switch(getMaxRole(user)){
            case(1):
                return findReservationByIdAndUserId(reservationId, user.getId());
            case(2):
                return reservationRepository.findReservationById(reservationId);
            default:
                throw new SecurityException();
        }
    }

    public Long findPeoplesInQueue(int id) {
        Reservation reservation = findReservationById(id);

        try{
            Query query = em.createNativeQuery(
                    "SELECT * FROM reservations WHERE reservation_time BETWEEN :from AND :to",
                    Reservation.class
            );
            query.setParameter("from", LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
            query.setParameter("to", reservation.getReservation_time());
            List<Reservation> reservationsList = (List<Reservation>) query.getResultList();

            Long count = reservationsList.stream().count();
            if(count == 0){
                return count;
            }
            return count - 1;
        }catch (NullPointerException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public String getTimeInterval(int id) {
        Reservation reservation = findReservationById(id);
        if(reservation == null){
            return null;
        }

        final long intervalTime = reservation.getReservation_time().getTime() - LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        final long intervalDays = intervalTime / (24 * 60 * 60 * 1000);
        final long intervalHours = intervalTime / (60 * 60 * 1000) % 24;
        final long intervalMinutes = intervalTime / (60 * 1000) % 60;

        if(intervalMinutes < 0 || intervalHours < 0 || intervalDays < 0){
            return "Ваша очередь прошла";
        }
        return "Ваша услуга будет доступна через: " + intervalDays + " " + lastDigitToWord(intervalDays) + " и " + intervalHours + ":"  + intervalMinutes;
    }

    private String lastDigitToWord(Long intervalDays){
        Long lastFigure = intervalDays%10;
        if (intervalDays >= 11 && intervalDays < 15) {
            return "дней";
        }else {
            if (lastFigure == 1) return "день";
            if (lastFigure > 1 && lastFigure < 5) return "дня";
            if (lastFigure == 0 || lastFigure >= 5) return "дней";
        }
        return null;
    }

    public Map<String, Object> getReservationQueue(int id) {
        return new HashMap<String, Object>() {
            {
                put("placeQueue", findPeoplesInQueue(id));
                put("timeToService", getTimeInterval(id));
                put("reservation", findReservationById(id));
            }
        };
    }

    public List<Reservation> findAllReservationByUserId(Long userId) {
        Query query = em.createNativeQuery(
                "SELECT * FROM reservations WHERE users_id = :id",
                Reservation.class
        );
        query.setParameter("id", userId);

        return (List<Reservation>) query.getResultList();
    }

    public Reservation findReservationByIdAndUserId(int reservationId, Long userId) {
        Query query = em.createNativeQuery(
                "SELECT * FROM reservations WHERE id = :reservationId AND users_id = :userId",
                Reservation.class
        );
        query.setParameter("reservationId", reservationId);
        query.setParameter("userId", userId);

        Reservation reservation;

        try{
            reservation = (Reservation) query.getSingleResult();
        }catch (NoResultException e) {
            e.printStackTrace();
            return null;
        }

        return reservation;
    }

    public List<Reservation> getAllReservations() {
        User user = validateUserRole();

        switch(getMaxRole(user)){
            case(1):
                return findAllReservationByUserId(user.getId());
            case(2):
                return reservationRepository.findAll();
            default:
                throw new SecurityException();
        }
    }

    public int getMaxRole(User user) {
        ArrayList<Long> rolesId = new ArrayList<>();
        for (Role role : user.getRoles())
            rolesId.add(role.getId());

        return Math.toIntExact(Collections.max(rolesId));
    }

    public User validateUserRole() {
        String username = getCurrentUsername();
        return userService.loadUserByUsername(username);
    }

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }


    public boolean saveReservation(Reservation reservation) throws ParseException {
        SimpleDateFormat GMT_DATE_FORM = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
        Date reservationTime = reservation.getReservation_time();
        GMT_DATE_FORM.setTimeZone(TimeZone.getTimeZone("GMT+03"));

        Calendar calFrom = Calendar.getInstance();
        Calendar calTo = Calendar.getInstance();
        Calendar calCorrect = Calendar.getInstance();

        int execution_time = carServicesService
                .findCarServiceById(reservation.getCarServices().getId()).getExecution_time();

        //from
        calFrom.setTime(reservationTime);
        calFrom.add(Calendar.MINUTE,-1 * execution_time);
        String stringFrom = GMT_DATE_FORM.format(calFrom.getTime());
        calFrom.set(Calendar.HOUR_OF_DAY,8);
        calFrom.set(Calendar.MINUTE,00);
        Calendar upperBoundFrom = (Calendar) calFrom.clone();
        calFrom.set(Calendar.HOUR_OF_DAY,00);
        Calendar lowerBoundFrom = (Calendar) calFrom.clone();

        //to
        calTo.setTime(reservationTime);
        calTo.add(Calendar.MINUTE, 1 * execution_time);
        String stringTo = GMT_DATE_FORM.format(calTo.getTime());
        calTo.set(Calendar.HOUR_OF_DAY,18);
        calTo.set(Calendar.MINUTE,00);
        Calendar lowerBoundTo = (Calendar) calTo.clone();
        calTo.set(Calendar.HOUR_OF_DAY,24);
        Calendar upperBoundTo = (Calendar) calTo.clone();

        //CorrectReservationTime
        calCorrect.setTime(reservationTime);
        String stringCorrectReservationTime = GMT_DATE_FORM.format(calCorrect.getTime());

        SimpleDateFormat MY_SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date from;
        Date to;
        Date correctReservationTime;

        from = MY_SIMPLE_DATE_FORMAT.parse(stringFrom);
        to = MY_SIMPLE_DATE_FORMAT.parse(stringTo);
        correctReservationTime = MY_SIMPLE_DATE_FORMAT.parse(stringCorrectReservationTime);

        if(
                (
                        from.getTime() >= lowerBoundFrom.getTime().getTime() &&
                                from.getTime() < upperBoundFrom.getTime().getTime()
                ) ||
                (
                        to.getTime() > lowerBoundTo.getTime().getTime() &&
                                to.getTime() <= upperBoundTo.getTime().getTime()
                ) ||
                correctReservationTime.getTime() < LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        ){
            return false;
        }else{
            Query query = em.createNativeQuery(
                    "SELECT * FROM reservations WHERE reservation_time BETWEEN :from AND :to",
                    Reservation.class
            );
            query.setParameter("from", from);
            query.setParameter("to", to);
            List<Reservation> reservationsList = (List<Reservation>) query.getResultList();

            if(!reservationsList.isEmpty()){
                return false;
            }

            reservation.setUsers(validateUserRole());
            reservation.setCarServices(carServicesService.findCarServiceById(reservation.getCarServices().getId()));
            reservation.setReservation_time(correctReservationTime);

            reservationRepository.save(reservation);
            return true;
        }
    }

    @Transactional
    public void deleteReservation(int id) {
        System.out.println(111111);
        if (findReservationById(id) != null) {
            reservationRepository.deleteById(id);
        }
    }
}
