package com.huacai.web.dao.privilege;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface AdminMyDao {
	void insertAdmin(Map<String, Object> admin);

	Map<String, Object> getAdminById(@Param("adminId") int adminId);
}
