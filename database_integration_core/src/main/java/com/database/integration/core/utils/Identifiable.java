package com.database.integration.core.utils;

import java.io.Serializable;

public interface Identifiable<T extends Serializable> {
    T getId();
}