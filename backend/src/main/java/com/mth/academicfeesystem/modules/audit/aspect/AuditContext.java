package com.mth.academicfeesystem.modules.audit.aspect;

public class AuditContext {
    private static final ThreadLocal<Object> BEFORE_VALUE = new ThreadLocal<>();
    private static final ThreadLocal<Object> AFTER_VALUE = new ThreadLocal<>();

    private AuditContext() {
    }

    public static void setBefore(Object value) {
        BEFORE_VALUE.set(value);
    }

    public static Object getBefore() {
        return BEFORE_VALUE.get();
    }

    public static void setAfter(Object value) {
        AFTER_VALUE.set(value);
    }

    public static Object getAfter() {
        return AFTER_VALUE.get();
    }

    public static void clear() {
        BEFORE_VALUE.remove();
        AFTER_VALUE.remove();
    }
}
