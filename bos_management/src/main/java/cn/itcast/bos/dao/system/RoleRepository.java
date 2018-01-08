package cn.itcast.bos.dao.system;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.bos.domain.system.Role;

public interface RoleRepository extends JpaRepository<Role,Integer> {

}
