package core.wefix.lab.controller;

import core.wefix.lab.configuration.error.ErrorResponse;
import core.wefix.lab.service.CustomerService;
import core.wefix.lab.utils.object.response.GetCustomerResponse;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("wefix/customer")
@SecurityRequirement(name = "JWT_Customer")
@PreAuthorize("hasAnyAuthority('Customer')")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Tag(name = "Customer", description = "Customer API")
public class CustomerApiController {

    private final CustomerService customerService;

    @GetMapping(path = "/profile", produces = "application/json")
    @Operation(summary = "Allows customer to get all his data")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetCustomerResponse.class))),
            @ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    GetCustomerResponse getProfile() {
        return customerService.getProfile();
    }
/*
    @PutMapping(path = "/complete/signup/", produces = "application/json", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Allows customer to complete his signup")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<ErrorResponse> updateMV(@RequestParam("bio") String bio, @RequestParam("photoProfile") MultipartFile mvId) {
        return customerService.updateMV(vehicleId, mvId);
    }
 */
}
