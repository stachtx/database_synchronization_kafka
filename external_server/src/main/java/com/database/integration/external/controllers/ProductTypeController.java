package com.database.integration.external.controllers;

import com.database.integration.core.dto.ProductTypeDto;
import com.database.integration.core.exception.base.SystemBaseException;
import com.database.integration.core.model.ProductType;
import com.database.integration.external.services.impl.ProductTypeServiceImpl;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@RequestMapping("/secured/products/type")
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = SystemBaseException.class)
public class ProductTypeController {

  @Autowired
  private ProductTypeServiceImpl productTypeService;

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public @ResponseBody
  ProductType get(@PathVariable UUID id) throws SystemBaseException {
    return productTypeService.getProductTypeById(id);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public @ResponseBody
  List<ProductType> getAll() {
    return productTypeService.getAllProductTypes();
  }

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public void create(@RequestBody ProductTypeDto productTypeDto) throws SystemBaseException {
    productTypeService.createProductType(productTypeDto);
  }

  @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public void update(@RequestBody ProductTypeDto productTypeDto) throws SystemBaseException {
    productTypeService.updateProductType(productTypeDto);
  }

  @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public void delete(@PathVariable UUID id) throws SystemBaseException {
    productTypeService.deleteProductTypeById(id);
  }
}
