package com.huacai.web.dao.task;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface TaskMyDao {

	void insertlog(Map<String, Object> log);

	void insertTask(Map<String, Object> task);

	void insertTaskDetail(Map<String, Object> task);

	void updateTask(Map<String, Object> task);
	
	void updateTaskDetailRunStatus(Map<String, Object> param);

	void updateTaskM5(Map<String, Object> map);

	void delTask(int task_id);

	void delTaskDetail(int task_id);

	void stopTask(Map<String, Object> parm);

	void clearTask(int task_id);

	void runTask(Map<String, Object> parm);

	void copyTask(Map<String, Object> task);

	void copyTaskDetail(Map<String, Object> task);

	List<Map<String, Object>> getTaskList(Map<String, Object> req);

	List<Map<String, Object>> getTemplateList();
	
	List<Map<String, Object>> getLogList(@Param(value = "task_id") int task_id, @Param(value = "lastid") String lastid);

	String getTemplateEmails(@Param(value = "privs") String privs);
	
	String getModuleFilePathById(@Param(value = "task_id") int task_id, @Param(value = "module") String module);

	Map<String, Object> getTemplateById(int template_id);

	void updateTemplate(Map<String, Object> parm);

	Map<String, Object> getTaskById(int task_id);

	int getTaskListCount(Map<String, Object> req);
	
	int getTaskCountByName(@Param(value = "task_name") String task_name);

	List<Map<String, Object>> getHistoryFile(Map<String, Object> req);

	int getHistoryFileCount(Map<String, Object> req);

	void updateTask4upfile(Map<String, Object> req);
}
