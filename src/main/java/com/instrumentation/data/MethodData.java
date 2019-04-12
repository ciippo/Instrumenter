package com.instrumentation.data;

import java.util.Arrays;

public class MethodData {

    private final int access;
    private final String name;
    private final String desc;
    private final String signature;
    private final String[] exceptions;

    public MethodData(final int access, final String name, final String desc, final String signature,
            final String[] exceptions) {
        this.access = access;
        this.name = name;
        this.desc = desc;
        this.signature = signature;
        this.exceptions = exceptions;
    }

    public int getAccess() {
        return this.access;
    }

    public String getName() {
        return this.name;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getSignature() {
        return this.signature;
    }

    public String[] getExceptions() {
        return this.exceptions;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + this.access;
        result = (prime * result) + ((this.desc == null) ? 0 : this.desc.hashCode());
        result = (prime * result) + Arrays.hashCode(this.exceptions);
        result = (prime * result) + ((this.name == null) ? 0 : this.name.hashCode());
        result = (prime * result) + ((this.signature == null) ? 0 : this.signature.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MethodData other = (MethodData) obj;
        if (this.access != other.access) {
            return false;
        }
        if (this.desc == null) {
            if (other.desc != null) {
                return false;
            }
        } else if (!this.desc.equals(other.desc)) {
            return false;
        }
        if (!Arrays.equals(this.exceptions, other.exceptions)) {
            return false;
        }
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        if (this.signature == null) {
            if (other.signature != null) {
                return false;
            }
        } else if (!this.signature.equals(other.signature)) {
            return false;
        }
        return true;
    }

}
