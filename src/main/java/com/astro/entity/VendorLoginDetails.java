package com.astro.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name="vendor_login_details")
public class VendorLoginDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vendorId;
    private String emailAddress;
    private String password;
    private Boolean emailSent;

    private LocalDateTime createdDate = LocalDateTime.now();


}
