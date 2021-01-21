import static java.lang.Math.abs;

// Partia działająca "z rozmachem".
public class LuxuriousParty extends Party
{
    public LuxuriousParty(String name, int budget)
    {
        super(name, budget);
    }

    // Wyszukanie i wykonanie najdroższej kampanii, która mieści się w budżecie.
    @Override
    public  void launchCampaign(int districts, int parties, int traits, int actions, District[] allDistricts, int[][] campaign)
    {
        while (this.budget > 0)
        {
            int actionId = 0;
            int districtId = 0;
            int chosenActionCost = 0;

            for (int d = 1; d <= districts; ++d)
            {
                if (allDistricts[d] == null)
                    continue;

                for (int a = 1; a <= actions; ++a)
                {
                    int curActionCost = 0;
                    for (int t = 0; t < traits; ++t)
                    {
                        curActionCost += abs(campaign[a][t]);
                    }

                    if (curActionCost * allDistricts[d].getPopulation() <= this.budget
                            && (districtId == 0 || chosenActionCost * allDistricts[districtId].getPopulation() < curActionCost * allDistricts[d].getPopulation()))
                    {
                        districtId = d;
                        chosenActionCost = curActionCost;
                        actionId = a;
                    }

                }
            }

            // Każda była za droga.
            if (actionId == 0)
                return;

            this.budget -= allDistricts[districtId].getPopulation() * chosenActionCost;
            allDistricts[districtId].applyCampaign(campaign[actionId]);
        }
    }
}


