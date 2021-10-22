package com.bits.demo.sticky.repository;

import com.bits.demo.sticky.model.Notes;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotesRepository extends CrudRepository<Notes, Long> {

    Optional<Notes> findByTitle(String title);
}
