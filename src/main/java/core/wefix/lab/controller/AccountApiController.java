package core.wefix.lab.controller;

import core.wefix.lab.configuration.error.ErrorResponse;
import core.wefix.lab.service.AccountService;
import core.wefix.lab.utils.object.request.UpdateProfileRequest;
import core.wefix.lab.utils.object.response.GetProfileResponse;
import core.wefix.lab.utils.object.response.GetWorkersCategoriesResponse;
import core.wefix.lab.utils.object.response.JWTResponse;
import core.wefix.lab.utils.object.staticvalues.Category;
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

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("wefix/account")
@SecurityRequirement(name = "JWT_Customer")
@PreAuthorize("hasAnyAuthority('Customer','Worker')")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Tag(name = "User", description = "User API")
public class AccountApiController {

    private final AccountService accountService;

    @GetMapping(path = "/profile", produces = "application/json")
    @Operation(summary = "Allows user to get all his data")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    GetProfileResponse getProfile() {
        return accountService.getProfile();
    }

    @PutMapping(path = "/complete/signup", produces = "application/json", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Allows user to complete his signup with bio and photo profile")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    void completeSignUp(@Param("bio") String bio, @Param("photoProfile") MultipartFile photoProfile) throws IOException {
        accountService.completeSignUp(bio, photoProfile);
    }

    @PostMapping(path = "/change-password", produces = "application/json")
    @Operation(summary = "Allows user to change password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    JWTResponse changePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword) {
        return accountService.changePassword(oldPassword,newPassword);
    }

    @PutMapping(path = "/update-profile", produces = "application/json")
    @Operation(summary = "Allows user to update his profile data")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    void updateProfile(@RequestBody UpdateProfileRequest updateProfileRequest) {
        accountService.updateProfile(updateProfileRequest);
    }

    @GetMapping(path = "/categories", produces = "application/json")
    @Operation(summary = "Allows to get all workers categories")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    GetWorkersCategoriesResponse getWorkersCategories() {
        return new GetWorkersCategoriesResponse(List.of(Category.values()));
    }

    @GetMapping(path = "/workers{category}", produces = "application/json")
    @Operation(summary = "Allows to get all workers about one category")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    List<GetProfileResponse> getWorkersOfCategory(@PathParam("category") String category) {
        return accountService.getWorkersOfCategory(category);
    }

    @GetMapping(path = "/workers-filter{value}", produces = "application/json")
    @Operation(summary = "Allows to get list of workers filtered by first name, second name and email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    List<GetProfileResponse> getWorkersByName(@PathParam("value") String value) {
        return accountService.getWorkersByFirstNameOrSecondNameOrEmail(value);
    }

}
