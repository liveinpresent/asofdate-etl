package com.asofdate.platform.dao;

import com.asofdate.platform.entity.ResourceEntity;
import com.asofdate.platform.entity.RoleResourceEntity;

import java.util.List;

/**
 * Created by hzwy23 on 2017/6/20.
 */
public interface RoleResourceDao {
    List<RoleResourceEntity> findAll(String roleId);

    List<ResourceEntity> getOther(String roleId);

    int revoke(String roleId, String resId);

    int auth(String roleId, String resId);
}
