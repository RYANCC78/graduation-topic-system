package com.buptsse.topic.service;

import com.buptsse.topic.dao.ConfigDao;
import com.buptsse.topic.dao.impl.ConfigDaoImpl;
import com.buptsse.topic.model.SystemConfig;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

public class ConfigService {
    private ConfigDao configDao = new ConfigDaoImpl();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public List<SystemConfig> listAll() {
        return configDao.findAll();
    }

    public String getStart() {
        return configDao.getValue("selection_start_time");
    }

    public String getEnd() {
        return configDao.getValue("selection_end_time");
    }

    public boolean isWithinSelectionPeriod() {
        String start = getStart();
        String end = getEnd();
        if (start == null || end == null) return false;
        try {
            Timestamp now = new Timestamp(System.currentTimeMillis());
            Timestamp s = Timestamp.valueOf(start);
            Timestamp e = Timestamp.valueOf(end);
            return !now.before(s) && !now.after(e);
        } catch (Exception ex) {
            return false;
        }
    }

    public String getPeriodText() {
        return "选题时间：" + getStart() + " 至 " + getEnd();
    }

    public void updatePeriod(String start, String end) {
        if (start == null || start.trim().isEmpty() || end == null || end.trim().isEmpty()) {
            throw new IllegalArgumentException("起止时间不能为空");
        }
        try {
            sdf.parse(start.trim());
            sdf.parse(end.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("时间格式应为 yyyy-MM-dd HH:mm:ss");
        }
        configDao.updateValue("selection_start_time", start.trim());
        configDao.updateValue("selection_end_time", end.trim());
    }
}
