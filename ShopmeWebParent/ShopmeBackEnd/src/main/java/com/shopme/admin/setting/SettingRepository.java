package com.shopme.admin.setting;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface SettingRepository extends CrudRepository <Setting, String> {

	public List<Setting> findByCategory(SettingCategory category);
}
