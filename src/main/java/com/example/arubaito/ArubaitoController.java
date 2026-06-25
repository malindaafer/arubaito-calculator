package com.example.arubaito;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArubaitoController {

    @GetMapping("/calculate")
    public ShiftResult calculateShift(
            @RequestParam("start") String start,
            @RequestParam("end") String end,
            @RequestParam("breakMins") int breakMins,
            @RequestParam("wage") double wage) {
        
        // ShiftCalculator එකෙන් ගණන් හදලා ShiftResult object එකක් return කරනවා
        return ShiftCalculator.calculate(start, end, breakMins, wage);
    }
}