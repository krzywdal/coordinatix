package com.zitt.cdx.domain;

/**
 * @author lukasz
 * @since 2022-05-08
 */
public class Permit {

    private String permitId;
    private String resourceId;
    private int maxAllowedPermits;
    private int currentPermits;
    private long expirationMs;
    private boolean isGranted;

    /**
     * Default constructor
     */
    public Permit() {
        isGranted = false;
    }

    public String getPermitId() {
        return permitId;
    }

    public void setPermitId(String permitId) {
        this.permitId = permitId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public int getMaxAllowedPermits() {
        return maxAllowedPermits;
    }

    public void setMaxAllowedPermits(int maxAllowedPermits) {
        this.maxAllowedPermits = maxAllowedPermits;
    }

    public int getCurrentPermits() {
        return currentPermits;
    }

    public void setCurrentPermits(int currentPermits) {
        this.currentPermits = currentPermits;
    }

    public long getExpirationMs() {
        return expirationMs;
    }

    public void setExpirationMs(long expirationMs) {
        this.expirationMs = expirationMs;
    }

    public boolean isGranted() {
        return isGranted;
    }

    public void setGranted(boolean granted) {
        isGranted = granted;
    }

    /**
     *
     */
    public static class Builder extends BuilderSupport<Permit> {

        public Builder() {
            super(new Permit());
        }

        public Builder permitId(String permitId) {
            getObject().permitId = permitId;
            return this;
        }

        public Builder resourceId(String resourceId) {
            getObject().resourceId = resourceId;
            return this;
        }

        public Builder maxAllowedPermits(int maxAllowedPermits) {
            getObject().maxAllowedPermits = maxAllowedPermits;
            return this;
        }

        public Builder currentPermits(int currentPermits) {
            getObject().currentPermits = currentPermits;
            return this;
        }

        public Builder expirationMs(long expirationMs) {
            getObject().expirationMs = expirationMs;
            return this;
        }

        public Builder isGranted(boolean isGranted) {
            getObject().isGranted = isGranted;
            return this;
        }

    }

}
