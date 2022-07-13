package com.finalproject.secondhand.response;

import com.finalproject.secondhand.entity.Offers;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class OfferResponse {

    Integer offerId;
    String ImageProfil;
    String fullname;
    String city;
    String Image1;
    String Image2;
    String Image3;
    String Image4;
    String productName;
    String price;
    String priceNegotiated;
    String statusProcess;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    public OfferResponse(Offers offers){
        this.offerId=offers.getOfferId();
        this.ImageProfil=offers.getUsers().getImageProfil();
        this.fullname=offers.getUsers().getFullname();
        this.city=offers.getUsers().getCity();
        this.Image1=offers.getProduct().getImage1();
        this.Image2=offers.getProduct().getImage2();
        this.Image3=offers.getProduct().getImage3();
        this.Image4=offers.getProduct().getImage4();
        this.productName=offers.getProduct().getProductName();
        this.price=offers.getProduct().getPrice();
        this.priceNegotiated=offers.getPriceNegotiated();
        this.statusProcess=offers.getStatusProcess().name();
        this.createdAt=offers.getCreatedAt();
        this.updatedAt=offers.getUpdatedAt();
    }

}
