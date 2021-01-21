// Nadklasa wyborców na których głos nie ma wpływu kampania.
abstract public class NonInfluencableVoter extends Voter
{
    public NonInfluencableVoter(String name, String surname, int favParty, int district)
    {
        super(name, surname, favParty, district);
    }

    // Kampania nic nie zmienia.
    @Override
    public void getInfleuced(int[] shiftTrait)
    {
        return;
    }

    // Wpływ kampanii jest zawsze równy 0.
    @Override
    public int getImpactDiff(int[] shiftTrait)
    {
        return 0;
    }
}
