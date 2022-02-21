package core.wefix.lab.repository;


import core.wefix.lab.entity.Account;
import core.wefix.lab.utils.object.staticvalues.Category;
import core.wefix.lab.utils.object.staticvalues.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

	Account findByAccountId(Long accountId);

	Optional<Account> findByEmail(String email);

	Account findByEmailAndUserRole(String email, Role userRole);

	Optional<Account> findByUserRoleAndEmail(Role userRole, String email);

	Optional<Account> findByEmailAndUserPassword(String email, String userPassword);

	Optional<Account> findByUserRoleAndEmailAndUserPassword(Role userRole, String email, String userPassword);

	Optional<Account> findByUserRoleAndEmailAndResetCode(Role userRole, String email, String resetCode);

	Optional<Account> findByEmailAndResetCode(String email, String resetCode);

	List<Account> findByUserCategoryAndUserRole(Category category, Role role);

	@Query("SELECT a FROM Account a WHERE (a.userRole =:role AND a.userCategory =:category) AND (a.firstName =:firstName OR " +
			   "a.secondName =:secondName OR a.email =:email OR a.bio LIKE CONCAT('%',:bio,'%'))")
	List<Account> findByFirstNameOrSecondNameOrEmailOrBioAndUserCategoryAndUserRole(String firstName, String secondName, String email, String bio, Category category, Role role);

}