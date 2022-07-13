package com.finalproject.secondhand.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.secondhand.dto.product.AddProductDto;
import com.finalproject.secondhand.dto.product.UpdateProductDto;
import com.finalproject.secondhand.entity.Categories;
import com.finalproject.secondhand.entity.Products;
import com.finalproject.secondhand.entity.Users;
import com.finalproject.secondhand.response.ProductResponse;
import com.finalproject.secondhand.service.image.CloudinaryStorageService;
import com.finalproject.secondhand.service.product.CategoriesService;
import com.finalproject.secondhand.service.product.ProductService;
import com.finalproject.secondhand.service.transaction.NotificationService;
import com.finalproject.secondhand.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "Product", description = "API for processing CRUD Products")
@RequestMapping("/api/product/")
@SecurityRequirement(name = "Authorization")
@CrossOrigin(origins = {"*"}, allowedHeaders = "*")
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoriesService categoriesService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CloudinaryStorageService cloudinaryStorageService;

    @Operation(summary = "Validasi profil")
    @GetMapping("/sell")
    public ResponseEntity<?> validasiProfil(Authentication valid) {
        String username = valid.getName();
        return new ResponseEntity<>(productService.validasiProfil(username), HttpStatus.OK);
    }

    @Operation(summary = "Find product by productId")
    @GetMapping("{productId}")
    public ResponseEntity<ProductResponse> findProductById(@PathVariable ("productId") Integer productId) {
        return new ResponseEntity<>(productService.findByProductId(productId), HttpStatus.OK);
    }

    @Operation(summary = "Preview product")
    @PostMapping(value = "add/{isPublished}",
            consumes = {MediaType.APPLICATION_JSON_VALUE,
                    MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> saveProduct( @RequestPart (name = "addJson") String addJson,
                                          @RequestPart(name = "image") MultipartFile[] image,
                                          @PathVariable String isPublished, Authentication authentication) {
        String username = authentication.getName();
        if (productService.validasiProfil(username).equals(true)) {
            Products products = new Products();
            AddProductDto add = new AddProductDto();
            try {
                ObjectMapper om = new ObjectMapper();
                add = om.readValue(addJson, AddProductDto.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Categories categories = categoriesService.loadCategoryByCategoryId(add.getCategoryId());
            Users users = userService.findByUsername(username);
            products.setUsers(users);
            products.setProductName(add.getProductName());
            products.setCategories(categories);
            products.setPrice(add.getPrice());
            products.setDescription(add.getDescription());
            products.setIsSold(products.getIsSold());
            List<String> urlImage = new ArrayList<>();
            if (image == null) {
                LOGGER.info("skip upload...");
            } else {
                for (int i = 0; i < image.length; i++) {
                    Map<?, ?> uploadImage = (Map<?, ?>) cloudinaryStorageService.upload(image[i]).getData();
                    urlImage.add(i, uploadImage.get("url").toString());
                    if (urlImage.get(i) == null) {
                        LOGGER.info("skip upload...");
                    } else if (urlImage.get(0) != null) {
                        products.setImage1(urlImage.get(i));
                    } else if (urlImage.get(1) != null) {
                        products.setImage1(urlImage.get(i));
                    } else if (urlImage.get(2) != null) {
                        products.setImage1(urlImage.get(i));
                    } else if (urlImage.get(3) != null) {
                        products.setImage1(urlImage.get(i));
                    }
                }
            }
            if (isPublished.equals("preview")) {
                products.setIsPublished(products.getIsPublished());
            } else if (isPublished.equals("publish")) {
                notificationService.saveNotificationProduct("Produk berhasil diterbitkan", products, products.getUsers());
                products.setIsPublished(true);
            }
            return new ResponseEntity<>(productService.save(products), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
    }

    @Operation(summary = "Publish product")
    @PutMapping("update/publish/{productId})")
    public ResponseEntity<?> publishProduct(@PathVariable Integer productId) {
        Products products = new Products();
        products.setIsPublished(true);
        notificationService.saveNotificationProduct("Produk berhasil diterbitkan", products, products.getUsers());
        productService.publish(products, productId);
        return new ResponseEntity<>("Product published successfully", HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Edit product by productId")
    @PutMapping(value = "update/{productId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE,
                    MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updateProduct(@RequestPart(name = "updateJson", required = false) String updateJson,
                                           @RequestPart(name = "image", required = false) MultipartFile[] image,
                                           @PathVariable Integer productId) {
        Products products = new Products();
        UpdateProductDto update = new UpdateProductDto();
        try {
            ObjectMapper om = new ObjectMapper();
            update = om.readValue(updateJson, UpdateProductDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        products.setProductName(update.getProductName());
        products.setPrice(update.getPrice());
        products.setDescription(update.getDescription());
        List<String> urlImage = new ArrayList<>();
        if (image == null) {
            LOGGER.info("skip upload image");
        } else {
            for (int i = 0; i < image.length; i++) {
                Map<?, ?> uploadImage = (Map<?, ?>) cloudinaryStorageService.upload(image[i]).getData();
                urlImage.add(i, uploadImage.get("url").toString());
                if (urlImage.get(i) == null) {
                    LOGGER.info("skip upload...");
                } else {
                    if (urlImage.get(i) == null) {
                        LOGGER.info("skip upload...");
                    } else if (urlImage.get(0) != null) {
                        products.setImage1(urlImage.get(i));
                    } else if (urlImage.get(1) != null) {
                        products.setImage1(urlImage.get(i));
                    } else if (urlImage.get(2) != null) {
                        products.setImage1(urlImage.get(i));
                    } else if (urlImage.get(3) != null) {
                        products.setImage1(urlImage.get(i));
                    }
                }
            }
        }
        productService.update(products, productId);
        return new ResponseEntity<>("Product is updated", HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Delete product by productId")
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId) {
        return new ResponseEntity<>(productService.deleteProduct(productId), HttpStatus.OK);
    }
}
