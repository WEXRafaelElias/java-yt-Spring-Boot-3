package com.example.springdemo.controllers;

import com.example.springdemo.dto.ProductDto;
import com.example.springdemo.models.ProductModel;
import com.example.springdemo.repository.interfaces.IProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class ProductController {

    @Autowired
    IProductRepository productRepository;

    @PostMapping("/products")
    public ResponseEntity<ProductModel> post(@RequestBody @Valid ProductDto productRecord) {

        var productModel = new ProductModel();

        BeanUtils.copyProperties(productRecord, productModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductModel>> getAll() {

        return ResponseEntity.status(HttpStatus.OK).body(productRepository.findAll());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Object> get(@PathVariable("id") UUID id) {

        Optional<ProductModel> product = productRepository.findById(id);

        return product.<ResponseEntity<Object>>map(productModel -> ResponseEntity.status(HttpStatus.OK).body(productModel)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found"));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Object> put(@PathVariable("id") UUID id, @RequestBody @Valid ProductDto productDto) {

        Optional<ProductModel> product = productRepository.findById(id);

        if(product.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Product with id %s not found", id));

        var productToBeModified = product.get();

        BeanUtils.copyProperties(productDto, productToBeModified);

        return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(productToBeModified));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") UUID id) {

        Optional<ProductModel> product = productRepository.findById(id);

        if(product.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Product with id %s not found", id));

        productRepository.delete(product.get());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(String.format("Product with id %s deleted successfully", id));
    }
}
