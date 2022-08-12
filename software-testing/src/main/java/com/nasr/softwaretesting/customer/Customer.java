package com.nasr.softwaretesting.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(name = "define_unique",columnNames = {Customer.PHONE_NUMBER}))
@Builder
public class Customer {

    public static final String PHONE_NUMBER="phone_number";

    @Id
   /* @GenericGenerator(name = "uuid2",strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "uuid2")*/
    @Column(length = 36,updatable = false)
    private String id;

//    @NotBlank
    @Column(nullable = false)
    private String name;

//    @NotBlank
    @Column(name = PHONE_NUMBER, nullable = false,unique = true)
    private String phoneNumber;

}
