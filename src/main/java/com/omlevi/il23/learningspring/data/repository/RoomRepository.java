package com.omlevi.il23.learningspring.data.repository;

import com.omlevi.il23.learningspring.data.entity.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends CrudRepository<Room,Long> {
}
