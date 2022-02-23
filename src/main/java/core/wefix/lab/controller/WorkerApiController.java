package core.wefix.lab.controller;

import core.wefix.lab.configuration.error.ErrorResponse;
import core.wefix.lab.service.WorkerService;
import core.wefix.lab.utils.object.request.InsertNewProductRequest;
import core.wefix.lab.utils.object.response.AvgReviewsResponse;
import core.wefix.lab.utils.object.response.GetPaymentResponse;
import core.wefix.lab.utils.object.response.GetProductResponse;
import core.wefix.lab.utils.object.staticvalues.CurrencyPayPal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("wefix/worker")
@SecurityRequirement(name = "JWT_Worker")
@PreAuthorize("hasAnyAuthority('Worker')")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Tag(name = "Worker", description = "Worker API")
public class WorkerApiController {
    private final WorkerService workerService;

    @PostMapping(path = "/insert-new-product/", produces = "application/json", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Allows workers to insert a new product to sell in his gallery")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    void insertNewProduct(@RequestParam(required = false, value = "image") MultipartFile imageGallery, InsertNewProductRequest newProduct) throws IOException {
        workerService.insertNewProduct(imageGallery, newProduct);
    }

    @GetMapping(path = "/all-product", produces = "application/json")
    @Operation(summary = "Allows workers to get all his products")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    List<GetProductResponse> getProducts() {
        return workerService.getProducts();
    }

    @PutMapping(path = "/update-product/{productId}", produces = "application/json", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Allows workers to update a product to sell in his gallery")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    void updateProduct(@PathVariable("productId") Long productId, @RequestParam(required = false, value ="image") MultipartFile imageGallery, InsertNewProductRequest updateProduct) {
        workerService.updateProduct(imageGallery, updateProduct, productId);
    }

    @PutMapping(path = "/delete-product/{productId}", produces = "application/json")
    @Operation(summary = "Allows workers to delete a product from his gallery")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    void deleteProduct(@PathVariable("productId") Long productId) {
        workerService.deleteProduct(productId);
    }

    @GetMapping(path = "/user-avg-reviews", produces = "application/json")
    @Operation(summary = "Allows worker to get avg of customer reviews")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    AvgReviewsResponse getCustomerAvgReviews(@Param("emailCustomer") String emailCustomer) {
        return workerService.getCustomerAvgReviews(emailCustomer);
    }

    @PostMapping(path="/payment", produces = "application/json")
    @Operation(summary = "Allows worker to pay plan price")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    String payFine(@RequestParam("price") Double price, @RequestParam("currency") CurrencyPayPal currency){
        return workerService.pay(price, currency);
    }

    @GetMapping(path="/payment-failed", value = "/payment-failed", produces = "application/text")
    @Operation(summary = "PayPal payment failed")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public String cancelFinePayment() { return "PayPal payment failed";}

    @GetMapping(path= "/payment-success", value = "/payment-success", produces = "application/text")
    @Operation(summary = "PayPal payment success")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    String successFinePayment(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        return workerService.paymentSuccess(paymentId, payerId);
    }

    @GetMapping(path = "/all-payments", produces = "application/json")
    @Operation(summary = "Allows worker to get all his payment")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    List<GetPaymentResponse> getPayments() {
        return workerService.getPayments();
    }

    @GetMapping(path = "/get-payment", produces = "application/json")
    @Operation(summary = "Allows worker to get all info about a certain payment")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    GetPaymentResponse getPaymentInfo(@Param("paymentId") Long paymentId) {
        return workerService.getPaymentInfo(paymentId);
    }

}
