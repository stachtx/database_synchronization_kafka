package com.database.integration.external.kafka.consumer;

import com.database.integration.core.model.Department;
import com.database.integration.core.model.products.Product;
import com.database.integration.core.model.products.ProductType;
import com.database.integration.core.model.users.User;
import com.database.integration.external.services.DepartmentService;
import com.database.integration.external.services.ProductService;
import com.database.integration.external.services.ProductTypeService;
import com.database.integration.external.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaReceiver.class);

    @KafkaListener(id = "external-user-receiver", topics = "external-topic-user", containerFactory = "userKafkaListenerContainerFactory")
    public void receive(User user) {
        userService.mergeUser(user);
        LOGGER.info("Received user: " + user.toString());
    }

    @KafkaListener(id = "external-product-receiver", topics = "external-topic-product", containerFactory = "productKafkaListenerContainerFactory")
    public void receive(Product product) {
        productService.mergeProduct(product);
        log.info("Received product: " + product.toString());
    }

    @KafkaListener(id = "external-product-type-receiver", topics = "external-topic-product-type", containerFactory = "productTypeKafkaListenerContainerFactory")
    public void receive(ProductType productType) {
        productTypeService.mergeProductType(productType);
        log.info("Received product type: " + productType.toString());
    }

    @KafkaListener(id = "external-department-receiver", topics = "external-topic-department", containerFactory = "departmentKafkaListenerContainerFactory")
    public void receive(Department department) {
        departmentService.mergeDepartment(department);
        log.info("Received department: " + department.toString());
    }
}
