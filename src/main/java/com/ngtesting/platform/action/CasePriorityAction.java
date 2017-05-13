package com.ngtesting.platform.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.ngtesting.platform.entity.SysCasePriority;
import com.ngtesting.platform.entity.SysCaseType;
import com.ngtesting.platform.entity.SysCustomField;
import com.ngtesting.platform.service.CasePriorityService;
import com.ngtesting.platform.util.AuthPassport;
import com.ngtesting.platform.util.Constant;
import com.ngtesting.platform.vo.CasePriorityVo;
import com.ngtesting.platform.vo.CaseTypeVo;
import com.ngtesting.platform.vo.CustomFieldVo;
import com.ngtesting.platform.vo.TestProjectAccessHistoryVo;
import com.ngtesting.platform.vo.UserVo;


@Controller
@RequestMapping(Constant.API_PATH_CLIENT + "case_priority/")
public class CasePriorityAction extends BaseAction {
	private static final Log log = LogFactory.getLog(CasePriorityAction.class);
	
	@Autowired
	CasePriorityService casePriorityService;
	
	@AuthPassport(validate = true)
	@RequestMapping(value = "list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> list(HttpServletRequest request, @RequestBody JSONObject json) {
		Map<String, Object> ret = new HashMap<String, Object>();
		
		UserVo userVo = (UserVo) request.getSession().getAttribute(Constant.HTTP_SESSION_USER_KEY);
		Long orgId = userVo.getDefaultOrgId();

		List<CasePriorityVo> vos = casePriorityService.listVos(orgId);
		
        ret.put("data", vos);
		ret.put("code", Constant.RespCode.SUCCESS.getCode());
		
		return ret;
	}
	
	@AuthPassport(validate = true)
	@RequestMapping(value = "get", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> get(HttpServletRequest request, @RequestBody JSONObject json) {
		Map<String, Object> ret = new HashMap<String, Object>();
		
		UserVo userVo = (UserVo) request.getSession().getAttribute(Constant.HTTP_SESSION_USER_KEY);

		Long id = json.getLong("id");
		if (id == null) {
			ret.put("data", new CasePriorityVo());
			ret.put("code", Constant.RespCode.SUCCESS.getCode());
			return ret;
		}
		
		SysCasePriority po = (SysCasePriority) casePriorityService.get(SysCasePriority.class, id);
		CasePriorityVo vo = casePriorityService.genVo(po);
		ret.put("data", vo);
		
		ret.put("code", Constant.RespCode.SUCCESS.getCode());
		return ret;
	}

	@AuthPassport(validate = true)
	@RequestMapping(value = "save", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> save(HttpServletRequest request, @RequestBody JSONObject json) {
		Map<String, Object> ret = new HashMap<String, Object>();
		
		UserVo userVo = (UserVo) request.getSession().getAttribute(Constant.HTTP_SESSION_USER_KEY);
		Long orgId = userVo.getDefaultOrgId();
		
		CasePriorityVo vo = json.getObject("model", CasePriorityVo.class);
		
		SysCasePriority po = casePriorityService.save(vo, orgId);
		CasePriorityVo projectVo = casePriorityService.genVo(po);
        
        ret.put("data", projectVo);
		ret.put("code", Constant.RespCode.SUCCESS.getCode());
		return ret;
	}
	
	@AuthPassport(validate = true)
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> delete(HttpServletRequest request, @RequestBody JSONObject json) {
		Map<String, Object> ret = new HashMap<String, Object>();
		
		Long id = json.getLong("id");
		
		casePriorityService.delete(id);
        
		ret.put("code", Constant.RespCode.SUCCESS.getCode());
		return ret;
	}
	
	@AuthPassport(validate = true)
	@RequestMapping(value = "setDefault", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> setDefault(HttpServletRequest request, @RequestBody JSONObject json) {
		Map<String, Object> ret = new HashMap<String, Object>();
		
		UserVo userVo = (UserVo) request.getSession().getAttribute(Constant.HTTP_SESSION_USER_KEY);
		Long orgId = userVo.getDefaultOrgId();
		Long id = json.getLong("id");
		
		boolean success = casePriorityService.setDefaultPers(id, orgId);
		List<CasePriorityVo> vos = casePriorityService.listVos(orgId);
		
        ret.put("data", vos);
		ret.put("code", Constant.RespCode.SUCCESS.getCode());
		
		return ret;
	}
	
	@AuthPassport(validate = true)
	@RequestMapping(value = "changeOrder", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> changeOrder(HttpServletRequest request, @RequestBody JSONObject json) {
		Map<String, Object> ret = new HashMap<String, Object>();
		
		UserVo userVo = (UserVo) request.getSession().getAttribute(Constant.HTTP_SESSION_USER_KEY);
		Long orgId = userVo.getDefaultOrgId();
		Long id = json.getLong("id");
		String act = json.getString("act");
		
		boolean success = casePriorityService.changeOrderPers(id, act);
		
		List<CasePriorityVo> vos = casePriorityService.listVos(orgId);
		
        ret.put("data", vos);
		ret.put("code", Constant.RespCode.SUCCESS.getCode());
		
		return ret;
	}
}