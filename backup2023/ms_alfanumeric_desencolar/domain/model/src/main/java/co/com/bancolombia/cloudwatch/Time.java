package co.com.bancolombia.cloudwatch;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Time {
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;
    private String timezone;



}
