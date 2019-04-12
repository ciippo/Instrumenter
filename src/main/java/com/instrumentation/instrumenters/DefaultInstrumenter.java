package com.instrumentation.instrumenters;

import java.util.ArrayList;
import java.util.List;

import com.instrumentation.data.InstrumentationData;

public abstract class DefaultInstrumenter<T> implements Instrumenter<T> {

    private final List<T> datas;
    private InstrumentationData instrumentationData;

    public DefaultInstrumenter() {
        this.datas = new ArrayList<>();
    }

    @Override
    public void setInstrumentationData(final InstrumentationData instrumentationData) {
        this.instrumentationData = instrumentationData;
    }

    protected InstrumentationData getInstrumentationData() {
        return this.instrumentationData;
    }

    @Override
    public void addData(final T data) {
        this.datas.add(data);
    }

    protected List<T> getDatas() {
        return this.datas;
    }
}
