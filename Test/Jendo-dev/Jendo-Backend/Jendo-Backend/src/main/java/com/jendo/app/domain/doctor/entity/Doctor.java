package com.jendo.app.domain.doctor.entity;

import com.jendo.app.domain.appointment.entity.Appointment;
import com.jendo.app.domain.consultationfee.entity.ConsultationFee;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doctors")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"appointments", "consultationFees", "availableSlots"})
@ToString(exclude = {"appointments", "consultationFees", "availableSlots"})
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "specialty", length = 100)
    private String specialty;

    @Column(name = "hospital", length = 200)
    private String hospital;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "qualifications")
    private String qualifications;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "address")
    private String address;

    @Column(name = "is_available")
    private Boolean isAvailable;

    @Column(name = "available_days")
    private String availableDays;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ConsultationFee> consultationFees = new ArrayList<>();

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DoctorAvailableSlot> availableSlots = new ArrayList<>();
}
