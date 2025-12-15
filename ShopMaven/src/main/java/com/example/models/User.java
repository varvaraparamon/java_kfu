package com.example.models;


import lombok.*;

@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private Long id;
    private String name;
    private String surname;
    private String phone;
    private String email;
    private Integer age;
}
