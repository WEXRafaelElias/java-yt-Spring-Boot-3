package com.example.springdemo.repository.interfaces;

import com.example.springdemo.models.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface IProductRepository extends JpaRepository<ProductModel, UUID> {

}
