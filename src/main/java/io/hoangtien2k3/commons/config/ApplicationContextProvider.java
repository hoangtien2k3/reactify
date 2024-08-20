/*
 * Copyright 2024 author - Hoàng Anh Tiến
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 */
package io.hoangtien2k3.commons.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Provides access to the Spring {@link ApplicationContext} from anywhere in the
 * application.
 *
 * <p>
 * This class implements the {@link ApplicationContextAware} interface to
 * receive the {@link ApplicationContext} and store it in a static field. It
 * allows for the retrieval of the {@link ApplicationContext} from any class,
 * enabling access to beans and other context-related features.
 * </p>
 *
 * <p>
 * This class is a {@link Component}, so it is managed by the Spring container.
 * Once the context is set, it can be accessed through the static method
 * {@link #getApplicationContext()}.
 * </p>
 *
 * <p>
 * The {@link #setApplicationContext(ApplicationContext)} method is used
 * internally by the Spring container to inject the {@link ApplicationContext}
 * into this class. It is not intended to be called manually.
 * </p>
 *
 * @see ApplicationContext
 * @see ApplicationContextAware
 * @see Component
 * @see ApplicationContextProvider
 *
 * @author hoangtien2k3
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {
    private static ApplicationContext context;

    /**
     * Returns the {@link ApplicationContext} stored in this class.
     *
     * <p>
     * This method provides access to the {@link ApplicationContext} from any part
     * of the application. It allows for retrieving beans and other context-related
     * functionality.
     * </p>
     *
     * @return the {@link ApplicationContext} instance
     */
    public static ApplicationContext getApplicationContext() {
        return context;
    }

    /**
     * Sets the {@link ApplicationContext} for this class.
     *
     * <p>
     * This method is called by the Spring container to inject the
     * {@link ApplicationContext}. It should not be called manually. The
     * {@link ApplicationContext} is stored in a static field for later retrieval.
     * </p>
     *
     * @param ac
     *            the {@link ApplicationContext} to set
     * @throws BeansException
     *             if the context could not be set
     */
    @SuppressWarnings("squid:S2696")
    @Override
    public void setApplicationContext(@NotNull ApplicationContext ac) throws BeansException {
        context = ac;
    }
}
