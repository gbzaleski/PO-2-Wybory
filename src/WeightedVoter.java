import static java.lang.Integer.max;
import static java.lang.Integer.min;

// Wyborca wszechstronny.
public class WeightedVoter extends Voter
{
    // Wagi według których wybiera kandydata.
    int[] weights;

    public WeightedVoter(String name, String surname, int favParty, int[] weights, int district)
    {
        super(name, surname, favParty, district);
        this.weights = weights;
    }

    @Override
    public void getInfleuced(int[] shiftTrait)
    {
        for (int w = 0; w < weights.length; ++w)
        {
            weights[w] += shiftTrait[w];
            if (weights[w] > 100)
                weights[w] = 100;
            else if (weights[w] < -100)
                weights[w] = -100;
        }
    }

    @Override
    public int getImpactDiff(int[] shiftTrait)
    {
        int[] possibleWeights = new int[weights.length];

        for (int i = 0; i < weights.length; ++i)
        {
            possibleWeights[i] = weights[i] + shiftTrait[i];
            possibleWeights[i] = max(-100, min(100, possibleWeights[i]));
        }

        int impact = 0;
        for (int i = 0; i < weights.length; ++i)
        {
            impact += possibleWeights[i] - weights[i];
        }
        return impact;
    }

    @Override
    public void castVote(District where, int[] voteGotten)
    {
        int whichParty = 0;
        int whichCandidate = 0;
        int bestValue = 0;
        for (int p = 1; p <= where.getPlurality(); ++p)
        {
            if (p == favParty || favParty == 0)
            {
                for (int c = 1; c <= where.getPopulation()/10; ++c)
                {
                    if (whichCandidate == 0)
                    {
                        whichParty = p;
                        whichCandidate = c;
                        for (int t = 0; t < weights.length; ++t)
                            bestValue += weights[t] * where.getCandidate(p, c).getTrait(t);
                        continue;
                    }

                    int curValue = 0;
                    for (int t = 0; t < weights.length; ++t)
                        curValue += weights[t] * where.getCandidate(p, c).getTrait(t);

                    if (bestValue < curValue)
                    {
                        bestValue = curValue;
                        whichCandidate = c;
                        whichParty = p;
                    }
                }
            }
        }

        where.applyVote(whichParty, whichCandidate);
        this.chosenCandidate = where.getCandidate(whichParty, whichCandidate);
        voteGotten[whichParty]++;
    }
}
