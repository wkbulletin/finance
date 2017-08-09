package com.huacai.web.controller.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huacai.util.DateUtil;
import com.huacai.util.PageUtil;
import com.huacai.web.annotation.TaskSendMailTemplate;
import com.huacai.web.controller.BaseController;
import com.huacai.web.dao.task.TaskMyDao;
import com.huacai.web.filters.InitConfig;

import libcore.util.PaginationContext;
import libcore.util.VarUtil;

@Controller
@RequestMapping("task")
@Scope("prototype")
public class TaskController<V> extends BaseController {
	public static final String Priv_Set = "privSet";

	public static String getSimpleFilename(String fullFilename) {
		return fullFilename.substring(fullFilename.lastIndexOf("/") + 1);
	}

	public static boolean checkFileIsUpload(Object... files) {
		if (files == null) {
			return false;
		}
		for (Object f : files) {
			if (f == null) {
				return false;
			}
			String fs = f.toString();
			if (StringUtils.isEmpty(fs)) {
				return false;
			}
		}
		return true;
	}

	public static boolean checkAuthorized(HashSet<String> privSet, String privName, boolean moduleStatus, boolean isOk) {
		if (!privSet.contains(privName)) {
			return false;
		}
		if (!moduleStatus) {
			return false;
		}
		return isOk;
	}

	public static final int MODULE_STATUS_READY = 0;
	public static final int MODULE_STATUS_RUNNING = 1;
	public static final int MODULE_STATUS_STOP = 2;
	public static final int MODULE_STATUS_ERROR = 3;
	public static final int MODULE_STATUS_DONE = 9;

	protected final static Logger logger = LogManager.getLogger(TaskController.class);
	@Autowired
	private TaskMyDao taskMyDao;

	@RequestMapping(value = "/list")
	public String list(String act, String begin_time, String end_time, String task_name, HashMap<String, Object> model) throws Exception {
		if (!"search".equals(act)) {
			Date now = new Date();
			if (StringUtils.isEmpty(begin_time))
				begin_time = DateUtil.getDay(-7, now);
			if (StringUtils.isEmpty(end_time))
				end_time = DateUtil.getDate(now, "yyyy-MM-dd");
		}
		PageHelper.startPage(PaginationContext.getPageNum(), PaginationContext.getPageSize());

		Map<String, Object> req = new HashMap<>();
		req.put("begin_time", begin_time);
		req.put("end_time", StringUtils.isEmpty(end_time) ? "" : end_time + " 23:59:59");
		req.put("task_name", task_name);

		List<Map<String, Object>> list = taskMyDao.getTaskList(req);
		PageInfo<Map<String, Object>> page = new PageInfo<>(list);
		model.putAll(req);
		model.put("end_time", end_time);
		model.put("pageStr", PageUtil.page2("", page.getPageNum(), page.getPageSize(), page.getTotal(), model));
		model.put("list", list);

		return "/task/task_list";
	}

	@RequestMapping("/template_list")
	public String tempate_list(HashMap<String, Object> model) {
		// PageHelper.startPage(PaginationContext.getPageNum(),
		// PaginationContext.getPageSize());
		List<Map<String, Object>> list = taskMyDao.getTemplateList();
		model.put("list", list);
		return "/task/template_list";
	}

	@RequestMapping("/viewlog")
	public String viewlog(HashMap<String, Object> model, int task_id, String lastid) {

		List<Map<String, Object>> list = taskMyDao.getLogList(task_id, lastid);
		model.put("list", list);
		if (list != null && !list.isEmpty()) {
			model.put("lastid", list.get(0).get("log_id"));
			model.put("emptry", false);
		} else {
			model.put("emptry", true);
		}
		model.put("task_id", task_id);
		if (StringUtils.isEmpty(lastid)) {
			return "/task/log_list";
		} else {
			JSONObject result = new JSONObject();
			result.putAll(model);
			return result(model, result.toJSONString());
		}

	}

	@RequestMapping("/template_edit")
	public String tempate_list(HashMap<String, Object> model, String actType, int template_id) {
		if ("save".equals(actType)) {
			Map<String, Object> req = new HashMap<>();
			req.put("template_id", template_id);
			req.put("template_status", VarUtil.RequestInt(request, "template_status"));
			req.put("template_content", VarUtil.RequestStr(request, "template_content"));
			taskMyDao.updateTemplate(req);
			model.put("ok_msg", "操作成功");
			return "/task/result";
		}
		model.putAll(taskMyDao.getTemplateById(template_id));
		return "/task/template_edit";
	}

