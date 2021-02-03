package com.omlevi.il23.learningspring.data.repository;

import com.omlevi.il23.learningspring.data.entity.Guest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GuestRepository extends CrudRepository<Guest,Long> {
}
