package core.wefix.lab.utils.object.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import core.wefix.lab.utils.object.staticvalues.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetWorkersCategoriesResponse {
	private List<Category> workersCategories;
}
