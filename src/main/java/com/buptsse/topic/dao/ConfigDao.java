package com.buptsse.topic.dao;

import com.buptsse.topic.model.SystemConfig;
import java.util.List;

public interface ConfigDao {
    String getValue(String key);
    List<SystemConfig> findAll();
    void updateValue(String key, String value);
}
