package com.example.menu.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "cafe_menus")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;        // 메뉴 이름
    private String category;    // 메뉴 종류 (커피, 디저트 등)
    private String description; // 메뉴 설명
    private int price;          // 가격 (원)
}
