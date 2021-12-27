package core.wefix.lab.controller;

import core.wefix.lab.configuration.error.ErrorResponse;
import core.wefix.lab.service.PublicService;
import core.wefix.lab.utils.object.request.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("wefix/public")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Tag(name = "Public", description = "Public API")
public class PublicApiController {

	private final PublicService publicService;

	@PostMapping(path = "/signup")
	@Operation(summary = "User Register as customer")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(mediaType = "text/plain")),
			@ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
	})
	public String signUp(@RequestBody RegisterRequest data) {
		return publicService.signUp(data);
	}

	@PostMapping(path = "/login/customer")
	@Operation(summary = "Customer Login")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(mediaType = "text/plain")),
			@ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
	})
	public String loginCustomer(@RequestParam("email") String username, @RequestParam("password") String password) {
		return publicService.loginCustomer(username, password);
	}

	@PostMapping(path = "/login/worker")
	@Operation(summary = "Worker Login")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(mediaType = "text/plain")),
			@ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
	})
	public String loginWorker(@RequestParam("email") String username, @RequestParam("password") String password) {
		return publicService.loginWorker(username, password);
	}

	@PostMapping(path = "/reset/user")
	@Operation(summary = "User Password Reset")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Successful Operation"),
			@ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
	})
	public void resetUser(
			@RequestParam("username") String username) {
		publicService.resetUser(username);
	}

	@PostMapping(path = "/reset/admin")
	@Operation(summary = "Admin Password Reset")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Successful Operation"),
			@ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
	})
	public void resetAdmin(
			@RequestParam("username") String username) {
			publicService.resetAdmin(username);
	}

}
