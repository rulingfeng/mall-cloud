package com.rlf.module.entity;

import lombok.Data;

/**
 * @author RU
 * @date 2020/7/30
 * @Desc
 */
@Data
public class Car  {
    int id;
    int manufacturerId;
    String model;
    int year;
    float rating;


    public Car(int id, int manufacturerId, String model, int year) {
        this.id = id;
        this.manufacturerId = manufacturerId;
        this.model = model;
        this.year = year;
    }

}
