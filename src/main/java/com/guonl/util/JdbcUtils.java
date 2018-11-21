package com.guonl.util;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * Created by guonl
 * Date 2018/11/15 7:21 PM
 * Description:
 */
public class JdbcUtils {
    public JdbcUtils() {
    }

    public static void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException var2) {
                var2.printStackTrace();
            } catch (Throwable var3) {
                var3.printStackTrace();
            }
        }

    }

    public static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException var2) {
                var2.printStackTrace();
            } catch (Throwable var3) {
                var3.printStackTrace();
            }
        }

    }

    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException var2) {
                var2.printStackTrace();
            } catch (Throwable var3) {
                var3.printStackTrace();
            }
        }

    }

    public static Object getResultSetValue(ResultSet rs, int index, Class requiredType) throws SQLException {
        if (requiredType == null) {
            return getResultSetValue(rs, index);
        } else {
            Object value = null;
            boolean wasNullCheck = false;
            if (String.class.equals(requiredType)) {
                value = rs.getString(index);
            } else if (!Boolean.TYPE.equals(requiredType) && !Boolean.class.equals(requiredType)) {
                if (!Byte.TYPE.equals(requiredType) && !Byte.class.equals(requiredType)) {
                    if (!Short.TYPE.equals(requiredType) && !Short.class.equals(requiredType)) {
                        if (!Integer.TYPE.equals(requiredType) && !Integer.class.equals(requiredType)) {
                            if (!Long.TYPE.equals(requiredType) && !Long.class.equals(requiredType)) {
                                if (!Float.TYPE.equals(requiredType) && !Float.class.equals(requiredType)) {
                                    if (!Double.TYPE.equals(requiredType) && !Double.class.equals(requiredType) && !Number.class.equals(requiredType)) {
                                        if (byte[].class.equals(requiredType)) {
                                            value = rs.getBytes(index);
                                        } else if (Date.class.equals(requiredType)) {
                                            value = rs.getDate(index);
                                        } else if (Time.class.equals(requiredType)) {
                                            value = rs.getTime(index);
                                        } else if (!Timestamp.class.equals(requiredType) && !java.util.Date.class.equals(requiredType)) {
                                            if (BigDecimal.class.equals(requiredType)) {
                                                value = rs.getBigDecimal(index);
                                            } else if (Blob.class.equals(requiredType)) {
                                                value = rs.getBlob(index);
                                            } else if (Clob.class.equals(requiredType)) {
                                                value = rs.getClob(index);
                                            } else {
                                                value = getResultSetValue(rs, index);
                                            }
                                        } else {
                                            value = rs.getTimestamp(index);
                                        }
                                    } else {
                                        value = new Double(rs.getDouble(index));
                                        wasNullCheck = true;
                                    }
                                } else {
                                    value = new Float(rs.getFloat(index));
                                    wasNullCheck = true;
                                }
                            } else {
                                value = new Long(rs.getLong(index));
                                wasNullCheck = true;
                            }
                        } else {
                            value = new Integer(rs.getInt(index));
                            wasNullCheck = true;
                        }
                    } else {
                        value = new Short(rs.getShort(index));
                        wasNullCheck = true;
                    }
                } else {
                    value = new Byte(rs.getByte(index));
                    wasNullCheck = true;
                }
            } else {
                value = rs.getBoolean(index);
                wasNullCheck = true;
            }

            if (wasNullCheck && value != null && rs.wasNull()) {
                value = null;
            }

            return value;
        }
    }

    public static Object getResultSetValue(ResultSet rs, int index) throws SQLException {
        Object obj = rs.getObject(index);
        String className = null;
        if (obj != null) {
            className = obj.getClass().getName();
        }

        if (obj instanceof Blob) {
            obj = rs.getBytes(index);
        } else if (obj instanceof Clob) {
            obj = rs.getString(index);
        } else if (className != null && ("oracle.sql.TIMESTAMP".equals(className) || "oracle.sql.TIMESTAMPTZ".equals(className))) {
            obj = rs.getTimestamp(index);
        } else if (className != null && className.startsWith("oracle.sql.DATE")) {
            String metaDataClassName = rs.getMetaData().getColumnClassName(index);
            if (!"java.sql.Timestamp".equals(metaDataClassName) && !"oracle.sql.TIMESTAMP".equals(metaDataClassName)) {
                obj = rs.getDate(index);
            } else {
                obj = rs.getTimestamp(index);
            }
        } else if (obj != null && obj instanceof java.util.Date && "java.sql.Timestamp".equals(rs.getMetaData().getColumnClassName(index))) {
            obj = rs.getTimestamp(index);
        }

        return obj;
    }
}
