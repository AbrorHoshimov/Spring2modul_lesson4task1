package com.example.lesson4task1.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class IncomeDto {
    private String from;
    private String to;
    private double amount;
}
