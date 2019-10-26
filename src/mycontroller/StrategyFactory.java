package mycontroller;

public class StrategyFactory {
    /**
     * Singleton factory to create strategies.
     */
    private static StrategyFactory stratFactory;

    private ExploreStrategy explore;
    private ParcelStrategy parcel;
    private ExitStrategy exit;

    private StrategyFactory() {
        explore = new ExploreStrategy();
        parcel = new ParcelStrategy();
        exit = new ExitStrategy();
    }

    public static StrategyFactory getInstance() {
        if (stratFactory == null)
            stratFactory = new StrategyFactory();

        return stratFactory;
    }
    public IGoalStrategy getStrategy(String strategy) {
        if (strategy.equalsIgnoreCase("explore"))
            return explore;
        else if (strategy.equalsIgnoreCase("parcel"))
            return parcel;
        else if (strategy.equalsIgnoreCase("exit"))
            return exit;
        return null;
    }
}
