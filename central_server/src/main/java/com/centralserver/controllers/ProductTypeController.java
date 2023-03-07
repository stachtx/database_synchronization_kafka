package com.centralserver.controllers;

import com.centralserver.dto.ProductTypeDto;
import com.centralserver.exception.base.SystemBaseException;
import com.centralserver.model.products.ProductType;
import com.centralserver.services.impl.ProductTypeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/secured/products/type")
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = SystemBaseException.class)
public class ProductTypeController {

    @Autowired
    private ProductTypeServiceImpl productTypeService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    ProductType get(@PathVariable UUID id) throws SystemBaseException {
        return productTypeService.getProductTypeById(id);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    List<ProductType> getAll() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return productTypeService.getAllProductTypes();
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public void create(@RequestBody ProductTypeDto productTypeDto) throws SystemBaseException {
        productTypeService.createNewDeviceModel(productTypeDto);
    }

    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public void update(@RequestBody ProductTypeDto productTypeDto) throws SystemBaseException {
        productTypeService.updateProductType(productTypeDto);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable UUID id) throws SystemBaseException {
        productTypeService.deleteProductTypeById(id);
    }
}
