package com.asofdate.platform.controller;

import com.asofdate.platform.authentication.JwtService;
import com.asofdate.platform.entity.RoleEntity;
import com.asofdate.platform.service.RoleService;
import com.asofdate.utils.Hret;
import com.asofdate.utils.JoinCode;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by hzwy23 on 2017/6/18.
 */
@RestController
@RequestMapping(value = "/v1/auth/role")
public class RoleController {
    private final Logger logger = LoggerFactory.getLogger(RoleController.class);
    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "/other", method = RequestMethod.GET)
    public List getOther(HttpServletRequest request) {
        String userId = request.getParameter("user_id");
        return roleService.getOther(userId);
    }

    @RequestMapping(value = "/owner", method = RequestMethod.GET)
    public List getOwner(HttpServletRequest request) {
        String userId = request.getParameter("user_id");
        return roleService.getOwner(userId);
    }

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public String auth(HttpServletResponse response, HttpServletRequest request) {
        String modifyUserId = JwtService.getConnUser(request).getUserId();
        JSONArray json = new JSONArray(request.getParameter("JSON"));
        try {
            int size = roleService.auth(json, modifyUserId);
            if (1 == size) {
                return Hret.success(200, "success", null);
            }
            response.setStatus(422);
            return Hret.error(422, "授权失败,用户已经拥有了这个角色", null);
        } catch (Exception e) {
            logger.info(e.getMessage());
            response.setStatus(421);
            return Hret.error(421, "授权失败,用户已经拥有了这个角色", null);
        }
    }


    @RequestMapping(value = "/auth/batch", method = RequestMethod.POST)
    public String batchAuth(HttpServletResponse response, HttpServletRequest request) {
        String modifyUserId = JwtService.getConnUser(request).getUserId();
        JSONArray json = new JSONArray(request.getParameter("JSON"));
        try {
            int size = roleService.batchAuth(json, modifyUserId);
            if (1 == size) {
                return Hret.success(200, "success", null);
            }
            response.setStatus(422);
            return Hret.error(422, "授权失败,用户已经拥有了这个角色", null);
        } catch (Exception e) {
            logger.info(e.getMessage());
            response.setStatus(421);
            return Hret.error(421, "授权失败,用户已经拥有了这个角色", null);
        }
    }

    @RequestMapping(value = "/revoke", method = RequestMethod.POST)
    public String revoke(HttpServletResponse response, HttpServletRequest request) {
        JSONArray json = new JSONArray(request.getParameter("JSON"));
        try {
            int size = roleService.revoke(json);
            if (1 == size) {
                return Hret.success(200, "success", null);
            }
            response.setStatus(422);
            return Hret.error(422, "撤销权限失败,请联系管理员", null);
        } catch (Exception e) {
            logger.info(e.getMessage());
            response.setStatus(421);
            return Hret.error(421, "撤销权限失败,请联系管理员", null);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<RoleEntity> findAll(HttpServletRequest request) {
        String domainId = request.getParameter("domain_id");
        if (domainId == null || domainId.isEmpty()) {
            domainId = JwtService.getConnUser(request).getDomainID();
        }
        return roleService.findAll(domainId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public String add(HttpServletResponse response, HttpServletRequest request) {
        RoleEntity roleEntity = parse(request);
        int size = roleService.add(roleEntity);
        if (1 == size) {
            return Hret.success(200, "success", null);
        }
        response.setStatus(422);
        return Hret.error(422, "新增角色信息失败，角色编码重复", null);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public String update(HttpServletRequest request, HttpServletResponse response) {
        RoleEntity roleEntity = parse(request);
        int size = roleService.update(roleEntity);
        if (1 == size) {
            return Hret.success(200, "success", null);
        }
        response.setStatus(421);
        return Hret.error(421, "更新角色信息失败，请联系管理员", null);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(HttpServletResponse response, HttpServletRequest request) {
        String json = request.getParameter("JSON");
        JSONArray jsonArray = new JSONArray(json);
        int size = roleService.delete(jsonArray);
        if (1 == size) {
            return Hret.success(200, "success", null);
        }
        response.setStatus(421);
        return Hret.error(421, "删除角色信息失败,角色已经被使用,请先解除引用关系", null);
    }

    private RoleEntity parse(HttpServletRequest request) {
        RoleEntity roleEntity = new RoleEntity();
        String codeNumber = request.getParameter("role_id");
        String domainId = request.getParameter("domain_id");
        roleEntity.setCode_number(codeNumber);
        roleEntity.setRole_name(request.getParameter("role_name"));
        roleEntity.setRole_status_code(request.getParameter("role_status"));
        roleEntity.setDomain_id(domainId);
        String userId = JwtService.getConnUser(request).getUserId();
        roleEntity.setCreate_user(userId);
        roleEntity.setModify_user(userId);
        String roleId = JoinCode.join(domainId, codeNumber);
        roleEntity.setRole_id(roleId);

        return roleEntity;
    }
}
