package com.idis.shared.time;

import java.util.Date;
import java.util.Stack;

public class TimeProviderContext {
    private static final ITimeProvider DEFAULT_PROVIDER = new DefaultTimeProvider();
    private static final Stack<ITimeProvider> providers = new Stack<>();

    static {
        providers.push(DEFAULT_PROVIDER);
    }

    public static ITimeProvider getCurrentProvider() {
        return providers.peek();
    }

    public static void advanceTimeTo(Date time) {
        providers.push(new TestTimeProvider(time));
    }

    public static void reset() {
        while (providers.size() > 1) {
            providers.pop();
        }
    }
}

class TestTimeProvider implements ITimeProvider {
    private Date fixedDate;

    public TestTimeProvider(Date fixedDate) {
        this.fixedDate = fixedDate;
    }

    @Override
    public Date now() {
        return fixedDate;
    }
}
