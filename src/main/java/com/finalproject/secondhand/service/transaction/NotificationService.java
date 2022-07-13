package com.finalproject.secondhand.service.transaction;

import com.finalproject.secondhand.entity.Notification;
import com.finalproject.secondhand.entity.Offers;
import com.finalproject.secondhand.entity.Products;
import com.finalproject.secondhand.entity.Users;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface NotificationService {

    void saveNotification(String title, String info, Offers offersId, Products productsId, Users usersId,
                          Boolean isRead);
    void saveNotification(String title, Products productsId, String username);

    void updateIsRead(Integer id);

    List<Notification> getNotification(Integer userId);


}