package com.finalproject.secondhand.response;

import com.finalproject.secondhand.entity.Offers;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class OfferResponse {

    Integer offerId;
    String ImageProfil;
    String fullname;
    String city;
    String Image1;
    String productName;
    String price;
    String priceNegotiated;
    String statusProcess;

    public OfferResponse() {}

    public OfferResponse(Offers offers){
        this.offerId=offers.getOfferId();
        this.ImageProfil=offers.getProduct().getUsers().getImageProfil();
        this.fullname=offers.getProduct().getUsers().getFullname();
        this.city=offers.getProduct().getUsers().getCity();
        this.Image1=offers.getProduct().getImage1();
        this.productName=offers.getProduct().getProductName();
        this.price=offers.getProduct().getPrice();
        this.priceNegotiated=offers.getPriceNegotiated();
        this.statusProcess=offers.getStatusProcess().name();
    }

}
