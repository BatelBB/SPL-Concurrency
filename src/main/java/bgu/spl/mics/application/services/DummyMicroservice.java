package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;

public class DummyMicroservice extends MicroService {


    /**
     * @param name the micro-service name (used mainly for debugging purposes -
     *             does not have to be unique)
     */
    public DummyMicroservice(String name) {
        super(name);
    }

    @Override
    protected void initialize() {

    }


}
