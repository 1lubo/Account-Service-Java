package account.group;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {

	Group findByCode(String code);

	Group save(Group group);
}
