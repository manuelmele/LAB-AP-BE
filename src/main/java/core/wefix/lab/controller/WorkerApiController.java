package core.wefix.lab.controller;

import core.wefix.lab.configuration.error.ErrorResponse;
import core.wefix.lab.service.WorkerService;
import core.wefix.lab.utils.object.response.GetWorkerResponse;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("wefix/worker")
@SecurityRequirement(name = "JWT_Worker")
@PreAuthorize("hasAnyAuthority('Worker')")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Tag(name = "Worker", description = "Worker API")
public class WorkerApiController {
    private final WorkerService workerService;

    @GetMapping(path = "/profile", produces = "application/json")
    @Operation(summary = "Allows worker to get all his data")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetWorkerResponse.class))),
            @ApiResponse(responseCode = "400", description = "Operation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authentication Failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    GetWorkerResponse getProfile() {
        return workerService.getProfile();
    }
}
