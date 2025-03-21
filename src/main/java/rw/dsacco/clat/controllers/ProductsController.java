package rw.dsacco.clat.controllers;

import rw.dsacco.clat.dto.ApiResponse;
import rw.dsacco.clat.dto.ProductsDTO;
import rw.dsacco.clat.models.Products;
import rw.dsacco.clat.services.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200") // Allow frontend access
@RestController
@RequestMapping("/api/products")
public class ProductsController {

    @Autowired
    private ProductsService productsService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Products>> createProduct(@RequestBody ProductsDTO productsDTO) {
        Products product = new Products();
        product.setEnProduct(productsDTO.getEnProduct());
        product.setFrProduct(productsDTO.getFrProduct());
        product.setKnProduct(productsDTO.getKnProduct());
        product.setDescription(productsDTO.getDescription());

        Products savedProduct = productsService.createProduct(product);
        return ResponseEntity.ok(ApiResponse.success("Product created successfully", savedProduct));
    }


    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<Products>>> getAllProducts() {
        List<Products> products = productsService.getAllProducts();
        return ResponseEntity.ok(ApiResponse.success("Products retrieved successfully", products));
    }

    @GetMapping("/{code}")
    public ResponseEntity<ApiResponse<Products>> getProductByCode(@PathVariable String code) {
        Optional<Products> productOptional = productsService.getProductByCode(code);

        if (productOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND, "Product not found"));
        }

        return ResponseEntity.ok(ApiResponse.success("Product retrieved successfully", productOptional.get()));
    }

    @PutMapping("/{code}")
    public ResponseEntity<ApiResponse<Products>> updateProduct(@PathVariable String code, @RequestBody ProductsDTO productsDTO) {
        Optional<Products> productOptional = productsService.getProductByCode(code);

        if (productOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND, "Product not found"));
        }

        Products product = productOptional.get();
        product.setEnProduct(productsDTO.getEnProduct());
        product.setFrProduct(productsDTO.getFrProduct());
        product.setKnProduct(productsDTO.getKnProduct());
        product.setDescription(productsDTO.getDescription());

        Products updatedProduct = productsService.updateProduct(product);
        return ResponseEntity.ok(ApiResponse.success("Product updated successfully", updatedProduct));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable String code) {
        Optional<Products> productOptional = productsService.getProductByCode(code);

        if (productOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND, "Product not found"));
        }

        productsService.deleteProduct(productOptional.get().getId());
        return ResponseEntity.ok(ApiResponse.success("Product deleted successfully", null));
    }

}
