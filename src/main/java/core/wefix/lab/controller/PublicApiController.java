package core.wefix.lab.controller;

import core.wefix.lab.configuration.error.ErrorResponse;
import core.wefix.lab.service.PublicService;
import core.wefix.lab.utils.object.request.RegisterRequest;
import core.wefix.lab.utils.object.response.JWTResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
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
	@Operation(summary = "Allows user to register as customer")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = JWTResponse.class))),
			@ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "409", description = "Conflict", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
	})
	public JWTResponse signUp(@RequestBody RegisterRequest data) {
		return publicService.signUp(data);
	}

	@PostMapping(path = "/login")
	@Operation(summary = "Allows customer or worker to log in")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = JWTResponse.class))),
			@ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
	})
	public JWTResponse login(@RequestParam("email") String email, @RequestParam("password") String password) {
		return publicService.login(email, DigestUtils.sha3_256Hex(password));
	}

	@PostMapping(path = "/reset")
	@Operation(summary = "Allows customer or worker to reset password")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Successful Operation"),
			@ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
	})
	public void resetPassword(@RequestParam("email") String email) {
			publicService.resetPassword(email);
	}

}
