package com.example.arubaito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Duration;

public class ShiftCalculator {

    public static ShiftResult calculate(String startTimeStr, String endTimeStr, int breakMinutes, double hourlyWage) {
        LocalTime startLocalTime = LocalTime.parse(startTimeStr);
        LocalTime endLocalTime = LocalTime.parse(endTimeStr);

        // අද දවස පදනම් කරගනිමු
        LocalDate today = LocalDate.now();
        LocalDateTime startDateTime = LocalDateTime.of(today, startLocalTime);
        LocalDateTime endDateTime = LocalDateTime.of(today, endLocalTime);

        // වැදගත්: ඉවර වෙන වෙලාව පටන් ගත්තු වෙලාවට වඩා අඩු නම් (උදා: 18:00 ට පටන් ගෙන 01:00 ට ඉවර නම්)
        // ඒ කියන්නේ ඉවර වෙලා තියෙන්නේ ඊළඟ දවසේ. ඒ නිසා endDateTime එකට තව දවසක් එකතු කරනවා.
        if (endDateTime.isBefore(startDateTime) || endDateTime.isEqual(startDateTime)) {
            endDateTime = endDateTime.plusDays(1);
        }

        // 1. මුළු වැඩකල කාලය (මිනිත්තු වලින්)
        long totalWorkedMinutes = Duration.between(startDateTime, endDateTime).toMinutes();
        
        // සැබෑ වැඩ කල කාලය (බ්‍රේක් එක අඩු කරලා)
        double totalHours = (totalWorkedMinutes - breakMinutes) / 60.0;
        if (totalHours < 0) totalHours = 0;

        // 2. 深夜労働 (Night Hours) ගණනය කිරීම - රෑ 10:00 ත් පසුදා පාන්දර 5:00 ත් අතර
        // (ජපානයේ නීතියට අනුව Night Rate වැටෙන්නේ රෑ 10 සිට පසුදා පාන්දර 5 වෙනකන් විතරයි)
        LocalDateTime nightStart = LocalDateTime.of(today, LocalTime.of(22, 0));
        LocalDateTime nightEnd = LocalDateTime.of(today.plusDays(1), LocalTime.of(5, 0));

        long nightMinutes = 0;

        // වැඩ කරපු කාලය ඇතුළත රෑ 10 සහ පාන්දර 5 අතර කාලය බැලීම
        LocalDateTime overlapStart = startDateTime.isAfter(nightStart) ? startDateTime : nightStart;
        LocalDateTime overlapEnd = endDateTime.isBefore(nightEnd) ? endDateTime : nightEnd;

        if (overlapStart.isBefore(overlapEnd)) {
            nightMinutes = Duration.between(overlapStart, overlapEnd).toMinutes();
        }

        // 💡 උපකල්පනය: බ්‍රේක් එක ගත්තේ රෑ 10න් පස්සේ නම් Night Minutes වලින් අඩු විය යුතුයි. 
        // නැත්නම් සාමාන්‍යයෙන් මුළු පැය ගණනින් බ්‍රේක් එක අඩු කරලා Night Hours වෙනම ගන්නවා නම්:
        double nightHours = nightMinutes / 60.0;
        
        // සාමාන්‍ය පැය ගණන (මුළු සැබෑ පැය ගණනින් Night Hours අඩු කිරීම)
        double normalHours = totalHours - nightHours;
        if (normalHours < 0) {
            // බ්‍රේක් එක නිසා normal hours සෘණ වුණොත්, ඒ කියන්නේ බ්‍රේක් එක ගෙන තියෙන්නේ රෑ 10න් පසු කාලයේදීයි
            nightHours += normalHours; 
            normalHours = 0;
        }

        // 4. මුළු වැටුප (රෑ 10න් පසු 1.25 ගුණයකින් වැඩි වේ)
        double totalSalary = (normalHours * hourlyWage) + (nightHours * hourlyWage * 1.25);

        return new ShiftResult(totalHours, nightHours, Math.round(totalSalary));
    }
}