package com.example.cardsnew.repositories;



import com.example.cardsnew.entities.Card;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CardRepository  extends JpaRepository<Card, Long>, JpaSpecificationExecutor<Card> {

}