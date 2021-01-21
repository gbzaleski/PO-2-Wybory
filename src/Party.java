// Klasa reprezentująca partię.
abstract public class Party
{
    // Nazwa partii.
    protected String name;
    // Budżet partii.
    protected int budget;

    public Party(String name, int budget)
    {
        this.name = name;
        this.budget = budget;
    }

    @Override
    public String toString()
    {
        return name;
    }

    // Rozpoczęcie wybranego przez partię rodzaju kampanii.
    public abstract void launchCampaign(int districts, int parties, int traits, int actions, District[] allDistricts, int[][] campaign);
}
