package core.wefix.lab.controller;

import core.wefix.lab.configuration.error.ErrorResponse;
import core.wefix.lab.service.WorkerService;
import core.wefix.lab.utils.object.request.InsertNewProductRequest;
import core.wefix.lab.utils.object.response.GetProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    void insertNewProduct(@RequestParam("image") MultipartFile imageGallery, InsertNewProductRequest newProduct) {
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
    void updateProduct(@PathVariable("productId") Long productId, @RequestParam("image") MultipartFile imageGallery, InsertNewProductRequest updateProduct) {
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
}
