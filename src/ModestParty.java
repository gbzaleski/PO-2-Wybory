import static java.lang.Math.abs;

// Skromna partia.
public class ModestParty extends Party
{

    public ModestParty(String name, int budget)
    {
        super(name, budget);
    }

    // Znalezenie i wykonanie najtańszej kampanii (tak wiele razy na ile pozwala budżet).
    // Najtansza kampania = Najtansze działanie na najmniejszym okręgu.
    @Override
    public void launchCampaign(int districts, int parties, int traits, int actions, District[] allDistricts, int[][] campaign)
    {
        int minActionId = 0;
        int minActionCost = Integer.MAX_VALUE;
        for (int a = 1; a <= actions; ++a)
        {
            int curActionCost = 0;
            for (int t = 0; t < traits; ++t)
            {
                curActionCost += abs(campaign[a][t]);
            }
            if (minActionCost > curActionCost)
            {
                minActionCost = curActionCost;
                minActionId = a;
            }
        }

        int minPopulationWhere = 1;
        for (int d = 2; d <= districts; ++d)
        {
            if (allDistricts[d] != null && allDistricts[d].getPopulation() < allDistricts[minPopulationWhere].getPopulation())
                minPopulationWhere = d;
        }

        while (this.budget >= allDistricts[minPopulationWhere].getPopulation() * minActionCost)
        {
            this.budget -= allDistricts[minPopulationWhere].getPopulation() * minActionCost;
            allDistricts[minPopulationWhere].applyCampaign(campaign[minActionId]);
        }
    }
}
