package com.adarsh.wishlist.repository;

import com.adarsh.wishlist.entities.UserWishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishListRepository extends JpaRepository<UserWishlist,String> {

}
