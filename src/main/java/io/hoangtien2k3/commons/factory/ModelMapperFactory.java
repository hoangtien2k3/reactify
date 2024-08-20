package io.hoangtien2k3.commons.factory;

import org.modelmapper.ModelMapper;

public class ModelMapperFactory {
    private static ModelMapper modelMapper = new ModelMapper();

    public static ModelMapper getInstance() {
        return modelMapper;
    }
}
