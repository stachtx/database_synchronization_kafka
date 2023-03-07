package com.database.integration.central.kafka.consumer;


import com.database.integration.central.services.DepartmentService;
import com.database.integration.central.services.ProductService;
import com.database.integration.central.services.ProductTypeService;
import com.database.integration.central.services.UserService;
import com.database.integration.core.model.Department;
import com.database.integration.core.model.products.Product;
import com.database.integration.core.model.products.ProductType;
import com.database.integration.core.model.users.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
public class KafkaReceiver {

  @Autowired
  private UserService userService;
  @Autowired
  private ProductService productService;
  @Autowired
  private ProductTypeService productTypeService;
  @Autowired
  private DepartmentService departmentService;

  @KafkaListener(id = "central-user-receiver", topics = "central-topic-user", containerFactory = "userKafkaListenerContainerFactory")
  public void receive(User user) {
    userService.mergeUser(user);
    log.info("Received user: " + user.toString());
  }

  @KafkaListener(id = "central-product-receiver", topics = "central-topic-product", containerFactory = "productKafkaListenerContainerFactory")
  public void receive(Product product) {
    productService.mergeProduct(product);
    log.info("Received product: " + product.toString());
  }

  @KafkaListener(id = "central-product-type-receiver", topics = "central-topic-product-type", containerFactory = "productTypeKafkaListenerContainerFactory")
  public void receive(ProductType productType) {
    productTypeService.mergeProductType(productType);
    log.info("Received product type: " + productType.toString());
  }

  @KafkaListener(id = "central-department-receiver", topics = "central-topic-department", containerFactory = "departmentKafkaListenerContainerFactory")
  public void receive(Department department) {
    departmentService.mergeDepartment(department);
    log.info("Received department: " + department.toString());
  }
}
