package rw.dsacco.clat.services;

import rw.dsacco.clat.models.Products;
import rw.dsacco.clat.repositories.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductsService {

    @Autowired
    private ProductsRepository productsRepository;

    public Products createProduct(Products product) {
        return productsRepository.save(product);
    }


    public List<Products> getAllProducts() {
        return productsRepository.findAll();
    }

    public Optional<Products> getProductById(Long id) {
        return productsRepository.findById(id);
    }

    public Optional<Products> getProductByCode(String code) {
        return productsRepository.findByCode(code);
    }

    public Products updateProduct(Products product) {
        return productsRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productsRepository.deleteById(id);
    }
}
