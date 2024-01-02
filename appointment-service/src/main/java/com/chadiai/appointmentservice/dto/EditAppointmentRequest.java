package com.chadiai.appointmentservice.dto;

import com.chadiai.appointmentservice.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditAppointmentRequest {
    private String title;
    private int initiatorId;
    private int responderId;
    private Date date;
    private Status status;
    private String location;
}