import static java.lang.Math.abs;

// Partia wykorzystująca moją autorską strategię.
public class MyOwnParty extends Party
{
    public MyOwnParty(String name, int budget)
    {
        super(name, budget);
    }

    @Override
    // Moja własna strategia zachłannie oblicza stosunek kosztu kampanii do średniego wpływu na wyborce w okręgu,
    // następnie wybiera taką kampanie gdze ten stosuenek kosztów jest najniższy.
    public  void launchCampaign(int districts, int parties, int traits, int actions, District[] allDistricts, int[][] campaign)
    {
        while (this.budget > 0)
        {
            int actionId = 0;
            int districtId = 0;
            int chosenActionCost = 0;
            double worthness = 0;

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

                    double curWorthness = (double) allDistricts[d].getDiffImpact(campaign[a]) / (allDistricts[d].getPopulation() * curActionCost);

                    if (curActionCost * allDistricts[d].getPopulation() <= this.budget
                            && (districtId == 0 || worthness < curWorthness))
                    {
                        actionId = a;
                        districtId = d;
                        chosenActionCost = curActionCost;
                        worthness = curWorthness;
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
