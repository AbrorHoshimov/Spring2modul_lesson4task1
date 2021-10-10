package com.example.lesson4task1.controller;

import com.example.lesson4task1.entity.Card;
import com.example.lesson4task1.entity.Income;
import com.example.lesson4task1.entity.Outcome;
import com.example.lesson4task1.payload.IncomeDto;
import com.example.lesson4task1.repostory.CardRepostory;
import com.example.lesson4task1.repostory.IncomeRepostory;
import com.example.lesson4task1.repostory.OutcomeRepostory;
import com.example.lesson4task1.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/card")
public class CardControl {
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    OutcomeRepostory outcomeRepostory;
    @Autowired
    IncomeRepostory incomeRepostory;

    @Autowired
    CardRepostory cardRepostory;
    @PostMapping
    public HttpEntity<?>add(HttpServletRequest httpRequest){
        String token = httpRequest.getHeader("Authorization");
        if (token!=null&&token.startsWith("Bearer")){
            token=token.substring(7);
            String user = jwtProvider.getUSerFromToken(token);
            Card card =new Card();
            card.setUsername(user);
            card.setBalance(0);
            String ranNum = UUID.randomUUID().toString();
            ranNum=ranNum.substring(0,4);
            card.setNumber(ranNum);
            card.setExperidDate(new Date(System.currentTimeMillis()+(1000*86400*365*2)));
            cardRepostory.save(card);

        }
        return ResponseEntity.ok("karta ochildi");
    }
    @PostMapping("/transfer")
    public ResponseEntity<?>in_out(HttpServletRequest httpServletRequest, @RequestBody IncomeDto incomeDto){
        String token = httpServletRequest.getHeader("Authorization");
        if (token!=null&&token.startsWith("Bearer")) {
            token = token.substring(7);
            String user = jwtProvider.getUSerFromToken(token);
            Optional<Card> byUsername = cardRepostory.findByUsername(user);
            Card from = byUsername.get();
            if (from.getNumber().equals(incomeDto.getFrom())){
                Outcome outcome=new Outcome();
                outcome.setFromCard(cardRepostory.findByNumber(incomeDto.getFrom()).get());
                outcome.setToCard(cardRepostory.findByNumber(incomeDto.getTo()).get());
                outcome.setAmount(incomeDto.getAmount());
                outcome.setDate(new java.util.Date());
                double summa= incomeDto.getAmount()+(incomeDto.getAmount()*1/100);
                if (summa> from.getBalance()){
                    return ResponseEntity.status(409).body("hisobingizda mablag yetarli emas");
                }
                from.setBalance(from.getBalance()-summa);
                cardRepostory.save(from);
                Optional<Card> tocardOption = cardRepostory.findByNumber(incomeDto.getTo());
                Card toCard = tocardOption.get();
                toCard.setBalance(toCard.getBalance()+incomeDto.getAmount());
                cardRepostory.save(toCard);

                Income income =new Income();
                income.setFromCard(cardRepostory.findByNumber(incomeDto.getTo()).get());
                income.setToCard(cardRepostory.findByNumber(incomeDto.getFrom()).get());
                income.setAmount(incomeDto.getAmount());
                income.setDate(new java.util.Date());

                outcomeRepostory.save(outcome);
                incomeRepostory.save(income);
            }
        }
        return ResponseEntity.ok("Ok");
    }
    @GetMapping("/search")
    public HttpEntity<?> getByNumber(@RequestParam String number){
        List<Income> byToCard_number = incomeRepostory.findAllByToCard_Number(number);
        return  ResponseEntity.ok(byToCard_number);

    }
    @GetMapping
    public HttpEntity<?> getByNumberOut(@RequestParam String number){
        List<Outcome> allByToCard_number = outcomeRepostory.findAllByToCard_Number(number);
        return  ResponseEntity.ok(allByToCard_number);

    }
}
