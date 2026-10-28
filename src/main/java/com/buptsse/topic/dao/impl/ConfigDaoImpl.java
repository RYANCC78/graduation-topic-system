package com.buptsse.topic.dao.impl;

import com.buptsse.topic.dao.ConfigDao;
import com.buptsse.topic.model.SystemConfig;
import com.buptsse.topic.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConfigDaoImpl implements ConfigDao {

    @Override
    public String getValue(String key) {
        Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement("SELECT config_value FROM system_config WHERE config_key=?");
            ps.setString(1, key);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getString("config_value");
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally { DBUtil.close(conn, ps, rs); }
    }

    @Override
    public List<SystemConfig> findAll() {
        List<SystemConfig> list = new ArrayList<>();
        Connection conn = null; Statement st = null; ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            st = conn.createStatement();
            rs = st.executeQuery("SELECT * FROM system_config ORDER BY config_id");
            while (rs.next()) {
                SystemConfig c = new SystemConfig();
                c.setConfigId(rs.getInt("config_id"));
                c.setConfigKey(rs.getString("config_key"));
                c.setConfigValue(rs.getString("config_value"));
                c.setDescription(rs.getString("description"));
                list.add(c);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally { DBUtil.close(conn, st, rs); }
        return list;
    }

    @Override
    public void updateValue(String key, String value) {
        Connection conn = null; PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement("UPDATE system_config SET config_value=? WHERE config_key=?");
            ps.setString(1, value);
            ps.setString(2, key);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally { DBUtil.close(conn, ps); }
    }
}
