package com.adarsh.wishlist.repository;

import org.adarsh.entities.UserWishlist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@RequestMapping
public interface WishListRepository extends MongoRepository<UserWishlist,Long> {

    Optional<UserWishlist> findByUserId(String userId);
}
