package com.bt.marketplace.cart.mapper;

import com.bt.marketplace.cart.entity.CartEntity;
import com.bt.marketplace.cart.model.GetCartResponse;
import com.bt.marketplace.cart.model.GetCartVerificationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GetCartRequestEntityMapper {

  GetCartRequestEntityMapper INSTANCE = Mappers.getMapper(GetCartRequestEntityMapper.class);

  GetCartResponse entityToResponse(CartEntity cartEntity);

  GetCartVerificationResponse entityToVerificationResponse(CartEntity cartEntity);

}
