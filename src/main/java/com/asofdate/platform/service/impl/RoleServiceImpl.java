package com.asofdate.platform.service.impl;

import com.asofdate.platform.dao.RoleDao;
import com.asofdate.platform.entity.RoleEntity;
import com.asofdate.platform.service.RoleService;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by hzwy23 on 2017/6/18.
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;

    @Override
    public List<RoleEntity> findAll(String domainId) {
        return roleDao.findAll(domainId);
    }

    @Override
    public List<RoleEntity> getOther(String userId) {
        return roleDao.getOther(userId);
    }

    @Override
    public List<RoleEntity> getOwner(String userId) {
        return roleDao.getOwner(userId);
    }

    @Override
    public RoleEntity getDetails(String roleId) {
        return roleDao.getDetails(roleId);
    }

    @Override
    public int auth(JSONArray jsonArray, String modifyUserId) {
        return roleDao.auth(jsonArray, modifyUserId);
    }

    @Override
    public int revoke(JSONArray jsonArray) {
        return roleDao.revoke(jsonArray);
    }

    @Override
    public int batchAuth(JSONArray jsonArray, String modifyUserId) {
        return roleDao.batchAuth(jsonArray, modifyUserId);
    }

    @Override
    public int add(RoleEntity roleEntity) {
        return roleDao.add(roleEntity);
    }

    @Override
    public int delete(JSONArray jsonArray) {
        return roleDao.delete(jsonArray);
    }

    @Override
    public int update(RoleEntity roleEntity) {
        return roleDao.update(roleEntity);
    }
}
