import java.util.Random;

// Żelazny wyborca.
public class SimpleVoter extends NonInfluencableVoter
{
    // Kandydat na którego głosuje wyborca (tzw. żelazny elektorat kandydata), 0 - gdy nie ma takiego.
    private int favCandidate;

    public SimpleVoter(String name, String surname, int favParty, int favCandidate, int district)
    {
        super(name, surname, favParty, district);
        this.favCandidate = favCandidate;
    }

    @Override
    public void castVote(District where, int[] voteGotten)
    {
        int whichParty = this.favParty;
        int whichCandidate = this.favCandidate;

        if (whichCandidate == 0)
        {
            Random randomiser = new Random();
            whichCandidate = randomiser.nextInt(where.getPopulation() / 10) + 1;
        }

        where.applyVote(whichParty, whichCandidate);
        this.chosenCandidate = where.getCandidate(whichParty, whichCandidate);
        voteGotten[whichParty]++;
    }
}
