package com.travel.bookingservice.dto;

public class HotelDTO {
    private Long id;
    private String name;
    private String location;
    private Double pricePerNight;
    private Integer availableRooms;

    public HotelDTO() {}

    public HotelDTO(Long id, String name, String location, Double pricePerNight, Integer availableRooms) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.pricePerNight = pricePerNight;
        this.availableRooms = availableRooms;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Double getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(Double pricePerNight) { this.pricePerNight = pricePerNight; }
    public Integer getAvailableRooms() { return availableRooms; }
    public void setAvailableRooms(Integer availableRooms) { this.availableRooms = availableRooms; }
}