	@RequestMapping("/upload")
	public String upload(String module, String actType, int task_id, int type, HashMap<String, Object> model) {
		String pre = module.substring(0, module.indexOf("_") + 2);
		model.put("task_id", task_id);
		model.put("module", module);
		model.put("type", type);
		if ("save".equals(actType)) {
			String realFilePath = null;
			String remark = null;
			File uploadFile = null;
			if (type == 1) {
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				// 获得文件：
				MultipartFile file = multipartRequest.getFile("fname");
				String filePath = "/tpl/upload/" + task_id + "/" + module + "/";
				realFilePath = filePath + file.getOriginalFilename();
				File testfile = new File(InitConfig.basePath + filePath);
				if (!testfile.exists()) {
					testfile.mkdirs();
				}
				uploadFile = new File(InitConfig.basePath + realFilePath);
				try {
					file.transferTo(uploadFile);
				} catch (IllegalStateException | IOException e) {
					e.printStackTrace();
				}
				remark = VarUtil.RequestStr(multipartRequest, "remark");
			} else {
				realFilePath = VarUtil.RequestStr(request, "hfile");
				remark = VarUtil.RequestStr(request, "remark");
				try {
					File destFile = new File(InitConfig.basePath + "/tpl/upload/" + task_id + "/");
					if (!destFile.exists()) {
						destFile.mkdirs();
					}
					uploadFile = new File(InitConfig.basePath + realFilePath //
							.replaceAll("upload/[0-9]*/", "upload/" + task_id + "/"));
					FileUtils.copyFile(new File(InitConfig.basePath + realFilePath), //
							uploadFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			UploadUtil.saveTouzibuSheet(module, uploadFile);

			Map<String, Object> req = new HashMap<>();
			req.put("filepath", realFilePath);
			req.put("module", module);
			req.put("module_time", pre + "time");
			req.put("module_remark", pre + "remark");
			req.put("now", new Date());
			req.put("task_id", task_id);
			req.put("remark", remark);
			taskMyDao.updateTask4upfile(req);
			model.put("ok_msg", "操作成功");
			return "/task/result";
		}
		if (type == 2) {
			int page = VarUtil.RequestInt(request, "curPage", 1);
			int pageSize = VarUtil.RequestInt(request, "pageSize", 5);
			model.put("pageSize", pageSize);
			Map<String, Object> req = new HashMap<>();
			req.put("module_time", pre + "time");
			req.put("module_remark", pre + "remark");
			req.put("module", module);
			req.put("beginRow", pageSize * (page - 1));
			req.put("pageSize", pageSize);
			req.put("task_id", task_id);

			int count = taskMyDao.getHistoryFileCount(req);
			model.put("pageStr", PageUtil.page2("", page, pageSize, count, model));

			if (count > 0) {
				model.put("hfiles", taskMyDao.getHistoryFile(req));
			}
		}

		return "/task/upload";
	}

	@RequestMapping(value = "/create")
	@TaskSendMailTemplate({ 1 })
	public String create(Map<String, Object> model, String actType, int task_type) {
		if ("save".equals(actType)) {
			Map<String, Object> map = new HashMap<>();
			String task_name = VarUtil.RequestStr(request, "task_name");
			if (taskMyDao.getTaskCountByName(task_name) > 0) {
				model.put("error_msg", "任务名称重复");
				return "/task/result";
			}
			map.put("task_name", task_name);
			map.put("task_type", task_type);
			map.put("task_status", 0);
			map.put("task_estimate", VarUtil.RequestStr(request, "task_estimate"));
			map.put("task_remark", VarUtil.RequestStr(request, "task_remark"));
			map.put("task_createtime", new Date());
			taskMyDao.insertTask(map);
			taskMyDao.insertTaskDetail(map);
			// 发送邮件
			request.setAttribute("task_name", task_name); // 发送邮件使用
			request.setAttribute("EmailTemplate_1", true); // 发送邮件使用

			// 发送邮件结束
			model.put("ok_msg", "操作成功");
			return "/task/result";
		}
		model.put("task_type", task_type);
		return "/task/create";
	}

	@RequestMapping("/m5_input")
	public String m5_input(Map<String, Object> model, String actType, String task_name, int task_id) {
		if ("save".equals(actType)) {
			Map<String, Object> map = new HashMap<>();
			map.put("task_id", task_id);
			map.put("m5_param1", VarUtil.RequestInt(request, "m5_param1"));
			map.put("m5_param2", VarUtil.RequestInt(request, "m5_param2"));
			taskMyDao.updateTaskM5(map);
			model.put("ok_msg", "操作成功");
			return "/task/result";
		}
		model.put("task_name", task_name);
		model.put("task_id", task_id);
		model.put("m5_param1", VarUtil.RequestStr(request, "m5_param1"));
		model.put("m5_param2", VarUtil.RequestStr(request, "m5_param2"));
		return "/task/m5_input";
	}

	/// task/view.do?module=m3_3file&type=1
	@RequestMapping("/view")
	public String view(Map<String, Object> model, String module, int type, int task_id) {
		if (type == 1) { // 直接打开文件
			String filePath = taskMyDao.getModuleFilePathById(task_id, module);
			if (StringUtils.isEmpty(filePath)) {
				return result(model, "error");
			}
			try {
				String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
				// 设置response的编码方式
				response.setContentType("application/x-download");
				// response.setContentType("image/png");
				response.setCharacterEncoding("UTF-8");
				response.setHeader("Accept-Ranges", "bytes");
				response.setHeader("Pragma", "no-cache");
				response.setHeader("Cache-Control", "no-cache");
				response.setDateHeader("Expires", 0);
				String agent = request.getHeader("USER-AGENT");
				String downLoadName = null;
				if (null != agent && (-1 != agent.indexOf("MSIE") || -1 != agent.indexOf("Edge"))) // IE
				{
					downLoadName = java.net.URLEncoder.encode(fileName, "UTF-8");
				} else if (null != agent && -1 != agent.indexOf("Mozilla") && (-1 != agent.indexOf("Firefox") || -1 != agent.indexOf("Chrome"))) // Firefox
				{
					downLoadName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
				} else {
					downLoadName = java.net.URLEncoder.encode(fileName, "UTF-8");
				}
				response.setHeader("Content-Disposition", "attachment;filename=" + downLoadName);
				if (fileName != null) {
					InputStream in = null;
					OutputStream myout = null;
					try {
						myout = response.getOutputStream();
						File file = new File(InitConfig.basePath + filePath); // 拼写成完整的路径
						in = new FileInputStream(file);
						byte[] buf = new byte[1024];
						int len = -1;
						while ((len = in.read(buf, 0, 1024)) != -1) {
							myout.write(buf, 0, len);
						}
						myout.flush();

					} catch (IOException e) {
						e.printStackTrace();
					} finally {

						try {
							myout.close();
							in.close();
						} catch (IOException e) {
							e.printStackTrace();
						}

					}
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
			return null;
		} else if (type == 2) { // 查看json
			model.put("json6", "/tpl/upload/" + task_id + "/" + module + "/sheet6.json");
			model.put("json7", "/tpl/upload/" + task_id + "/" + module + "/sheet7.json");
			try {
				String json6 = FileUtils.readFileToString(new File(InitConfig.basePath+"/tpl/upload/" + task_id + "/" + module + "/sheet6.json"),"utf-8");
				model.put("json6Obj", JSON.parse(json6));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "/task/viewjson";
		} else {
			return result(model, "error");
		}
	}

	@RequestMapping("/edit")
	@TaskSendMailTemplate({ 2, 3, 6, 7, 10, 11, 14, 15, 18, 19 })
	public String edit(Map<String, Object> model, String actType, int task_id) {
		if ("save".equals(actType)) {
			Map<String, Object> map = new HashMap<>();
			map.put("task_name", VarUtil.RequestStr(request, "task_name"));
			map.put("task_remark", VarUtil.RequestStr(request, "task_remark"));
			map.put("task_id", VarUtil.RequestStr(request, "task_id"));

			int task_m1status = VarUtil.RequestInt(request, "task_m1status");
			int task_m2status = VarUtil.RequestInt(request, "task_m2status");
			int task_m3status = VarUtil.RequestInt(request, "task_m3status");
			int task_m4status = VarUtil.RequestInt(request, "task_m4status");
			int task_m5status = VarUtil.RequestInt(request, "task_m5status");

			int task_m1status_old = VarUtil.RequestInt(request, "task_m1status_old");
			int task_m2status_old = VarUtil.RequestInt(request, "task_m2status_old");
			int task_m3status_old = VarUtil.RequestInt(request, "task_m3status_old");
			int task_m4status_old = VarUtil.RequestInt(request, "task_m4status_old");
			int task_m5status_old = VarUtil.RequestInt(request, "task_m5status_old");

			// 发送邮件
			int task_type = VarUtil.RequestInt(request, "task_type");
			if (task_type == 1) {
				if (task_m1status != task_m1status_old) {
					if (task_m1status == 1) {
						request.setAttribute("EmailTemplate_2", true);
					} else {
						request.setAttribute("EmailTemplate_3", true);
					}
				}
				if (task_m2status != task_m2status_old) {
					if (task_m2status == 1) {
						request.setAttribute("EmailTemplate_6", true);
					} else {
						request.setAttribute("EmailTemplate_7", true);
					}
				}
				if (task_m3status != task_m3status_old) {
					if (task_m3status == 1) {
						request.setAttribute("EmailTemplate_10", true);
					} else {
						request.setAttribute("EmailTemplate_11", true);
					}
				}
				if (task_m5status != task_m5status_old) {
					if (task_m5status == 1) {
						request.setAttribute("EmailTemplate_18", true);
					} else {
						request.setAttribute("EmailTemplate_19", true);
					}
				}
			} else {
				if (task_m4status != task_m4status_old) {
					if (task_m4status == 1) {
						request.setAttribute("EmailTemplate_14", true);
					} else {
						request.setAttribute("EmailTemplate_15", true);
					}
				}
			}
			request.setAttribute("task_name", VarUtil.RequestStr(request, "task_name"));
			// 发送邮件结束

			map.put("task_m1status", task_m1status);
			map.put("task_m2status", task_m2status);
			map.put("task_m3status", task_m3status);
			map.put("task_m4status", task_m4status);
			map.put("task_m5status", task_m5status);
			map.put("task_status", VarUtil.RequestInt(request, "task_status"));

			taskMyDao.updateTask(map);
			model.put("ok_msg", "操作成功");
			return "/task/result";
		}
		model.putAll(taskMyDao.getTaskById(task_id));
		return "/task/update";
	}

	@RequestMapping("/copy")
	public String copy(Map<String, Object> model, int task_id) {
		Map<String, Object> map = new HashMap<>();
		map.put("task_id", task_id);
		try {
			taskMyDao.copyTask(map);
			taskMyDao.copyTaskDetail(map);
			File srcFile = new File(InitConfig.basePath + "/tpl/upload/" + task_id + "/");
			if (srcFile.exists()) {
				FileUtils.copyDirectory(new File(InitConfig.basePath + "/tpl/upload/" + task_id + "/"), //
						new File(InitConfig.basePath + "/tpl/upload/" + map.get("taskId") + "/"));
			}
			return result(model, getAjaxMessage(1, "操作成功！"));
		} catch (Exception e) {
			e.printStackTrace();
			return result(model, getAjaxMessage(0, "服务器异常！"));
		}

	}

	@RequestMapping("/clear")
	public String clear(Map<String, Object> model, int task_id) {
		try {
			taskMyDao.clearTask(task_id);
			FileUtils.deleteDirectory(new File(InitConfig.basePath + "/tpl/upload/" + task_id + "/"));
			return result(model, getAjaxMessage(1, "操作成功！"));
		} catch (Exception e) {
			e.printStackTrace();
			return result(model, getAjaxMessage(0, "服务器异常！"));
		}

	}

	@RequestMapping("/delete")
	public String delete(Map<String, Object> model, int task_id) {
		taskMyDao.delTask(task_id);
		return result(model, getAjaxMessage(1, "操作成功！"));
	}

	@RequestMapping("/stop")
	public String stop(Map<String, Object> model, int task_id, String module) {
		Map<String, Object> map = new HashMap<>();
		map.put("task_id", task_id);
		map.put("module", module + "_status");
		map.put("module_value", MODULE_STATUS_STOP);
		taskMyDao.stopTask(map);
		return result(model, getAjaxMessage(1, "操作成功！"));
	}

	@RequestMapping("/run")
	public String run(Map<String, Object> model, int task_id, String module, int type) {
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("task_id", task_id);
			if (type == 2) {
				if ("m1".equals(module)) {
					// 清空模块 2, 3, 5 的运行报告
					FileUtils.deleteDirectory(new File(InitConfig.basePath + "/tpl/upload/" + task_id + "/m2_report/"));
					FileUtils.deleteDirectory(new File(InitConfig.basePath + "/tpl/upload/" + task_id + "/m3_report/"));
					FileUtils.deleteDirectory(new File(InitConfig.basePath + "/tpl/upload/" + task_id + "/m5_report/"));
					map.put("clearreport", " ,m2_status = 0, m3_status=0, m5_status=0 ");
				}
			}
			map.put("module", module + "_status");
			map.put("module_value", MODULE_STATUS_RUNNING);
			taskMyDao.runTask(map);
			return result(model, getAjaxMessage(1, "操作成功！"));
		} catch (IOException e) {
			e.printStackTrace();
			return result(model, getAjaxMessage(0, "服务器异常！"));
		}
	}

	/**
	 * http://192.168.8.214:82/task/log.do?log_taskid=64&log_module=%E6%A8%A1%E5
	 * %9D%971&log_department=%E8%B4%A2%E5%8A%A1%E9%83%A8&log_content=asdfasdf&
	 * log_status=2
	 * 
	 * @param model
	 * @param log_taskid
	 * @param log_module
	 * @param log_department
	 * @param log_status
	 * @param log_content
	 */
	@RequestMapping("/log")
	@TaskSendMailTemplate({ 4, 5, 8, 9, 12, 13, 16, 17, 20, 21 })
	public void log(HashMap<String, Object> model, int log_taskid, String log_module, String log_department, int log_status, String log_content) {
		Map<String, Object> param = new HashMap<>();
		param.put("task_id", log_taskid);
		Map<String, Object> task = taskMyDao.getTaskById(log_taskid);
		request.setAttribute("task_name", MapUtils.getString(task, "task_name"));
		String module = null;
		if (log_module.equals("模块1")) {
			module = "m1_";

		} else if (log_module.equals("模块2")) {
			module = "m2_";

		} else if (log_module.equals("模块3")) {
			module = "m3_";

		} else if (log_module.equals("模块4")) {
			module = "m4_";

		} else if (log_module.equals("模块5")) {
			module = "m5_";

		}
		if (log_status == 2) { // 运行出错
			param.put("module", module + "status");
			param.put("module_value", MODULE_STATUS_ERROR);
			taskMyDao.updateTaskDetailRunStatus(param);

			if (log_module.equals("模块1")) {
				request.setAttribute("EmailTemplate_4", true);
			} else if (log_module.equals("模块2")) {
				request.setAttribute("EmailTemplate_8", true);
			} else if (log_module.equals("模块3")) {
				request.setAttribute("EmailTemplate_12", true);
			} else if (log_module.equals("模块4")) {
				request.setAttribute("EmailTemplate_16", true);
			} else if (log_module.equals("模块5")) {
				request.setAttribute("EmailTemplate_20", true);
			}
			request.setAttribute("EmailTemplate_pa", "'" + log_module + "." + log_department + ".运行出错'");
		} else if (log_status == 9) { // 生成报告
			param.put("module", module + "status");
			param.put("module_value", MODULE_STATUS_DONE);
			taskMyDao.updateTaskDetailRunStatus(param);

			if (log_module.equals("模块1")) {
				request.setAttribute("EmailTemplate_5", true);
			} else if (log_module.equals("模块2")) {
				request.setAttribute("EmailTemplate_9", true);
			} else if (log_module.equals("模块3")) {
				request.setAttribute("EmailTemplate_13", true);
			} else if (log_module.equals("模块4")) {
				request.setAttribute("EmailTemplate_17", true);
			} else if (log_module.equals("模块5")) {
				request.setAttribute("EmailTemplate_21", true);
			}
		}

		param.put("log_taskid", log_taskid);
		param.put("log_module", log_module);
		param.put("log_department", log_department);
		param.put("log_status", log_status);
		param.put("log_content", log_content);
		param.put("log_time", new Date());
		taskMyDao.insertlog(param);
	}
}
