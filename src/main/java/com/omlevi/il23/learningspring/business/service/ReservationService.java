package com.omlevi.il23.learningspring.business.service;

import com.omlevi.il23.learningspring.business.domain.RoomReservation;
import com.omlevi.il23.learningspring.data.entity.Guest;
import com.omlevi.il23.learningspring.data.entity.Reservation;
import com.omlevi.il23.learningspring.data.entity.Room;
import com.omlevi.il23.learningspring.data.repository.GuestRepository;
import com.omlevi.il23.learningspring.data.repository.ReservationRepository;
import com.omlevi.il23.learningspring.data.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(RoomRepository roomRepository, GuestRepository guestRepository,
                              ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.guestRepository = guestRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<RoomReservation> getRoomsReservationsForDate(Date date){
        Iterable<Room> rooms = this.roomRepository.findAll();
        Map<Long,RoomReservation> roomReservationMap = new HashMap();
        rooms.forEach(room -> {
           RoomReservation roomReservation = setRoomReservationsFields(room);
           roomReservationMap.put(room.getRoomId(),roomReservation);
        } );
        Iterable<Reservation> reservations = this.reservationRepository.findReservationByReservationDate(new java.sql.
                Date(date.getTime()));
        reservations.forEach(reservation -> {
            RoomReservation roomReservation = roomReservationMap.get(reservation.getRoomId());
            roomReservation.setDate(date);
            setGuestFields(roomReservation, reservation);
        });
        List<RoomReservation> roomReservations = new ArrayList<>();
        for(Long id: roomReservationMap.keySet()){
            roomReservations.add(roomReservationMap.get(id));
        }
        return roomReservations;
    }

    private void setGuestFields(RoomReservation roomReservation, Reservation reservation) {
        Guest guest = this.guestRepository.findById(reservation.getGuestId()).get();
        roomReservation.setFirstName(guest.getFirstName());
        roomReservation.setLastName(guest.getLastName());
        roomReservation.setGuestId(guest.getGuestId());
    }

    private RoomReservation setRoomReservationsFields(Room room) {
        RoomReservation roomReservation = new RoomReservation();
        roomReservation.setRoomId(room.getRoomId());
        roomReservation.setRoomName(room.getRoomName());
        roomReservation.setRoomNumber(room.getRoomNumber());
        return roomReservation;
    }

    public List<Guest> getHotelGuests(){
        Iterable<Guest> guests = this.guestRepository.findAll();
        List<Guest> guestList = new ArrayList<>();
        guests.forEach(guest->{guestList.add(guest);});
        guestList.sort(new Comparator<Guest>() {
            @Override
            public int compare(Guest o1, Guest o2) {
                if (o1.getLastName() == o2.getLastName()){
                    return o1.getFirstName().compareTo(o2.getFirstName());
                }
                return o1.getLastName().compareTo(o2.getLastName());
            }
        });
        return guestList;
    }
}
