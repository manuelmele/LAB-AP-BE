package core.wefix.lab.controller;

import core.wefix.lab.configuration.error.ErrorResponse;
import core.wefix.lab.entity.Account;
import core.wefix.lab.service.AccountService;
import core.wefix.lab.utils.object.request.*;
import core.wefix.lab.utils.object.response.*;
import core.wefix.lab.utils.object.staticvalues.Category;
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
        return accountService.getProfile(accountService.getCustomerOrWorkerInfo().getAccountId());
    }

    @GetMapping(path = "/worker-profile", produces = "application/json")
    @Operation(summary = "Allows consumer to get all worker data")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    Account getWorkerProfile(@Param("emailWorker") String emailWorker) {
        return accountService.getWorkerProfile(emailWorker);
    }

    @GetMapping(path = "/worker-reviews", produces = "application/json")
    @Operation(summary = "Allows consumer to get all worker reviews")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    List<GetReviewsResponse> getWorkerReviews(@Param("emailWorker") String emailWorker) {
        return accountService.getWorkerReviews(emailWorker);
    }

    @GetMapping(path = "/user-reviews", produces = "application/json")
    @Operation(summary = "Allows user/worker to get all user reviews")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    List<GetReviewsResponse> getCustomerReviews(@Param("emailCustomer") String emailCustomer) {
        return accountService.getCustomerReviews(emailCustomer);
    }

    @GetMapping(path = "/worker-avg-reviews", produces = "application/json")
    @Operation(summary = "Allows consumer to get avg of worker reviews")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    AvgReviewsResponse getWorkerAvgReviews(@Param("emailWorker") String emailWorker) {
        return accountService.getWorkerAvgReviews(emailWorker);
    }

    @GetMapping(path = "/worker-products", produces = "application/json")
    @Operation(summary = "Allows consumer to get all worker products")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    List<GetProductResponse> getWorkerProducts(@Param("emailWorker") String emailWorker) {
        return accountService.getWorkerProducts(emailWorker);
    }

    @GetMapping(path = "/worker-meetings", produces = "application/json")
    @Operation(summary = "Allows the worker to get all meetings")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    List<GetMeetingResponse> getWorkerMeetings(@Param("emailWorker") String emailWorker) {
        return accountService.getWorkerMeetings(emailWorker);
    }

    @GetMapping(path = "/customer-meetings", produces = "application/json")
    @Operation(summary = "Allows the customer to get all meetings")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    List<GetMeetingResponse> getCustomerMeetings(@Param("emailCustomer") String emailCustomer) {
        return accountService.getCustomerMeetings(emailCustomer);
    }

    @PutMapping(path = "/approve-meeting", produces = "application/json")
    @Operation(summary = "Allows the worker to accept/decline a meeting")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    void approveMeeting(@Param("idMeeting") Long idMeeting, @Param("accept") Boolean accept) {
        accountService.approveMeeting(idMeeting, accept);
    }

    @PutMapping(path = "/share-position", produces = "application/json")
    @Operation(summary = "Allows the worker to start/stop sharing their position")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    void sharePosition(@Param("idMeeting") Long idMeeting, @Param("start") Boolean start, @Param("latitude") Double latitude, @Param("longitude") Double longitude) {
        accountService.sharePosition(idMeeting, start, latitude, longitude);
    }

    @GetMapping(path = "/get-position", produces = "application/json")
    @Operation(summary = "Allows the user to get the worker meeting position")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    List<GetPositionResponse> getPosition(@Param("idMeeting") Long idMeeting) {
        return accountService.getPosition(idMeeting);
    }

    @PostMapping(path = "/add-meeting", produces = "application/json")
    @Operation(summary = "Allows to create a new meeting")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    void insertNewMeeting(@RequestBody InsertNewMeetingRequest newMeeting) {
        accountService.insertNewMeeting(newMeeting);
    }

    @PutMapping(path = "/complete/signup", produces = "application/json", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Allows user to complete his signup with bio and photo profile")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    void completeSignUp(@Param("bio") String bio, @Param("photoProfile") MultipartFile photoProfile) {
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

    @PutMapping(path = "/update-photo-profile", produces = "application/json", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Allows user to update his profile data")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    void updatePhotoProfile(@Param("photoProfile") MultipartFile photoProfile) {
        accountService.updatePhotoProfile(photoProfile);
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

    @GetMapping(path = "/workers/category", produces = "application/json")
    @Operation(summary = "Allows to get all workers about one category")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    List<GetProfileResponse> getWorkersOfCategory(@RequestParam("category") String category) {
        return accountService.getWorkersOfCategory(category);
    }

    @GetMapping(path = "/workers-filter", produces = "application/json")
    @Operation(summary = "Allows to get list of workers filtered by first name, second name, email, or word contains in bio")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    List<GetProfileResponse> getWorkersByName(@RequestParam(required = false, value = "value") String value, @RequestParam("category") String category) {
        return accountService.getWorkersByFirstNameOrSecondNameOrEmailOrBio(value, category);
    }

    @GetMapping(path = "/reviews", produces = "application/json")
    @Operation(summary = "Allows to get all his reviews")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    List<GetReviewsResponse> getReviews() {
        return accountService.getReviews();
    }

    @GetMapping(path = "/currencies", produces = "application/json")
    @Operation(summary = "Allows to get all paypal currencies")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    GetPayPalCurrencyResponse getPayPalCurrencies() {
        return new GetPayPalCurrencyResponse(List.of(CurrencyPayPal.values()));
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
        return accountService.paymentSuccess(paymentId, payerId);
    }

    @PostMapping(path = "/upgrade-pro", produces = "application/json")
    @Operation(summary = "Allows user to update his profile from customer to worker")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    String updateProfile(@RequestBody UpdateProRequest updateProRequest) {
        return accountService.updatePro(updateProRequest);
    }

    @PostMapping(path = "/add-review", produces = "application/json")
    @Operation(summary = "Allows user to add review for worker, it allows worker to add review for user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    GetReviewsResponse addReview(@RequestBody AddReviewRequest addReviewRequest) {
        return accountService.addReview(addReviewRequest);
    }

}
