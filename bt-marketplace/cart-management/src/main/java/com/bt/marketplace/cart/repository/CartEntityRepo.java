package com.bt.marketplace.cart.repository;

import com.bt.marketplace.cart.entity.CartEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the CMSApplicationEntity entity.
 */
@Repository
public interface CartEntityRepo extends MongoRepository<CartEntity, String> {}
