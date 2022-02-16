package core.wefix.lab.repository;


import core.wefix.lab.entity.Account;
import core.wefix.lab.utils.object.staticvalues.Category;
import core.wefix.lab.utils.object.staticvalues.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

	Optional<Account> findByEmail(String email);

	Optional<Account> findByUserRoleAndEmail(Role userRole, String email);

	Optional<Account> findByEmailAndUserPassword(String email, String userPassword);

	Optional<Account> findByUserRoleAndEmailAndUserPassword(Role userRole, String email, String userPassword);

	Optional<Account> findByUserRoleAndEmailAndResetCode(Role userRole, String email, String resetCode);

	Optional<Account> findByEmailAndResetCode(String email, String resetCode);

	List<Account> findByUserCategoryAndUserRole(Category category, Role role);

	List<Account> findByFirstNameOrSecondNameOrEmail(String firstName, String secondName, String email);

}