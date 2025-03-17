package org.example.ssm02;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class States {
    public static final String READY = "ready";
    public static final String DEPLOY_PREPARE = "deploy-prepare";
    public static final String DEPLOY_EXECUTE = "deploy-execute";
}
