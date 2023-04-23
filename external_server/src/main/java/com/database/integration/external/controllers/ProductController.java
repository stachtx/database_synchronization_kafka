package com.database.integration.external.controllers;

import com.database.integration.core.dto.ProductDto;
import com.database.integration.core.exception.base.SystemBaseException;
import com.database.integration.core.model.Product;
import com.database.integration.external.services.ProductService;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/secured/products")
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = SystemBaseException.class)
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    Product getBySerialNumber(@PathVariable UUID id) throws SystemBaseException {
        return productService.getProduct(id);
    }

  @GetMapping(value = "/serial-number/{serialNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    Product getBySerialNumber(@PathVariable String serialNumber) throws SystemBaseException {
        return productService.getProductBySerialNumber(serialNumber);
    }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    List<Product> getAll() {
        return productService.getAllProducts();
    }

  @GetMapping(value = "/department/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    List<Product> getAllProductsForDepartment(@PathVariable UUID id) {
        return productService.getAllProductsForDepartment(id);
    }

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> create(@RequestBody ProductDto productDto) throws SystemBaseException {
    productService.createProduct(productDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

  @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
    public void update(@RequestBody ProductDto productDto) throws SystemBaseException {
        productService.updateProduct(productDto);
    }

  @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable UUID id) throws SystemBaseException {
        productService.deleteProductById(id);
    }
}
