package com.example.lesson4task1.repostory;

import com.example.lesson4task1.entity.Card;
import com.example.lesson4task1.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncomeRepostory extends JpaRepository<Income,Integer> {

    List<Income>findAllByToCard_Number(String toCard_number);
}
