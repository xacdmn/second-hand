package com.finalproject.secondhand.controller;

import com.finalproject.secondhand.entity.Offers;
import com.finalproject.secondhand.entity.Products;
import com.finalproject.secondhand.entity.Users;
import com.finalproject.secondhand.enums.EStatusProcess;
import com.finalproject.secondhand.response.WishlistResponse;
import com.finalproject.secondhand.service.product.ProductService;
import com.finalproject.secondhand.service.transaction.OfferService;
import com.finalproject.secondhand.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@Tag(name = "Offer", description = "API for processing Transaction")
@RequestMapping("/api/offer/")
@SecurityRequirement(name = "Authorization")
@CrossOrigin(origins = {"*"}, maxAge = 3600)
public class OfferController {

    @Autowired
    OfferService offerService;

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @Operation(summary = "Show by user")
    @PostMapping("show-offer")
    public ResponseEntity<WishlistResponse> showOffer(Authentication valid) {
        String username = valid.getName();
        Users validUser = userService.findByUsername(username);
        return new ResponseEntity<>(offerService.findByUser(validUser), HttpStatus.OK);
    }

    @Operation(summary = "Add offers")
    @PostMapping("add/{productId}")
    public ResponseEntity<?> saveOffer(@RequestParam (name = "priceNegotiated") String priceNegotiated,
                                       @PathVariable (name = "productId") Integer productId,
                                       Authentication valid) {
        String username = valid.getName();
        Users users = userService.findByUsername(username);
        Products products = productService.findProductById(productId);
        Offers offers = new Offers();

        offers.setUsers(users);
        offers.setProduct(products);
        offers.setPriceNegotiated(priceNegotiated);
        offers.setStatusProcess(offers.getStatusProcess());
        offerService.saveOffer(offers);
        return new ResponseEntity<>("Offers Added", HttpStatus.OK);
    }

    @Operation(summary = "Update status offers")
    @PutMapping("update/{offerId}/{status}")
    public ResponseEntity<?> updateStatusAccepted(@PathVariable ("offerId") Integer offerId,
                                                  @PathVariable ("status") String status) {
        Offers offers = offerService.findByOfferId(offerId);
        if (Objects.equals(status, "accepted")) {
            offers.setStatusProcess(EStatusProcess.ACCEPTED);
            offerService.updateStatusOffer(offers,offerId);
            return new ResponseEntity<>("Status Accepted", HttpStatus.ACCEPTED);
        } else if (Objects.equals(status, "rejected")) {
            offers.setStatusProcess(EStatusProcess.REJECTED);
            offerService.updateStatusOffer(offers,offerId);
            return new ResponseEntity<>("Status Rejected", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("Status not updated", HttpStatus.FORBIDDEN);
        }
    }
}
