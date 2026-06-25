package com.example.arubaito;

public class ShiftResult {
    private double totalHours;
    private double nightHours;
    private long totalSalary;

    public ShiftResult(double totalHours, double nightHours, long totalSalary) {
        this.totalHours = totalHours;
        this.nightHours = nightHours;
        this.totalSalary = totalSalary;
    }

    // Getters (VS Code එකට මේවා ඕන වෙනවා JSON හදන්න)
    public double getTotalHours() { return totalHours; }
    public double getNightHours() { return nightHours; }
    public long getTotalSalary() { return totalSalary; }
}