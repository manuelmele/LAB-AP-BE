package core.wefix.lab.controller;

import core.wefix.lab.configuration.error.ErrorResponse;
import core.wefix.lab.entity.Account;
import core.wefix.lab.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
	@Operation(summary = "Get user data")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Successful Operation"),
			@ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	Account getProfile() {
		return customerService.getUserInfo();
	}

	@PostMapping(path = "/change-password", produces = "application/json")
	@Operation(summary = "Allows user to change password")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Successful Operation"),
			@ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	String changePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword) {
   return customerService.changePassword(oldPassword,newPassword);
	}
}
