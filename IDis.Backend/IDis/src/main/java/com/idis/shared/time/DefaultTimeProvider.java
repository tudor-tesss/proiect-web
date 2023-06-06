package com.idis.shared.time;

import java.util.Date;

public class DefaultTimeProvider implements ITimeProvider {
    @Override
    public Date now() {
        return new Date();
    }
}