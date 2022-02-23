package core.wefix.lab.service;

import core.wefix.lab.entity.Account;
import core.wefix.lab.entity.Product;
import core.wefix.lab.entity.Review;
import core.wefix.lab.repository.AccountRepository;
import core.wefix.lab.repository.ProductRepository;
import core.wefix.lab.repository.ReviewRepository;
import core.wefix.lab.service.jwt.JWTService;
import core.wefix.lab.utils.object.request.InsertNewProductRequest;
import core.wefix.lab.utils.object.response.AvgReviewsResponse;
import core.wefix.lab.utils.object.response.GetProductResponse;
import core.wefix.lab.utils.object.response.GetReviewsResponse;
import core.wefix.lab.utils.object.staticvalues.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static core.wefix.lab.utils.object.Regex.emailRegex;
import static core.wefix.lab.utils.object.staticvalues.StaticObject.photoProfileBase;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class WorkerService {
    private final AccountRepository accountRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;


    /**
     * Allows retrieving of all worker data from his authentication
     * @return an Account: all information about worker logged
     */
    public Account getWorkerInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            User user = ((User) authentication.getPrincipal());
            if (!user.getAuthorities().contains(new SimpleGrantedAuthority(Role.Worker.name()))) {
                throw new JWTService.TokenVerificationException();
            }
            return accountRepository.findByUserRoleAndEmail(Role.Worker, user.getUsername())
                    .orElseThrow(JWTService.TokenVerificationException::new);
        }
        throw new JWTService.TokenVerificationException();
    }

    /**
     * Allows workers to insert a new product to sell in his gallery
     * @param imageGallery: the image that worker wants to be set for a product
     * @param newProduct: json data retrieved from body to complete request
     */
    public void insertNewProduct(MultipartFile imageGallery, InsertNewProductRequest newProduct) throws IOException {
        Account account = getWorkerInfo();
        Product product;
        // newProduct validate
        if (!InsertNewProductRequest.validateInsertNewProductRequestJsonFields(newProduct))
            throw new IllegalArgumentException("Invalid json body");
        if (imageGallery == null){
            product = new Product(
                    account.getAccountId(),
                    photoProfileBase,
                    newProduct.getPrice(),
                    newProduct.getDescription(),
                    newProduct.getTitle());
        }else {
            try {
                product = new Product(
                            account.getAccountId(),
                            imageGallery.getBytes(),
                            newProduct.getPrice(),
                            newProduct.getDescription(),
                            newProduct.getTitle());
            } catch (IOException e) {
                throw new IllegalArgumentException("Invalid image");}
        }
        productRepository.save(product);
    }

    /**
     * Allows workers to retrieve all his products
     * @return a GetCustomerResponse: all information about products to send as response for a certain workers
     */
    public List<GetProductResponse> getProducts() {
        Account account = getWorkerInfo();
        List<Product> productsRetrieved = productRepository.findByUserIdAndDeletedProductFalse(account.getAccountId());
        List<GetProductResponse> getProductResponse = new ArrayList<>();
        for (Product product : productsRetrieved) {
            getProductResponse.add(new GetProductResponse(
                    product.getProductId(),
                    product.getProductImage(),
                    product.getPrice(),
                    product.getDescription(),
                    product.getTitle()));
        }
        return getProductResponse;
    }

    /**
     * Allows workers to insert a new product to sell in his gallery
     * @param imageGallery: the image that worker wants to be set for a product
     * @param updateProduct: json data retrieved from body to complete request
     * @param productId: the unique id of product that worker wants retrieve
     */
    public void updateProduct(MultipartFile imageGallery, InsertNewProductRequest updateProduct, Long productId) {
        Account account = getWorkerInfo();
        // updateProduct validate
        if (!InsertNewProductRequest.validateInsertNewProductRequestJsonFields(updateProduct))
            throw new IllegalArgumentException("Invalid json body");
        Product productRetrieved = productRepository.findByProductId(productId);
        if(productRetrieved == null)
            throw new IllegalArgumentException("Invalid productId");
        if(!productRetrieved.getUserId().equals(account.getAccountId()))
            throw new JWTService.TokenVerificationException("Worker not authorized to update current productId");
        try {
            if(imageGallery!=null)
                productRetrieved.setProductImage(imageGallery.getBytes());
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid image");}
        if(updateProduct.getDescription() != null)
            productRetrieved.setDescription(updateProduct.getDescription());
        if(updateProduct.getPrice() != null)
            productRetrieved.setPrice(updateProduct.getPrice());
        if(updateProduct.getTitle() != null)
            productRetrieved.setTitle(updateProduct.getTitle());
        productRepository.save(productRetrieved);
    }

    /**
     * Allows workers to delete a product from his gallery
     * @param productId: the unique id of product that worker wants retrieve
     */
    public void deleteProduct(Long productId) {
        Account account = getWorkerInfo();
        Product productRetrieved = productRepository.findByProductId(productId);
        if(productRetrieved == null)
            throw new IllegalArgumentException("Invalid productId");
        if(!productRetrieved.getUserId().equals(account.getAccountId()))
            throw new JWTService.TokenVerificationException("Worker not authorized to delete current productId");
        productRetrieved.setDeletedProduct(Boolean.TRUE);
        productRepository.save(productRetrieved);
    }

    public AvgReviewsResponse getCustomerAvgReviews(String emailCustomer) {
        getWorkerInfo();
        // emailCustomer validate
        if (!emailCustomer.matches(emailRegex))
            throw new IllegalArgumentException("Invalid emailCustomer");
        Account account = accountRepository.findByEmailAndUserRole(emailCustomer, Role.Customer);
        Double avgStar;
        if (account != null)
            avgStar = reviewRepository.avgStar(account.getAccountId());
        else
            throw new IllegalArgumentException("Invalid emailCustomer");

        return new AvgReviewsResponse(avgStar);
    }

}
