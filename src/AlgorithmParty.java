import static java.lang.Math.abs;

// Partia wykorzystująca strategię zachłanną w kampanii.
public class AlgorithmParty extends Party
{
    public AlgorithmParty(String name, int budget)
    {
        super(name, budget);
    }

    // Sprawdzenie każdej akcji na każdym okręgu i wybranie tej gdzie wpływ jest największy.
    @Override
    public  void launchCampaign(int districts, int parties, int traits, int actions, District[] allDistricts, int[][] campaign)
    {
        while (this.budget > 0)
        {
            int actionId = 0;
            int districtId = 0;
            int chosenActionCost = 0;
            int impactMade = 0;

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
                            && (districtId == 0 || impactMade < allDistricts[d].getDiffImpact(campaign[a])))
                    {
                        actionId = a;
                        districtId = d;
                        chosenActionCost = curActionCost;
                        impactMade = allDistricts[d].getDiffImpact(campaign[a]);
                    }

                }
            }

            // Każda była za droga/nieskuteczna.
            if (actionId == 0)
                return;

            this.budget -= allDistricts[districtId].getPopulation() * chosenActionCost;
            allDistricts[districtId].applyCampaign(campaign[actionId]);
        }
    }
}
