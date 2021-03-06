package com.ngtesting.platform.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ngtesting.platform.config.Constant;
import com.ngtesting.platform.entity.TestCustomFieldOption;
import com.ngtesting.platform.service.IssueCustomFieldOptionService;
import com.ngtesting.platform.util.AuthPassport;
import com.ngtesting.platform.vo.CustomFieldOptionVo;
import com.ngtesting.platform.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(Constant.API_PATH_CLIENT + "issue_custom_field_option/")
public class IssueCustomFieldOptionAction extends BaseAction {
	@Autowired
	IssueCustomFieldOptionService customFieldOptionService;

	@AuthPassport(validate = true)
	@RequestMapping(value = "save", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> save(HttpServletRequest request, @RequestBody JSONObject json) {
		Map<String, Object> ret = new HashMap<String, Object>();

		UserVo userVo = (UserVo) request.getSession().getAttribute(Constant.HTTP_SESSION_USER_KEY);

		CustomFieldOptionVo option = JSON.parseObject(JSON.toJSONString(json.getJSONObject("model")), CustomFieldOptionVo.class);

		TestCustomFieldOption po = customFieldOptionService.save(option);
		List<CustomFieldOptionVo> vos = customFieldOptionService.listVos(po.getFieldId());

		ret.put("data", vos);
		ret.put("code", Constant.RespCode.SUCCESS.getCode());
		return ret;
	}

	@AuthPassport(validate = true)
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> delete(HttpServletRequest request, @RequestBody JSONObject json) {
		Map<String, Object> ret = new HashMap<String, Object>();

        Long fieldId = json.getLong("fieldId");
		Long id = json.getLong("id");

		boolean success = customFieldOptionService.delete(id);
        List<CustomFieldOptionVo> vos = customFieldOptionService.listVos(fieldId);

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
		Long fieldId = json.getLong("fieldId");
		Long id = json.getLong("id");
		String act = json.getString("act");

		boolean success = customFieldOptionService.changeOrderPers(id, act, fieldId);
        List<CustomFieldOptionVo> vos = customFieldOptionService.listVos(fieldId);

        ret.put("data", vos);
		ret.put("code", Constant.RespCode.SUCCESS.getCode());

		return ret;
	}

}
