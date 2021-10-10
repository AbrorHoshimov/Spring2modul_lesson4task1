package com.example.lesson4task1.repostory;

import com.example.lesson4task1.entity.Card;
import com.example.lesson4task1.entity.Income;
import com.example.lesson4task1.entity.Outcome;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutcomeRepostory extends JpaRepository<Outcome,Integer> {
    List<Outcome> findAllByToCard_Number(String toCard_number);
}
