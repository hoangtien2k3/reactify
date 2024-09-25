/*
 * Copyright 2024 the original author Hoàng Anh Tiến.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.hoangtien2k3.reactify.factory;

import org.modelmapper.ModelMapper;

/**
 * <p>
 * ModelMapperFactory class.
 * </p>
 *
 * @author hoangtien2k3
 */
public class ModelMapperFactory {
    private static ModelMapper modelMapper = new ModelMapper();

    /**
     * <p>
     * getInstance.
     * </p>
     *
     * @return a {@link org.modelmapper.ModelMapper} object
     */
    public static ModelMapper getInstance() {
        return modelMapper;
    }
}