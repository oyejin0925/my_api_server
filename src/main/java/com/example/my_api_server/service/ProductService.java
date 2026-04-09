package com.example.my_api_server.service;

import com.example.my_api_server.entity.Product;
import com.example.my_api_server.repo.ProductRepo;
import com.example.my_api_server.service.dto.ProductCreateDto;
import com.example.my_api_server.service.dto.ProductResponseDto;
import com.example.my_api_server.service.dto.ProductUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProductService {

    private final ProductRepo productRepo;

    //상품 등록
    //JPA 하이버네이트는 DB와 통신하기 위해, DB의 ACID가 되기위해서는 begin tran; commit이 반드시 되어야함
    @Transactional
    public ProductResponseDto createProduct(ProductCreateDto dto) {
        Product product = Product.builder()
                .productName(dto.getProductName())
                .productType(dto.getProductType())
                .productNumber(dto.getProductNumber())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .build();

        //save() 안에 @Transactional 존재 -> 위에 선언안해도 동작
        //BUT!!! 선언해야함, 상위에 선언해줘야 전체 부분이 트랜잭션 내 포함됨
        //선언없을 시 save 하나만 트랜잭션
        Product savedProduct = productRepo.save(product); //영속화

        //Entity -> Dto
        ProductResponseDto resDto = ProductResponseDto.builder()
                .productNumber(savedProduct.getProductNumber())
                .stock(savedProduct.getStock())
                .price(savedProduct.getPrice())
                .build();

        return resDto;
    }

    //상품 조회(without @Transactional)
    public ProductResponseDto findProduct(Long productId) {
        //DB에서조회한 것을 바탕으로 조회하여 영속성 컨텍스트에 저장하고(1차 캐시) 해당 값을 return
        Product product = productRepo.findById(productId).orElseThrow();

        //Entity -> Dto
        ProductResponseDto resDto = ProductResponseDto.builder()
                .productNumber(product.getProductNumber())
                .stock(product.getStock())
                .price(product.getPrice())
                .build();

        return resDto;
    }

    //상품 수정 : Transactional 이 없으면 DB반영이 안된다! (더티체킹이 일어나지 않아서)
    @Transactional
    public ProductResponseDto updateProduct(ProductUpdateDto dto) {
        //1. 조회
        Product product = productRepo.findById(dto.productId()).orElseThrow(); //select query X

        //2. 필요한 것만(상품명, 재고수량) 수정
        product.changeProductName(dto.changeProductName());
        product.increaseStock(dto.changeStock()); //수정 시 더한다고 가정

        //3. 리턴
        //Entity -> Dto
        ProductResponseDto resDto = ProductResponseDto.builder()
                .productNumber(product.getProductNumber())
                .stock(product.getStock())
                .price(product.getPrice())
                .build();

        return resDto;
    }

}
