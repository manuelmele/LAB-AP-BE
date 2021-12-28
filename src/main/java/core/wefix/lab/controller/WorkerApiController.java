package core.wefix.lab.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
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

}
