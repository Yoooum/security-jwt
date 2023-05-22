package com.prprv.authorize.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

/**
 * @author Yoooum
 */
@Data
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @ManyToMany
    private List<Permission> permissions;
}
