package com.centralserver.controllers;

import com.centralserver.dto.ProductDto;
import com.centralserver.exception.base.SystemBaseException;
import com.centralserver.model.products.Product;
import com.centralserver.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/secured/products")
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = SystemBaseException.class)
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    Product getBySerialNumber(@PathVariable UUID id) throws SystemBaseException {
        return productService.getProduct(id);
    }

    @RequestMapping(value = "/serial-number/{serialNumber}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    Product getBySerialNumber(@PathVariable String serialNumber) throws SystemBaseException {
        return productService.getProductBySerialNumber(serialNumber);
    }


    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    List<Product> getAll() {
        return productService.getAllProducts();
    }

    @RequestMapping(value = "/department/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    List<Product> getAllProductsForDepartment(@PathVariable UUID id) {
        return productService.getAllProductsForDepartment(id);
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> create(@RequestBody ProductDto productDto) throws SystemBaseException {
        productService.createNewProduct(productDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public void update(@RequestBody ProductDto productDto) throws SystemBaseException {
        productService.updateProduct(productDto);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable UUID id) throws SystemBaseException {
        productService.deleteProductById(id);
    }
}
