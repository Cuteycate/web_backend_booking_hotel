package com.example.hotel_booking_be_v1.service;

import com.example.hotel_booking_be_v1.model.Ward;

import java.util.List;

public interface IWardService {
    public List<Ward> findAllwards();

    public List<Ward> findwardsByDistrict(Long districtId);

    public List<Ward> searchwardsByName(String name);
}