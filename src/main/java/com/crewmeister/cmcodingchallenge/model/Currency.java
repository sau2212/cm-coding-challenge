package com.crewmeister.cmcodingchallenge.model;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
@Data
@NoArgsConstructor
public class Currency {
    @Id
    private String code;
    private String name;

    public Currency(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
