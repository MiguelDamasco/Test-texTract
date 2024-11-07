package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class userDTO {
    
    private String nombre;

    private String apellido;

    private String nacionalidad;

    private String fechaNacimiento;

    private String lugarDeNacimiento;

    private String numeroIdentidad;

    private String fechaExpedicion;

    private String fechaVencimiento;


}
