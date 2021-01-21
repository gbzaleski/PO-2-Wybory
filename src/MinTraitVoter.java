// Wyborca minimalizujący jednocechowy.
public class MinTraitVoter extends NonInfluencableVoter
{
    // Cecha wg. której wybiera wyborca.
    int whichTrait;

    public MinTraitVoter(String name, String surname, int favParty, int whichTrait, int district)
    {
        super(name, surname, favParty, district);
        this.whichTrait = whichTrait;
    }

    @Override
    public void castVote(District where, int[] voteGotten)
    {
        int whichParty = 0;
        int whichCandidate = 0;
        for (int p = 1; p <= where.getPlurality() ; ++p)
        {
            if (p == favParty || favParty == 0)
            {
                for (int c = 1; c <= where.getPopulation()/10; ++c)
                {
                    if (whichCandidate == 0)
                    {
                        whichParty = p;
                        whichCandidate = c;
                    }
                    else if (where.getCandidate(p, c).getTrait(whichTrait) < where.getCandidate(whichParty, whichCandidate).getTrait(whichTrait))
                    {
                        whichParty = p;
                        whichCandidate = c;
                    }
                }
            }
        }

        where.applyVote(whichParty, whichCandidate);
        this.chosenCandidate = where.getCandidate(whichParty, whichCandidate);
        voteGotten[whichParty]++;
    }

}
