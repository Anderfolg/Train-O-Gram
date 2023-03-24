package org.anderfolg.trainogram.service;

import java.util.Map;

public interface AuthService {
    Map<Object, Object> login( String username, String password);
}
